package com.codinghero.sql;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.codinghero.sql.annotation.ModelUtils;
import com.codinghero.sql.load.DBLoadable;
import com.codinghero.sql.util.ArrayUtils;
import com.codinghero.sql.util.BeanUtils;
import com.codinghero.sql.util.DateUtils;
import com.codinghero.sql.util.FileUtils;
import com.codinghero.sql.util.NumberUtils;

public class Dao {
	
	protected static Logger log = Logger.getLogger(Dao.class);
	
	private String driverClass;
	private String jdbcUrl;
	private String userId;
	private String password;

	protected String encoding;

	protected Connection conn;
	
	public Dao() {
		this.encoding = "UTF-8";
	}

	public Dao(String driverClass, String jdbcUrl, String userId,
			String password) {
		this.driverClass = driverClass;
		this.jdbcUrl = jdbcUrl;
		this.userId = userId;
		this.password = password;
		this.encoding = "UTF-8";
	}

	public Dao(String driverClass, String jdbcUrl, String userId,
			String password, String encoding) {
		this(driverClass, jdbcUrl, userId, password);
		this.encoding = encoding;
	}

	public void connect() throws SQLException {
		if (conn == null) {
			try {
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
			log.info("jdbcUrl=" + jdbcUrl);
			log.info("userId=" + userId);
			conn = DriverManager.getConnection(jdbcUrl, userId, password);
		}
	}

	public void startTransaction() throws SQLException {
		conn.setAutoCommit(false);
	}

	public void commit() throws SQLException {
		conn.commit();
		conn.setAutoCommit(true);
	}

	public void rollback() throws SQLException {
		conn.rollback();
		conn.setAutoCommit(true);
	}

	public int execute(String sql) throws SQLException {
		log.info(sql);
		Statement stmt = conn.createStatement();
		return stmt.executeUpdate(sql);
	}

	public int[] executeBatch(String sqls) throws SQLException {
		String[] sqlArr = sqls.split(";");
		return this.executeBatch(ArrayUtils.toList(sqlArr));
	}

	public int[] executeBatch(List<String> sqls) throws SQLException {
		Statement stmt = conn.createStatement();
		for (String sql : sqls) {
			if (!StringUtils.isEmpty(sql)) {
				log.info(sql);
				stmt.addBatch(sql);
			}
		}
		
		int[] result = stmt.executeBatch();
		this.closeStatement(stmt);
		return result;
	}

	public long selectCount(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			long result = rs.getLong(1);
			this.close(stmt, rs);
			return result;
		} else {
			this.close(stmt, rs);
			return 0L;
		}
	}
	
	public <T> long selectCount(Class<T> clazz) throws SQLException {
		String tableName = ModelUtils.getTableName(clazz);
		return selectCount("select count(*) from " + tableName);
	}

	public <T> T selectOne(String sql, Class<T> clazz) throws SQLException {
		log.info(sql);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		T bean = BeanUtils.newInstance(clazz);
		if(rs.next()) {
			selectOne(rs, bean);
		}
		this.close(stmt, rs);
		return bean;
	}

	public Map<String, Object> selectOne(String sql) throws SQLException {
		log.info(sql);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(rs.next()) {
			selectOne(rs, map);
		}
		this.close(stmt, rs);
		return map;
	}

	public <T> List<T> selectList(String sql, Class<T> clazz) throws SQLException {
		log.info(sql);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<T> resultList = new ArrayList<T>();
		while (rs.next()) {
			T bean = BeanUtils.newInstance(clazz);
			selectOne(rs, bean);
			resultList.add(bean);
		}
		this.close(stmt, rs);
		return resultList;
	}

	public List<Map<String, Object>> selectList(String sql) throws SQLException {
		log.info(sql);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		while (rs.next()) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			selectOne(rs, map);
			resultList.add(map);
		}
		this.close(stmt, rs);
		return resultList;
	}

	protected <T> void selectOne(ResultSet rs, T bean) throws SQLException {
		// get meta info
		ResultSetMetaData meta = rs.getMetaData();
		int length = meta.getColumnCount();
		for (int i = 1; i <= length; i++) {
			// get name & value
			String label = meta.getColumnLabel(i);
			Object value = rs.getObject(i);
			// set value
			if (PropertyUtils.isWriteable(bean, label)) {
				try {
					Class<?> propertyClazz = PropertyUtils.getPropertyType(bean, label);
					if (!propertyClazz.equals(value.getClass())) {
						BeanUtils.setProperty(bean, label, ConvertUtils.convert(value, propertyClazz));
					} else {
						BeanUtils.setProperty(bean, label, value);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					log.error(e);
				} catch (InvocationTargetException e) {
					e.printStackTrace();
					log.error(e);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					log.error(e);
				}

			}
		}
	}

	protected <T> void selectOne(ResultSet rs, Map<String, Object> map) throws SQLException {
		// get meta info
		ResultSetMetaData meta = rs.getMetaData();
		int length = meta.getColumnCount();
		for (int i = 1; i <= length; i++) {
			// get name & value
			String label = meta.getColumnLabel(i);
			Object value = rs.getObject(i);
			// set value
			map.put(label, value);
		}
	}
	
	public <T> int insertOne(T t) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		return execute(getInsertSql(t));
	}
	
	public int insertOne(String tableName, Map<String, Object> map) throws SQLException {
		return execute(getInsertSql(tableName, map));
	}
	
	public <T> int[] insertList(List<T> list) throws SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		if (list != null) {
			Statement stmt = conn.createStatement();
			for (T t : list) {
				String sql = getInsertSql(t);
				stmt.addBatch(sql);
			}
			int[] result = stmt.executeBatch();
			this.closeStatement(stmt);
			return result;
		}
		return null;
	}
	
	public int[] insertList(String tableName, List<Map<String, Object>> list) throws SQLException {
		Statement stmt = conn.createStatement();
		if (list != null) {
			for (Map<String, Object> map : list) {
				String sql = getInsertSql(tableName, map);
				stmt.addBatch(sql);
			}
			int[] result = stmt.executeBatch();
			this.closeStatement(stmt);
			return result;
		}
		return null;
	}
	
	private <T> String getInsertSql(T t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(t);
		final String tableName = ModelUtils.getTableName(t.getClass());
		StringBuilder sb = new StringBuilder();
		
		List<String> columns = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			Object value = PropertyUtils.getProperty(t, pd.getName());
			Field field = t.getClass().getDeclaredField(pd.getName());
			if (!ModelUtils.isIgnore(field) && value != null) {
				columns.add(pd.getName());
				if (pd.getPropertyType().equals(Date.class))
					values.add("'" + DateUtils.getDateStr((Date) value) + "'");
				else if (NumberUtils.isNumber(pd.getPropertyType()))
					values.add(value.toString());
				else
					values.add("'" + value + "'");
			}
		}
		sb.append("INSERT INTO " + tableName + " (");
		sb.append(StringUtils.join(columns, ","));
		sb.append(") VALUE(");
		sb.append(StringUtils.join(values, ","));
		sb.append(")");
		return sb.toString();
	}
	
	private String getInsertSql(String tableName, Map<String, Object> map) {
		StringBuilder sb = new StringBuilder();
		if (map != null) {
			List<String> columns = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			Set<Entry<String, Object>> set = map.entrySet();
			for (Entry<String, Object> e : set) {
				columns.add(e.getKey());
				Object value = e.getValue();
				if (value != null) {
					if (e.getValue().getClass().equals(Date.class))
						values.add("'" + DateUtils.getDateStr((Date) value) + "'");
					else if (NumberUtils.isNumber(value))
						values.add(value.toString());
					else
						values.add("'" + value + "'");
				}
			}

			sb.append("INSERT INTO " + tableName + " (");
			sb.append(StringUtils.join(columns, ","));
			sb.append(") VALUE(");
			sb.append(StringUtils.join(values, ","));
			sb.append(")");
		}
		return sb.toString();
	}

	public void importSQLFile(String sqlFilePath) throws SQLException {
		String content = FileUtils.getContent(sqlFilePath);
		content = removeComment(content);
		executeBatch(content);
	}

	public void exportDataFile(String sql, File file) throws SQLException {
		List<Map<String, Object>> resultList = selectList(sql);
		exportDataFile(resultList, file);
	}

	private String removeComment(String sql) {
		return sql.replaceAll("--.*" + FileUtils.LINE_SEPARATOR, "").replaceAll("#.*" + FileUtils.LINE_SEPARATOR, "");
	}

	/*
	 * public void genLoadInfile(List<? extends DBLoadable> dataList, String
	 * filePath) { genLoadInfile(dataList, filePath, "UTF-8"); }
	 */

	private void exportDataFile(List<Map<String, Object>> loadList, File file) {
		FileUtils.forceNewFile(file.getAbsolutePath());
		
		for (Map<String, Object> data : loadList) {
			FileUtils.appendFile(file.getAbsolutePath(), getDataLine(data), encoding);
		}
	}
	
	private String getDataLine(Map<String, Object> data) {
		if(!(data instanceof LinkedHashMap)){
			log.warn("The data is not LinkedHashMap, the column sequence may be wrong");
		}
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = data.entrySet();
		int index = 1;
		for(Entry<String, Object> e : entrySet) {
			sb.append(e.getValue());
			if (index < entrySet.size()) {
				sb.append(DBLoadable.FIELDS_TERMINATED);
			}
		}
		sb.append(DBLoadable.LINES_TERMINATED);
		return sb.toString();
	}

	public void closeConnection() {
		try {
			if (conn != null)
				conn.close();
			conn = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void close(Statement stmt, ResultSet rs) {
		this.closeResultSet(rs);
		this.closeStatement(stmt);
	}

	protected void closeStatement(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
			stmt = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	protected void closeResultSet(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean testConnection() {
		try {
			connect();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public Dao clone() {
		return new Dao(this.driverClass, this.jdbcUrl, this.userId, this.password);
	}

	public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
