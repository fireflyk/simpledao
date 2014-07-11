package com.codinghero.sql.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import com.codinghero.sql.util.entity.AutoArrayList;

/**
 * Bean utility. Support deep copy.
 * 
 * @author liutong01
 * 
 */
public final class BeanUtils {

	/**
	 * new instance quietly, not throw exception.<br/>
	 * u should test your own program pass on runtime, not only for compile.
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T newInstance(Class<T> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * new instance quietly, not throw exception.<br/>
	 * u should test your own program pass on runtime, not only for compile.
	 * 
	 * @param className
	 * @return
	 */
	public static Object newInstance(String className){
		try {
			return newInstance(Class.forName(className));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param bean
	 * @param field
	 * @return
	 */
	public static Class<?> getPropertyType(Object bean, String field) {
		try {
			return PropertyUtils.getPropertyType(bean, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * 
	 * @param orig
	 * @param field
	 * @return
	 */
	public static Object getPropertyValue(Object orig, String field) {
		try {
			return PropertyUtils.getSimpleProperty(orig, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void setProperty(Object bean, String name, Object value) {
		try {
			PropertyUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSimpleProperty(Object bean, String name, Object value) {
		try {
			PropertyUtils.setSimpleProperty(bean, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * copy object to object
	 * 
	 * @param dest
	 * @param orig
	 */
	public static void copyProperties(Object dest, Object orig){
		try {
			if (orig != null)
				PropertyUtils.copyProperties(dest, orig);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * copy an object & return a new object
	 * 
	 * @param orig
	 * @param destClazz
	 * @return
	 */
	public static <T> T copyProperties(Object orig, Class<T> destClazz) {
		T dest = newInstance(destClazz);
		copyProperties(dest, orig);
		return dest;
	}

	/**
	 * copy a field of an object 
	 * 
	 * @param dest
	 * @param orig
	 * @param getField
	 * @param setField
	 */
	public static void copyProperties(Object dest, Object orig, String getField, String setField){
		try {
			if (orig != null) {
				Object origValue = PropertyUtils.getSimpleProperty(orig, getField);
				PropertyUtils.setSimpleProperty(dest, setField, origValue);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	/**
	 * copy a field of an object & return a new object
	 * 
	 * @param orig
	 * @param destClazz
	 * @param getField
	 * @param setField
	 * @return
	 */
	public static <T> T copyProperties(Object orig, Class<T> destClazz, String getField, String setField) {
		T dest = newInstance(destClazz);
		copyProperties(dest, orig, getField, setField);
		return dest;
	}
	
	/**
	 * copy map to object
	 * 
	 * @param dest
	 * @param orig
	 */
	public static void copyProperties(Object dest, Map<?, ?> orig){
		try {
			org.apache.commons.beanutils.BeanUtils.populate(dest, orig);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * copy map an object & return a new object
	 * 
	 * @param orig
	 * @param destClazz
	 * @return
	 */
	public static <T> T copyProperties(Map<?, ?> orig, Class<T> destClazz){
		T dest = newInstance(destClazz);
		copyProperties(dest, orig);
		return dest;
	}

	/**
	 * copy object to map & return a new map
	 * 
	 * @param orig
	 * @return
	 */
	public static Map<?, ?> copyProperties(Object orig){
		try {
			return PropertyUtils.describe(orig);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 
	 * @param field1
	 * @param orig1
	 * @param field2
	 * @param orig2
	 * @param destClazz
	 * @return
	 */
	public static <T> T copyProperties(String field1, Object orig1, String field2, Object orig2, Class<T> destClazz) {
		T dest = newInstance(destClazz);
		try {
			PropertyUtils.setSimpleProperty(dest, field1, orig1);
			PropertyUtils.setSimpleProperty(dest, field2, orig2);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return dest;
	}

	public static <T1, T2> List<T2> copyList(List<T1> origList, Class<T2> destClazz) {
		if (origList == null)
			return null;
		List<T2> resultList = new ArrayList<T2>();
		for (T1 orig : origList) {
			resultList.add(copyProperties(orig, destClazz));
		}
		return resultList;
	}

	public static <T> List<T> copyAutoArrayList(List<T> list, Class<T> clazz) {
		if (list == null)
			return null;
		List<T> resultList = new AutoArrayList<T>(clazz);
		for (T t : list) {
			resultList.add(t);
		}
		return resultList;
	}

	public static <T1, T2> List<T2> copyList(List<T1> origList, Class<T2> destClazz, String getField, String setField) {
		if (origList == null)
			return null;
		List<T2> resultList = new ArrayList<T2>();
		for (T1 orig : origList) {
			resultList.add(copyProperties(orig, destClazz, getField, setField));
		}
		return resultList;
	}

	public static <T1, T2> List<T2> copyList(String setField1, Object orig, String getField2, String setField2, List<T1> origList, Class<T2> destClazz) {
		if (origList == null)
			return null;
		List<T2> resultList = new ArrayList<T2>();
		for (T1 origElement : origList) {
			try {
				Object field2Value = PropertyUtils.getSimpleProperty(origElement, getField2);
				resultList.add(copyProperties(setField1, orig, setField2, field2Value, destClazz));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return resultList;
	}

	/**
	 * use <code>copyList(List&lt;T1&gt; list, Class&lt;T2&gt; destClazz)</code> replaced
	 * 
	 * @param dtoList
	 * @param formClazz
	 * @return
	 */
	@Deprecated
	public static <T1, T2> List<T2> dtoToForm(List<T1> dtoList, Class<T2> formClazz) {
		return copyList(dtoList, formClazz);
	}
}
