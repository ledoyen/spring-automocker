package com.github.ledoyen.automocker.extension.sql;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Joiner;

public class DatasourceLocator implements InitializingBean {

	@Autowired
	private Map<String, DataSource> datasourcesByName;

	public DataSource getDataSource() {
		if (datasourcesByName.size() == 1) {
			return datasourcesByName.values()
					.iterator()
					.next();
		} else {
			throw new IllegalArgumentException("Multiple datasources available: " + Joiner.on(", ")
					.join(datasourcesByName.keySet()));
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (datasourcesByName.size() == 1) {

		}
	}

}
