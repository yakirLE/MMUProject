package com.hit.algorithm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SecondChanceAlgoCacheImpl<K, V> implements IAlgoCache<K, V> 
{
	private LinkedHashMap<K, V> cache;
	private Map<K, Integer> cacheReference;
	private int capacity;
	
	public SecondChanceAlgoCacheImpl(int capacity)
	{
		cache = new LinkedHashMap<>(capacity);
		cacheReference = new HashMap<>();
		this.capacity = capacity;
	}
	
	@Override
	public V getElement(K key)
	{
		V returnedValue = null;
		
		if(cache.containsKey(key))
		{
			cacheReference.put(key, 1);
			returnedValue = cache.get(key);
		}
		
		return returnedValue;
	}
	
	@Override
	public V putElement(K key, V value)
	{
		V returnedValue = null;
		
		if(capacity == cache.size())
			returnedValue = removeElementWithoutReference();
		cache.put(key, value);
		cacheReference.put(key, 0);
		
		return returnedValue;
	}
	
	private V removeElementWithoutReference()
	{
		K elementToRemove = cache.entrySet().iterator().next().getKey();
		V returnedValue = null;
		
		for(K key : cache.keySet())
		{
			if(cacheReference.get(key).equals(0))
			{
				elementToRemove = key;
				break;
			}
			else
				cacheReference.put(key, 0);
		}
		
		returnedValue = cache.get(elementToRemove);
		removeElement(elementToRemove);
		
		return returnedValue;
	}
	
	@Override
	public void removeElement(K key)
	{
		if(cache.containsKey(key))
		{
			cache.remove(key);
			cacheReference.remove(key);
		}
	}
}
