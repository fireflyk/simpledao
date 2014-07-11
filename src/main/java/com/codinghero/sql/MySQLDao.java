package com.codinghero.sql;

public class MySQLDao extends PreparedDao {
	public MySQLDao(String jdbcUrl, String userId, String password) {
		super("com.mysql.jdbc.Driver", jdbcUrl, userId, password);
	}
}
