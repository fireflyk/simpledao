package com.codinghero.sql;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MySQLDaoTest {
	private MySQLDao dao;

	public MySQLDaoTest() throws SQLException {
		dao = new MySQLDao("jdbc:mysql://localhost:3306/test?useUnicode=true&amp;characterEncoding=UTF-8", "root", "sa");
		dao.connect();
	}

	@Before
	public void before() throws SQLException {
		dao.execute("drop table if exists easydaouser");
		dao.execute("create table easydaouser(id bigint not null AUTO_INCREMENT, name varchar(30) not null, primary key (id)) engine=innodb;");
	}

	@After
	public void after() throws SQLException {
		dao.execute("drop table easydaouser");
		dao.closeConnection();
	}

	@Test
	public void testInsertOne() throws SecurityException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		User user = new User();
		user.setId(1L);
		user.setName("codinghero");
		dao.insertOne(user);
		
		long count = dao.selectCount(User.class);
		assertEquals(1, count);
		
		User result = dao.selectOne("select * from easydaouser where id=1", User.class);
		assertEquals(user.getId(), result.getId());
		assertEquals(user.getName(), result.getName());
	}
	
	@Test
	public void testInsertList() throws SecurityException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		User user1 = new User();
		user1.setId(1L);
		user1.setName("codinghero");
		
		User user2 = new User();
		user2.setId(2L);
		user2.setName("codingmonkey");
		
		List<User> users = new ArrayList<User>();
		users.add(user1);
		users.add(user2);
		dao.insertList(users);
		
		long count = dao.selectCount(User.class);
		assertEquals(2, count);
		
		List<User> results = dao.selectList("select * from easydaouser order by id asc", User.class);
		assertEquals(user1.getId(), results.get(0).getId());
		assertEquals(user1.getName(), results.get(0).getName());
		assertEquals(user2.getId(), results.get(1).getId());
		assertEquals(user2.getName(), results.get(1).getName());
	}
	
	@Test
	public void testCommit() throws SecurityException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		
		Dao anotherDao = dao.clone();
		anotherDao.connect();
		
		dao.startTransaction();
		
		assertEquals(0, dao.selectCount(User.class));		
		User user1 = new User();
		user1.setId(1L);
		user1.setName("codinghero");
		dao.insertOne(user1);
		// the new inserted one is visible to this connection
		assertEquals(1, dao.selectCount(User.class));
		// the new inserted one is invisible to other connections
		assertEquals(0, anotherDao.selectCount(User.class));
		
		dao.commit();
		
		assertEquals(1, dao.selectCount("select count(*) from easydaouser"));
		
		anotherDao.closeConnection();
	}
	
	@Test
	public void testRollback() throws SecurityException, SQLException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, NoSuchFieldException {
		
		dao.startTransaction();
		
		assertEquals(0, dao.selectCount("select count(*) from easydaouser"));		
		User user1 = new User();
		user1.setId(1L);
		user1.setName("codinghero");
		dao.insertOne(user1);
		// the new inserted one is visible to this connection
		assertEquals(1, dao.selectCount("select count(*) from easydaouser"));
		
		dao.rollback();
		
		assertEquals(0, dao.selectCount("select count(*) from easydaouser"));
	}
}
