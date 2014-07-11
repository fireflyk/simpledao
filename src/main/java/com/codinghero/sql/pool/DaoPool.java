package com.codinghero.sql.pool;

import java.sql.SQLException;

import com.codinghero.sql.Dao;

public class DaoPool extends ConnectionPool {
	public <T extends Dao> T getDao(Class<T> clazz)
			throws InstantiationException, IllegalAccessException, SQLException {
		T dao = clazz.newInstance();
		dao.setConnection(dataSource.getConnection());
		return dao;
	}
}
