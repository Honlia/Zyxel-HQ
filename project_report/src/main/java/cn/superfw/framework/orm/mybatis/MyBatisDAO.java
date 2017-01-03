package cn.superfw.framework.orm.mybatis;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

public class MyBatisDAO extends SqlSessionDaoSupport implements SqlSession {

    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

	@Override
	public <T> T selectOne(String statement) {
		T result = getSqlSession().selectOne(statement);
		return result;
	}

	@Override
	public <T> T selectOne(String statement, Object parameter) {
		T result = getSqlSession().selectOne(statement, parameter);
		return result;
	}

	@Override
	public <E> List<E> selectList(String statement) {
		List<E> result = getSqlSession().selectList(statement);
		return result;
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter) {
		List<E> result = getSqlSession().selectList(statement, parameter);
		return result;
	}

	@Override
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		List<E> result = getSqlSession().selectList(statement, parameter, rowBounds);
		return result;
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		Map<K, V> result = getSqlSession().selectMap(statement, mapKey);
		return result;
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		Map<K, V> result = getSqlSession().selectMap(statement, parameter, mapKey);
		return result;
	}

	@Override
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		Map<K, V> result = getSqlSession().selectMap(statement, parameter, mapKey, rowBounds);
		return result;
	}

	@Override
	public void select(String statement, Object parameter, @SuppressWarnings("rawtypes") ResultHandler handler) {
		getSqlSession().select(statement, parameter, handler);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void select(String statement, ResultHandler handler) {
		getSqlSession().select(statement, handler);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		getSqlSession().select(statement, parameter, rowBounds, handler);

	}

	@Override
	public int insert(String statement) {
		int count = getSqlSession().insert(statement);
		return count;
	}

	@Override
	public int insert(String statement, Object parameter) {
		int count = getSqlSession().insert(statement, parameter);
		return count;
	}

	@Override
	public int update(String statement) {
		int count = getSqlSession().update(statement);
		return count;
	}

	@Override
	public int update(String statement, Object parameter) {
		int count = getSqlSession().update(statement, parameter);
		return count;
	}

	@Override
	public int delete(String statement) {
		int count = getSqlSession().delete(statement);
		return count;
	}

	@Override
	public int delete(String statement, Object parameter) {
		int count = getSqlSession().delete(statement, parameter);
		return count;
	}

	@Override
	public void commit() {
		getSqlSession().commit();

	}

	@Override
	public void commit(boolean force) {
		getSqlSession().commit(force);

	}

	@Override
	public void rollback() {
		getSqlSession().rollback();

	}

	@Override
	public void rollback(boolean force) {
		getSqlSession().rollback(force);

	}

	@Override
	public List<BatchResult> flushStatements() {
		List<BatchResult> batchResult = getSqlSession().flushStatements();
		return batchResult;
	}

	@Override
	public void close() {
		getSqlSession().close();
	}

	@Override
	public void clearCache() {
		getSqlSession().clearCache();

	}

	@Override
	public Configuration getConfiguration() {
		Configuration configuration = getSqlSession().getConfiguration();
		return configuration;
	}

	@Override
	public <T> T getMapper(Class<T> type) {
		T mapper = getSqlSession().getMapper(type);
		return mapper;
	}

	@Override
	public Connection getConnection() {
		Connection connection = getSqlSession().getConnection();
		return connection;
	}

}
