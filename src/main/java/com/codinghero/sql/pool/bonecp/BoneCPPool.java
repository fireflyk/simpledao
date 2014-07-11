package com.codinghero.sql.pool.bonecp;

import java.sql.SQLException;

import com.codinghero.sql.pool.ConnectionPool;
import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPPool extends ConnectionPool {
	
	// private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	
	public BoneCPPool(
			String driverClass, String jdbcUrl, String username, String password) 
			throws SQLException, ClassNotFoundException {
		Class.forName(driverClass); 	// load the DB driver
	 	BoneCPDataSource dataSource = new BoneCPDataSource();
	 	dataSource.setJdbcUrl(jdbcUrl);
	 	dataSource.setUsername(username);
	 	dataSource.setPassword(password);
		this.dataSource = dataSource;
	}
}
