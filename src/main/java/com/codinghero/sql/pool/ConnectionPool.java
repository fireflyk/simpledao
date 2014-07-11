package com.codinghero.sql.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class ConnectionPool {
	
	protected DataSource dataSource;
	
	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
	
	public void setLogWriter(PrintWriter out) throws SQLException {
		dataSource.setLogWriter(out);
	}
	
	public PrintWriter getLogWriter() throws SQLException {
		return dataSource.getLogWriter();
	}
	
	public void setLoginTimeout(int seconds) throws SQLException {
		dataSource.setLoginTimeout(seconds);
	}
	
	public int getLoginTimeout() throws SQLException {
		return dataSource.getLoginTimeout();
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
