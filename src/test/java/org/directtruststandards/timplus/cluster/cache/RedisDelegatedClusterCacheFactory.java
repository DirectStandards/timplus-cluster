package org.directtruststandards.timplus.cluster.cache;

import static org.mockito.Mockito.mock;

import org.jivesoftware.openfire.cluster.NodeID;
import org.jivesoftware.util.cache.Cache;

public class RedisDelegatedClusterCacheFactory implements DelegatedClusteredCacheFactory
{

	@Override
	public Cache<?,?> createCache(String name, long maxSize, long maxLifetime,
			NodeID nodeId) 
	{
		return mock(RedisCache.class);
	}

}
