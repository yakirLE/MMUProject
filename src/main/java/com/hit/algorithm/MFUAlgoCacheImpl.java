package com.hit.algorithm;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MFUAlgoCacheImpl<K, V> implements IAlgoCache<K, V> 
{
	private Map<K, V> cache;
	private Map<K, Integer> cacheCounter;
	private int capacity;
	
	public MFUAlgoCacheImpl(int capacity)
	{
		cache = new HashMap<>(capacity);
		cacheCounter = new HashMap<>();
		this.capacity = capacity;
	}
	
	@Override
	public V getElement(K key)
	{
		Integer counter;
		V returnedValue = null;
		
		if(cache.containsKey(key))
		{
			counter = cacheCounter.get(key);
			cacheCounter.put(key, ++counter);
			returnedValue = cache.get(key);
		}
		
		return returnedValue;
	}
	
	public V putElement(K key, V value)
	{
		K maxKey;
		V returnedValue = null;
		
		if(capacity == cache.size())
		{
			maxKey = getMax();
			returnedValue = cache.get(maxKey);
			removeElement(maxKey);
		}
		
		cache.put(key, value);
		cacheCounter.put(key, 0);
		
		return returnedValue;
	}
	
	private K getMax() 
	{
		Integer maxValueInMap = (Collections.max(cacheCounter.values()));
        for (Entry<K, Integer> entry : cacheCounter.entrySet())
        {
            if (entry.getValue() == maxValueInMap)
                return entry.getKey();
        }
        
        return null;
	}
	
	@Override
	public void removeElement(K key)
	{
		if(cache.containsKey(key))
		{
			cache.remove(key);
			cacheCounter.remove(key);
		}
	}
	
	public String toString()
	{
		return cache.toString() + " " + cacheCounter.toString();
	}
}
