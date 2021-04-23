package org.directtruststandards.timplus.cluster.cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.jivesoftware.openfire.cluster.NodeID;
import org.jivesoftware.util.cache.Cache;

public class RedisCache implements Cache
{

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getMaxCacheSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxCacheSize(int maxSize) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getMaxLifetime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxLifetime(long maxLifetime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCacheSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCacheHits() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getCacheMisses() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void purgeClusteredNodeCaches(NodeID node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNodeCachePurgeable() {
		// TODO Auto-generated method stub
		return false;
	}

}
