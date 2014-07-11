package com.codinghero.sql.pool.bonecp;

import java.sql.SQLException;

import com.codinghero.sql.pool.DaoPool;
import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCPDaoPool extends DaoPool {
	
	public BoneCPDaoPool(
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
