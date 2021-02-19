package org.jivesoftware.util.cache;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.directtruststandards.timplus.cluster.cache.DelegatedClusteredCacheFactory;
import org.jivesoftware.openfire.SessionManager;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.cluster.ClusterException;
import org.jivesoftware.openfire.cluster.ClusterManager;
import org.jivesoftware.openfire.cluster.ClusterNode;
import org.jivesoftware.openfire.cluster.ClusterNodeInfo;
import org.jivesoftware.openfire.cluster.ClusterNodeStatus;
import org.jivesoftware.openfire.cluster.NodeID;
import org.jivesoftware.openfire.spi.RoutingTableImpl;
import org.jivesoftware.util.JiveGlobals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Clustered cache factory for creating clustered capable caches.  Cache creation is delegated to a 
 * delegated to a name class factory specified by the setting cache.clustering.clustered.delegatedCacheFactoryClass.
 * <br>
 * By default, it uses the org.directtruststandards.timplus.cluster.cache.RedisDelegatedClusterCacheFactory located in the
 * timplus-cluster-redis module.
 * @author Greg Meyer
 * @since 1.0
 */
public class ClusteredCacheFactory implements CacheFactoryStrategy
{
	private static final Logger Log = LoggerFactory.getLogger(ClusteredCacheFactory.class);

	public static String CLUSTER_DELEGATED_CACHE_FACTORY_CLASS = "cache.clustering.clustered.delegatedCacheFactoryClass";
	
	public static DelegatedClusteredCacheFactory cacheFactory;
	
    private static Set<String> remoteClusteredCacheNames;
	
    private Map<Object, LockAndCount> locks = new ConcurrentHashMap<>();
    
    static
    {
    	remoteClusteredCacheNames = new HashSet<>();
    	remoteClusteredCacheNames.add(RoutingTableImpl.ANONYMOUS_C2S_CACHE_NAME);
    	remoteClusteredCacheNames.add(RoutingTableImpl.C2S_CACHE_NAME);
    	remoteClusteredCacheNames.add(RoutingTableImpl.COMPONENT_CACHE_NAME);
    	remoteClusteredCacheNames.add(RoutingTableImpl.C2S_SESSION_NAME);
    	remoteClusteredCacheNames.add(SessionManager.C2S_INFO_CACHE_NAME);
    }
    
    @SuppressWarnings("unchecked")
    public ClusteredCacheFactory() 
    {
    	final String className = JiveGlobals.getProperty(CLUSTER_DELEGATED_CACHE_FACTORY_CLASS, 
    			"org.directtruststandards.timplus.cluster.cache.RedisDelegatedClusterCacheFactory");
    	
    	try
    	{
    		final Class<DelegatedClusteredCacheFactory> factoryClass = (Class<DelegatedClusteredCacheFactory>)Class.forName(className);
    		
    		cacheFactory = factoryClass.newInstance();
    	}
    	catch (Exception e)
    	{
    		Log.warn("Could not create delegated clustered factory {}.", className, e);
    	}
    }

	@Override
	public boolean startCluster() 
	{
		return true;
	}

	@Override
	public void stopCluster() 
	{
		// clear all caches
		CacheFactory.clearCaches();
	}

	@Override
	public Cache<?,?> createCache(String name) 
	{
        // Get cache configuration from system properties or default (hardcoded) values
        long maxSize = CacheFactory.getMaxCacheSize(name);
        long lifetime = CacheFactory.getMaxCacheLifetime(name);
        
        
        // Create cache with located properties... determine if the cache should be
        // a local or remote cache
        Cache<?,?> retVal = null;
        
        if (remoteClusteredCacheNames.contains(name))
        {
        	retVal = cacheFactory.createCache(name, maxSize, lifetime, XMPPServer.getInstance().getNodeID());
        }
        else
        {
        	retVal = new DefaultCache(name, maxSize, lifetime);
        }
        
        return retVal;
	}

	@Override
	public void destroyCache(Cache cache) 
	{
		cache.clear();
		
	}

	@Override
	public boolean isSeniorClusterMember() 
	{
		try
		{
			final ClusterNode node = ClusterManager.getClusterNodeProvider().getClusterMember(XMPPServer.getInstance().getNodeID());
			
			return node.getNodeStatus().equals(ClusterNodeStatus.NODE_MASTER);
				
		}
		catch (ClusterException e)
		{
			Log.warn("Error gettig senior cluster information.  isSeniorClusterMember defaulting to false.", e);
			return false;
		}
	}

	@Override
	public Collection<ClusterNodeInfo> getClusterNodesInfo() 
	{
		final Collection<ClusterNodeInfo> retVal = new ArrayList<>();
		
		try
		{
			final Collection<ClusterNode> nodes = ClusterManager.getClusterNodeProvider().getClusterMembers();
			
			for (final ClusterNode node : nodes)
			{
				final ClusterNodeInfo info = new ClusterNodeInfo()
				{

					@Override
					public String getHostName() {
						return node.getNodeHost();
					}

					@Override
					public NodeID getNodeID() {
						return node.getNodeId();
					}

					@Override
					public long getJoinedTime() {
						return node.getNodeJoinedDtTm().toEpochMilli();
					}

					@Override
					public boolean isSeniorMember() {
						return node.getNodeStatus().equals(ClusterNodeStatus.NODE_MASTER);
					}
					
				};
				
				retVal.add(info);
			}
		}
		catch (ClusterException e)
		{
			Log.warn("Error getting ClusterNodesInfo.  getClusterNodesInfo defaulting to empty list.", e);
			Collections.emptyList();
		}
		
		return retVal;
	}

	@Override
	public int getMaxClusterNodes() 
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public byte[] getSeniorClusterMemberID() 
	{
		try
		{
			final Collection<ClusterNode> nodes = ClusterManager.getClusterNodeProvider().getClusterMembers();
			
			for (ClusterNode node : nodes)
			{
				if (node.getNodeStatus().equals(ClusterNodeStatus.NODE_MASTER))
					return node.getNodeIP().getBytes();
			}
				
			return null;
		}
		catch (ClusterException e)
		{
			Log.warn("Error gettig senior cluster information.  getSeniorClusterMemberID defaulting to null.", e);
			return null;
		}
	}

	@Override
	public byte[] getClusterMemberID() 
	{
		return XMPPServer.getInstance().getNodeID().toByteArray();
	}

	@Override
	public long getClusterTime() 
	{
		// assumes time is synced from an exteranl source like NTP
		return Instant.now().toEpochMilli();
				
	}

	@Override
	public void doClusterTask(ClusterTask<?> task) 
	{

	}

	@Override
	public void doClusterTask(ClusterTask<?> task, byte[] nodeID) 
	{
		
	}

	@Override
	public <T> Collection<T> doSynchronousClusterTask(ClusterTask<T> task, boolean includeLocalMember) 
	{
		return Collections.emptyList();
	}

	@Override
	public <T> T doSynchronousClusterTask(ClusterTask<T> task, byte[] nodeID) {
		// TODO Auto-generated method stub
		return task.getResult();
	}

	@Override
	public void updateCacheStats(Map<String, Cache> caches) 
	{	
	}

	@Override
	public Lock getLock(Object key, Cache cache) 
	{
        Object lockKey = key;
        if (key instanceof String) {
            lockKey = ((String) key).intern();
        }

        return new LocalLock(lockKey);
	}

	@Override
	public String getPluginName() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClusterNodeInfo getClusterNodeInfo(byte[] nodeID) 
	{	
		ClusterNodeInfo retVal = null;
		
		try
		{
			final ClusterNode node = ClusterManager.getClusterNodeProvider().getClusterMember(XMPPServer.getInstance().getNodeID());
			
			retVal = new ClusterNodeInfo()
			{

				@Override
				public String getHostName() {
					return node.getNodeHost();
				}

				@Override
				public NodeID getNodeID() {
					return node.getNodeId();
				}

				@Override
				public long getJoinedTime() {
					return node.getNodeJoinedDtTm().toEpochMilli();
				}

				@Override
				public boolean isSeniorMember() {
					return node.getNodeStatus().equals(ClusterNodeStatus.NODE_MASTER);
				}
				
			};
			
		}
		catch (ClusterException e)
		{
			Log.warn("Error getting ClusterNodesInfo.  getClusterNodeInfo defaulting to null.", e);
			Collections.emptyList();
		}
		
		return retVal;
	}
	
    private class LocalLock implements Lock {
        private final Object key;

        LocalLock(Object key) {
            this.key = key;
        }

        @Override
        public void lock(){
            acquireLock(key);
        }

        @Override
        public void	unlock() {
            releaseLock(key);
        }

        @Override
        public void	lockInterruptibly(){
            throw new UnsupportedOperationException();
        }

        @Override
        public Condition newCondition(){
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean 	tryLock() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean 	tryLock(long time, TimeUnit unit) {
            throw new UnsupportedOperationException();
        }

    }

    private void acquireLock(Object key) {
        ReentrantLock lock = lookupLockForAcquire(key);
        lock.lock();
    }

    private void releaseLock(Object key) {
        ReentrantLock lock = lookupLockForRelease(key);
        lock.unlock();
    }

    private ReentrantLock lookupLockForAcquire(Object key) {
        synchronized(key) {
            LockAndCount lac = locks.get(key);
            if (lac == null) {
                lac = new LockAndCount(new ReentrantLock());
                lac.count = 1;
                locks.put(key, lac);
            }
            else {
                lac.count++;
            }

            return lac.lock;
        }
    }

    private ReentrantLock lookupLockForRelease(Object key) {
        synchronized(key) {
            LockAndCount lac = locks.get(key);
            if (lac == null) {
                throw new IllegalStateException("No lock found for object " + key);
            }

            if (lac.count <= 1) {
                locks.remove(key);
            }
            else {
                lac.count--;
            }

            return lac.lock;
        }
    }
    
    private static class LockAndCount {
        final ReentrantLock lock;
        int count;

        LockAndCount(ReentrantLock lock) {
            this.lock = lock;
        }
    }
}
