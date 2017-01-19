package com.hit.memoryunits;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.hit.algorithm.IAlgoCache;
import com.hit.algorithm.LRUAlgoCacheImpl;
import org.junit.Assert;

public class MMUTest 
{
	@Test
	public void test() 
	{
		int capacity = 4;
		Page<byte[]>[] pages;
		Long[] pageIds = {new Long(1), new Long(13), new Long(25)};
		Page<byte[]> p = new Page<byte[]>(new Long(82), new byte[]{0});
		IAlgoCache<Long, Long> algo = new LRUAlgoCacheImpl<>(capacity);
		MemoryManagementUnit mmu = new MemoryManagementUnit(capacity, algo);
		
		try 
		{
			pages = mmu.getPages(pageIds);
			Assert.assertEquals("[1, 13, 25] {1=1, 25=25, 13=13}", algo.toString());
			pageIds = new Long[]{new Long(12), new Long(82)};
			pages = mmu.getPages(pageIds);
			Assert.assertEquals("[13, 25, 12, 82] {82=82, 25=25, 12=12, 13=13}", algo.toString());
			Assert.assertEquals(pages[1].getPageId(), p.getPageId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}