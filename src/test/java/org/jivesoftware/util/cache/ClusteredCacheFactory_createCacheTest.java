package org.jivesoftware.util.cache;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.directtruststandards.timplus.cluster.cache.RedisCache;
import org.jivesoftware.openfire.spi.RoutingTableImpl;
import org.junit.jupiter.api.Test;

public class ClusteredCacheFactory_createCacheTest extends BaseTest
{
	@Test
	public void testCreateClusteredCache_assertClusteredCacheCreated()
	{
		final ClusteredCacheFactory factory = new ClusteredCacheFactory();
		
		final Cache<?,?> cache = factory.createCache(RoutingTableImpl.ANONYMOUS_C2S_CACHE_NAME);
		
		assertTrue(cache instanceof RedisCache);
	}
	
	@Test
	public void testCreateDefaultCache_assertDefaultCacheCreated()
	{
		final ClusteredCacheFactory factory = new ClusteredCacheFactory();
		
		final Cache<?,?> cache = factory.createCache("testcache");
		
		assertTrue(cache instanceof DefaultCache);
	}	
}
