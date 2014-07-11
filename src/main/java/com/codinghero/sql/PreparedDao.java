package com.codinghero.sql;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import com.codinghero.sql.annotation.ModelUtils;
import com.codinghero.sql.util.BeanUtils;

public class PreparedDao extends Dao {

	public PreparedDao() { }
	
	public PreparedDao(String driverClass, String jdbcUrl, String userId, String password) {
		super(driverClass, jdbcUrl, userId, password);
	}
	
	public int[] executeBatch(String sql, Object[][] values) throws SQLException {
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		if (values != null) {
			for (Object[] eachValues : values) {
				if (eachValues != null) {
					for (int i = 0; i < eachValues.length; i++) {
						stmt.setObject(i + 1, eachValues[i]);
					}
				}
				stmt.addBatch();
			}
		}		
		int[] result = stmt.executeBatch();
		closeStatement(stmt);
		return result;
	}
	
	public Map<String, Object> selectOne(String sql, Object[] values) throws SQLException {
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = query(stmt, values);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(rs.next()) {
			selectOne(rs, map);
		}
		close(stmt, rs);
		return map;
	}

	public <T> T selectOne(String sql, Class<T> clazz, Object[] values) throws SQLException {
		log.info(sql);
//		if(columns!=null && values!=null && columns.length!=values.length)
//			throw new SQLException("columns & values length don't equal");
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = query(stmt, values);
		T bean = BeanUtils.newInstance(clazz);
		if(rs.next()) {
			selectOne(rs, bean);
		}
		close(stmt, rs);
		return bean;
	}
	

	public <T> List<T> selectList(String sql, Class<T> clazz, Object[] values) throws SQLException {
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = query(stmt, values);
		List<T> resultList = new ArrayList<T>();
		while (rs.next()) {
			T bean = BeanUtils.newInstance(clazz);
			selectOne(rs, bean);
			resultList.add(bean);
		}
		close(stmt, rs);
		return resultList;
	}

	public List<Map<String, Object>> selectList(String sql, Object[] values) throws SQLException {
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = query(stmt, values);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			selectOne(rs, map);
			resultList.add(map);
		}
		close(stmt, rs);
		return resultList;
	}
	
	public <T> int insertOne(T t) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		String sql = getInsertSql(t);
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		Object[] values = getInsertValues(t);
		return execute(stmt,values);
	}
	
	public int insertOne(String tableName, Map<String, Object> map) throws SQLException {
		String sql = getInsertSql(tableName, map);
		log.info(sql);
		PreparedStatement stmt = conn.prepareStatement(sql);
		Object[] values = getInsertValues(map);
		return execute(stmt,values);
	}
	
	public <T> int[] insertList(List<T> list) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		if (list != null && list.size() > 0) {
			// get values
			List<Object[]> tempValues = new ArrayList<Object[]>();
			for (T t : list) {
				tempValues.add(getInsertValues(t));
			}
			Object[][] values = new Object[tempValues.size()][];
			for (int i = 0; i < tempValues.size(); i++) {
				values[i] = tempValues.get(i);
			}
			// get sql & execute
			return executeBatch(getInsertSql(list.get(0)), values);
		}
		return null;
	}

	
	protected ResultSet query(PreparedStatement stmt, Object[] values) throws SQLException {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				stmt.setObject(i + 1, values[i]);
			}
		}
		return stmt.executeQuery();
	}
	
	protected int execute(PreparedStatement stmt, Object[] values) throws SQLException {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				stmt.setObject(i + 1, values[i]);
			}
		}
		return stmt.executeUpdate();
	}
	
	private <T> String getInsertSql(T t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		// get table name
		final String tableName = ModelUtils.getTableName(t.getClass());
		// get column name
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(t);
		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			if (!"class".equals(pd.getName())) {
				Object value = PropertyUtils.getProperty(t, pd.getName());
				Field field = t.getClass().getDeclaredField(pd.getName());
				if (!ModelUtils.isIgnore(field) && value != null) {
					columns.add(pd.getName());
					values.add("?");
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO " + tableName + " (");
		sb.append(StringUtils.join(columns, ","));
		sb.append(") VALUE(");
		sb.append(StringUtils.join(values, ","));
		sb.append(")");
		return sb.toString();
	}

	private <T> Object[] getInsertValues(T t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(t);
		List<Object> values = new ArrayList<Object>();
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			if (!"class".equals(pd.getName())) {
				Object value = PropertyUtils.getProperty(t, pd.getName());
				Field field = t.getClass().getDeclaredField(pd.getName());
				if (!ModelUtils.isIgnore(field) && value != null) {
					values.add(value);
				}
			}
		}
		return values.toArray();
	}

	private String getInsertSql(String tableName, Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		if (map != null) {
			List<String> columns = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			Set<Entry<String, Object>> set = map.entrySet();
			for (Entry<String, Object> e : set) {
				columns.add(e.getKey());
				values.add("?");
			}

			sb.append("INSERT INTO " + tableName + " (");
			sb.append(StringUtils.join(columns, ","));
			sb.append(") VALUE(");
			sb.append(StringUtils.join(values, ","));
			sb.append(")");
		}
		return sb.toString();
	}
	
	private Object[] getInsertValues(Map<String, Object> map) {
		return map.values().toArray();
	}

}
