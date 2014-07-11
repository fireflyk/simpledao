package com.codinghero.sql.util;

import java.util.ArrayList;
import java.util.List;

public final class ArrayUtils {

	/**
	 * check if the array is empty
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> boolean isEmpty(T[] arr) {
		if (arr == null || arr.length == 0)
			return true;
		else
			return false;
	}

	/**
	 * transform the array to list
	 * 
	 * @param tArr
	 * @return
	 */
	public static <T> List<T> toList(T[] tArr) {
		if (tArr == null)
			return null;
		return toList(tArr, 0, tArr.length - 1);
	}

	public static List<Integer> toList(int[] tArr) {
		if (tArr == null)
			return null;
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < tArr.length; i++) {
			list.add(tArr[i]);
		}
		return list;
	}

	/**
	 * transform the array to list
	 * 
	 * @param tArr
	 * @param begin
	 * @param end
	 * @return
	 */
	public static <T> List<T> toList(T[] tArr, int begin, int end) {
		if (tArr == null)
			return null;
		List<T> list = new ArrayList<T>();
		for (int i = begin; i <= end; i++) {
			T t = tArr[i];
			list.add(t);
		}
		return list;
	}

	@Deprecated
	public static Integer[] toArray(int[] arr) {
		if (arr == null)
			return null;
		Integer[] integerArr = new Integer[arr.length];
		for (int i = 0; i < arr.length; i++) {
			integerArr[i] = arr[i];
		}
		return integerArr;
	}

	public static int[] toArray(List<Integer> list) {
		if (list == null)
			return null;
		int[] result = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}
		return result;
	}

	public static int[] reverse(int[] arr) {
		int[] result = new int[arr.length];
		for (int i = 0; i < arr.length; i--) {
			result[arr.length - 1 - i] = arr[i];
		}
		return result;
	}
	
	public static int[] merge(int[] arr1, int[] arr2) {
		if (arr1 == null)
			return merge(new int[] {}, arr2);
		else if (arr2 == null)
			return merge(arr1, new int[] {});
		else {
			int[] result = new int[arr1.length + arr2.length];
			for (int i = 0; i < arr1.length; i++)
				result[i] = arr1[i];
			for (int i = 0; i < arr2.length; i++)
				result[i + arr1.length] = arr2[i];
			return result;
		}
	}
	
	public static int[] merge(int[] arr, int num) {
		if (arr == null)
			return merge(new int[] {}, num);
		else {
			int[] result = new int[arr.length + 1];
			for (int i = 0; i < arr.length; i++)
				result[i] = arr[i];
			result[result.length - 1] = num;
			return result;
		}
	}

	/**
	 * disorder the array random
	 * 
	 * @param arr
	 * @return
	 */
	public int[][] disorder(int[][] arr) {
		int exchangeTimes = arr.length * 4 / 3;
		for (int i = 0; i < exchangeTimes; i++) {
			// 随机数得到交换的index值
			int index1 = NumberUtils.randomInt(arr.length);
			int index2 = index1;
			while (index1 != index2)
				index2 = NumberUtils.randomInt(arr.length);
			arr = exchange(arr, index1, index2);
		}
		return arr;
	}

	/**
	 * exchange the two item of the array
	 * 
	 * @param arr
	 * @param index1
	 * @param index2
	 * @return
	 */
	public int[][] exchange(int[][] arr, int index1, int index2) {
		int length = arr[index1].length;
		int[] temp = new int[length];
		for (int i = 0; i < length; i++) {
			temp[i] = arr[index1][i];
		}
		for (int i = 0; i < length; i++) {
			arr[index1][i] = arr[index2][i];
		}
		for (int i = 0; i < length; i++) {
			arr[index2][i] = temp[i];
		}
		return arr;
	}
}
