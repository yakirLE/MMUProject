package com.hit.algorithm;

import org.junit.Assert;
import org.junit.Test;

public class IAlgoCacheTest 
{
	IAlgoCache<Integer, Integer> algo;
	
	@Test
	public void LRUAlgoCacheImplTest()
	{
		Integer res;
		algo = new LRUAlgoCacheImpl<>(3);
		
		ProcessElement(2, 20);
		ProcessElement(3, 30);
		res = ProcessElement(4, 40);
		Assert.assertEquals(null, res);
		ProcessElement(2, 21);
		res = ProcessElement(1, 10);
		Assert.assertEquals(new Integer(30), res);
		ProcessElement(3, 31);
		res = ProcessElement(7, 70);
		Assert.assertEquals(new Integer(20), res);
		ProcessElement(5, 50);
		ProcessElement(4, 41);
		res = ProcessElement(3, 32);
		Assert.assertEquals(new Integer(70), res);
	}
	
	@Test
	public void MFUAlgoCacheImplTest()
	{
		Integer res;
		algo = new MFUAlgoCacheImpl<>(3);
		
		ProcessElement(7, 70);
		ProcessElement(0, 0);
		res = ProcessElement(1, 10);
		Assert.assertEquals(null, res);
		res = ProcessElement(0, 1);
		Assert.assertEquals(new Integer(-1), res);
		res = ProcessElement(8, 80);
		Assert.assertEquals(new Integer(0), res);
		res = ProcessElement(8, 81);
		ProcessElement(9, 90);
		ProcessElement(1, 11);
		ProcessElement(1, 12);
		res = ProcessElement(3, 30);
		Assert.assertEquals(new Integer(10), res);
	}
	
	@Test
	public void SecondChanceAlgoCacheImplTest()
	{
		Integer res;
		algo = new SecondChanceAlgoCacheImpl<>(3);
		
		ProcessElement(2, 20);
		ProcessElement(30, 30);
		res = ProcessElement(2, 21);
		Assert.assertEquals(new Integer(-1), res);
		res = ProcessElement(1, 10);
		Assert.assertEquals(null, res);
		res = ProcessElement(5, 50);
		Assert.assertEquals(new Integer(30), res);
		ProcessElement(2, 22);
		res = ProcessElement(4, 40);
		Assert.assertEquals(new Integer(10), res);
		ProcessElement(5, 51);
		ProcessElement(3, 31);
		res = ProcessElement(2, 23);
		Assert.assertEquals(new Integer(40), res);
		ProcessElement(5, 52);
		res = ProcessElement(2, 24);
		Assert.assertEquals(new Integer(-1), res);
	}
	
	private Integer ProcessElement(Integer key, Integer value)
	{
		if(algo.getElement(key) == null)
		{
			return algo.putElement(key, value);
		}
		
		return -1;
	}

}
