package com.codinghero.sql.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

public final class NumberUtils {
	
	public static boolean isNumber(char c) {
		if (c >= '0' && c <= '9')
			return true;
		else
			return false;
	}
	
	public static boolean isNumber(Object obj) {
		if (obj instanceof Byte) {
			return true;
		} else if (obj instanceof Character) {
			return true;
		} else if (obj instanceof Integer) {
			return true;
		} else if (obj instanceof Long) {
			return true;
		} else if (obj instanceof Float) {
			return true;
		} else if (obj instanceof Double) {
			return true;
		} else if (obj instanceof BigInteger) {
			return true;
		} else if (obj instanceof BigDecimal) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNumber(Class<?> clazz) {
		if (Byte.TYPE.equals(clazz)) {
			return true;
		} else if (Character.TYPE.equals(clazz)) {
			return true;
		} else if (Integer.TYPE.equals(clazz)) {
			return true;
		} else if (Long.TYPE.equals(clazz)) {
			return true;
		} else if (Float.TYPE.equals(clazz)) {
			return true;
		} else if (Double.TYPE.equals(clazz)) {
			return true;
		} else if (new BigInteger("0").getClass().equals(clazz)) {
			return true;
		} else if (new BigDecimal("0").getClass().equals(clazz)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isInteger(String str) {
		if (StringUtils.isEmpty(str))
			return false;
		else if (str.charAt(0) == '-')
			if (str.length() == 1)
				return false;
			else if(str.equals("-0"))
				return false;
			else
				return isNonNegativeInteger(str.substring(1, str.length()));
		else
			return isNonNegativeInteger(str);
	}
	
	public static boolean isNonNegativeInteger(String str) {
		// wrong input
		if (StringUtils.isEmpty(str))
			return false;
		
		// special input: 0
		if (str.equals("0"))
			if (str.length() == 1)
				return true;
			else
				return false;
		
		// judge it is a number or not
		char[] cArr = str.toCharArray();
		for (char c : cArr) {
			if (!isNumber(c))
				return false;
		}
		return true;
	}
	
	public static Integer toInteger(boolean b) {
		if (b)
			return 1;
		else
			return 0;
	}
	
	public static <T extends Comparable<? super T>> List<T> max(
			List<T> nums, int n) {
		Collections.sort(nums);
		return nums.subList(0, n);
	}

	public static int randomInt(int n) {
		return new Random().nextInt(n);
	}

	public static int randomInt(int start, int end) {
		return new Random().nextInt(end - start) + start;
	}

	public static boolean randomBoolean(double probability) {
		double d = new Random().nextDouble();
		if (d < probability)
			return true;
		else
			return false;
	}
	
	public static List<Integer> randomSelect(List<Integer> list, int num) {
		List<Integer> tempList = new ArrayList<Integer>();
		for (Integer i : list)
			tempList.add(i);
				
		if (num >= tempList.size())
			return tempList;
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < num; i++) {
			int random = randomInt(tempList.size());
			result.add(tempList.get(random));
			tempList.remove(random);
		}
		return result;
	}

	public static int randomOneOrMinusOne(double probability) {
		if (randomBoolean(probability))
			return 1;
		else
			return -1;
	}
}
