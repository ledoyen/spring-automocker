package com.github.ledoyen.automocker.internal.sql;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.github.ledoyen.automocker.Resettable;
import com.github.ledoyen.automocker.extension.sql.Connections;
import com.github.ledoyen.automocker.extension.sql.DataSources;

public class MockDataSourceFactory
		implements FactoryBean<DataSource>, BeanNameAware, InitializingBean, Resettable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MockDataSourceFactory.class);

	private String beanName;
	private JdbcDataSource instance;

	@Override
	public DataSource getObject() throws Exception {
		return instance;
	}

	@Override
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		instance = new JdbcDataSource();
		instance.setUrl("jdbc:h2:mem:" + beanName + ";DB_CLOSE_DELAY=-1");
		LOGGER.debug("Mocking up " + DataSource.class.getSimpleName() + " of name " + beanName);
	}

	@Override
	public void reset() {
		try {
			DataSources.doInConnection(instance, c -> Connections.tables(c)
					.forEach(tableName -> Connections.truncate(c, tableName)));
		} catch (RuntimeException e) {
			throw new IllegalStateException("Could not reset DB[" + beanName + "]", e);
		}
	}
}
