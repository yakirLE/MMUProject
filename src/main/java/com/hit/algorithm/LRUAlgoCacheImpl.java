package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LRUAlgoCacheImpl<K, V> implements IAlgoCache<K, V>
{
	private List<K> cache;
	private Map<K, V> mapKeys;
	private int capacity;
	
	public LRUAlgoCacheImpl(int capacity)
	{
		cache = new LinkedList<K>();
		mapKeys = new HashMap<>();
		this.capacity = capacity;
	}
	
	@Override
	public V getElement(K key)
	{
		if(mapKeys.containsKey(key))
		{
			cache.remove(key);
			cache.add(key);
			
			return mapKeys.get(key);
		}
		
		return null;
	}
	
	@Override
	public V putElement(K key, V value)
	{
		final int firstElement = 0;
		K removedKey;
		V removedValue = null;
		if(cache.size() == capacity)
		{
			removedKey = cache.remove(firstElement);
			removedValue = mapKeys.get(removedKey);
			removeElement(removedKey);
		}
		
		cache.add(key);
		mapKeys.put(key, value);
		
		return removedValue;
	}
	
	@Override
	public void removeElement(K key)
	{
		if(mapKeys.containsKey(key))
		{
			cache.remove(key);
			mapKeys.remove(key);
		}
	}
	
	public String toString()
	{
		return cache.toString() + " " + mapKeys.toString();
	}
}
