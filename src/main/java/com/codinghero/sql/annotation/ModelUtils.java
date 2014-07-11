package com.codinghero.sql.annotation;

import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;

public class ModelUtils {
	public static String getTableName(Class<?> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		if (table != null && !StringUtils.isEmpty(table.name()))
			return table.name();
		else
			return clazz.getSimpleName();
	}
	
	public static boolean isIgnore(Field field) {
		Ignore ignore = field.getAnnotation(Ignore.class);
		if (ignore == null)
			return false;
		else
			return true;
	}
}
