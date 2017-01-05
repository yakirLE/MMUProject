package com.hit.algorithm;

import org.junit.Assert;

public class test {

	public static void main(String[] args) 
	{
		/*SecondChanceAlgoCacheImpl<Integer, Integer> sc = new SecondChanceAlgoCacheImpl<>(3);
		sc.putElement(2, 20);
		sc.putElement(3, 30);
		sc.getElement(2);
		sc.putElement(1, 10);
		sc.putElement(5, 50);
		sc.getElement(2);
		sc.putElement(4, 40);
		sc.getElement(5);
		sc.putElement(3, 30);
		sc.putElement(2, 20);
		sc.getElement(5);
		sc.getElement(2);
		System.out.println(sc.toString());*/
		/*MFUAlgoCacheImpl<Integer, Integer> mfu = new MFUAlgoCacheImpl<>(3);
		mfu.putElement(7, 70);
		mfu.putElement(0, 0);
		mfu.putElement(1, 10);
		mfu.getElement(0);
		mfu.putElement(8, 80);
		mfu.getElement(8);
		mfu.putElement(9, 90);
		mfu.getElement(1);
		mfu.getElement(1);
		mfu.putElement(3, 30);
		System.out.println(mfu.toString());
		mfu.getElement(1);*/
		LRUAlgoCacheImpl<Integer, Integer> lru = new LRUAlgoCacheImpl<>(3);
		ProcessElement(lru, 2, 20);
		System.out.println(lru.toString());
		ProcessElement(lru, 3, 30);
		System.out.println(lru.toString());
		ProcessElement(lru, 4, 40);
		System.out.println(lru.toString());
		ProcessElement(lru, 2, 21);
		System.out.println(lru.toString());
		ProcessElement(lru, 1, 10);
		System.out.println(lru.toString());
		ProcessElement(lru, 3, 31);
		System.out.println(lru.toString());
		ProcessElement(lru, 7, 70);
		System.out.println(lru.toString());
		ProcessElement(lru, 5, 50);
		System.out.println(lru.toString());
		ProcessElement(lru, 4, 41);
		System.out.println(lru.toString());
		ProcessElement(lru, 3, 32);
		System.out.println(lru.toString());
	}
	
	private static Integer ProcessElement(IAlgoCache<Integer, Integer> algo, Integer key, Integer value)
	{
		if(algo.getElement(key) == null)
		{
			return algo.putElement(key, value);
		}
		
		return -1;
	}

}
