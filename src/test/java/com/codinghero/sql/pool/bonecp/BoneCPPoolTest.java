package com.codinghero.sql.pool.bonecp;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.codinghero.sql.PreparedDao;
import com.codinghero.sql.pool.DaoPool;

public class BoneCPPoolTest {
	
	private PreparedDao dao;

	public BoneCPPoolTest() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		DaoPool pool = new BoneCPDaoPool(
				"com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost:3306/girleg?useUnicode=true&amp;characterEncoding=UTF-8", 
				"root", "sa");
		dao = pool.getDao(PreparedDao.class);
	}

	@Before
	public void before() throws SQLException {
		dao.execute("drop table if exists easydaouser");
		dao.execute("create table easydaouser(id bigint not null AUTO_INCREMENT, name varchar(30) not null, primary key (id)) engine=innodb;");
	}

	@After
	public void after() throws SQLException {
		dao.execute("drop table easydaouser");
	}
	
	@Test
	public void test() {
		
	}
}
