package org.apache.commons.beanutils.converters;

import junit.framework.Assert;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

public class ConverterTest {

	@Test
	public void testIntegerConvert() {
		int i = 1;
		Integer j = new Integer(1);
		Long k = 1L;

		Assert.assertEquals(Integer.class,
				new IntegerConverter().convert(Integer.class, i).getClass());
		Assert.assertEquals(Integer.class,
				new IntegerConverter().convert(Integer.class, j).getClass());
		Assert.assertEquals(Integer.class,
				new IntegerConverter().convert(Integer.class, k).getClass());
	}
	
	@Test
	public void testDateConvert() {
		long currentTime = System.currentTimeMillis();
		java.sql.Date date = new java.sql.Date(currentTime);
		java.sql.Time time = new java.sql.Time(currentTime);
		java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTime);
		java.util.Date expected = new java.util.Date(currentTime); 
		
		Assert.assertEquals(expected, ConvertUtils.convert(date, java.util.Date.class));
		Assert.assertEquals(expected, ConvertUtils.convert(time, java.util.Date.class));
		Assert.assertEquals(expected, ConvertUtils.convert(timestamp, java.util.Date.class));
	}
}
