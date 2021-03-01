package org.directtruststandards.timplus.cluster.cache;

import org.jivesoftware.openfire.cluster.NodeID;
import org.jivesoftware.util.cache.Cache;

/**
 * Class factory used for delegating the creation of clustered cache objects.
 * @author Greg Meyer
 * @since 1.0
 */
public interface DelegatedClusteredCacheFactory
{
	/**
	 * Creates a clustered cache object.
	 * @param name The name of the cache.
	 * @param maxSize The maximum size of the cache for locally cached objects.
	 * @param maxLifetime The amount of time (in milliseconds) objects should remain in the cache.
	 * @param nodeId The cluster node number that owns this cache.  
	 * @param nodePurgeable Indicates if the cache can be purged by cluster node.
	 * @return A clustered cache object bound to a specific implementation.
	 */
	public Cache<?, ?> createCache(final String name, final long maxSize, final long maxLifetime, final NodeID nodeId, boolean nodePurgeable);
}
