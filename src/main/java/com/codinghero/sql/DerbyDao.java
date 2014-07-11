package com.codinghero.sql;

public class DerbyDao extends PreparedDao {
	public DerbyDao(String jdbcUrl, String userId, String password) {
		super("org.apache.derby.jdbc.EmbeddedDriver", jdbcUrl, userId, password);
	}
}
