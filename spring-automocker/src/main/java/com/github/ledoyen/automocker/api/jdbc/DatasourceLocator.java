package com.github.ledoyen.automocker.api.jdbc;

import java.util.Map;

import javax.sql.DataSource;

import com.google.common.base.Joiner;

public class DatasourceLocator {

    private final Map<String, DataSource> datasourcesByName;

    DatasourceLocator(Map<String, DataSource> datasourcesByName) {
        this.datasourcesByName = datasourcesByName;
    }

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

    public DataSource getDataSource(String dsName) {
        if (!datasourcesByName.containsKey(dsName)) {
            throw new IllegalArgumentException(
                    "No datasource named [" + dsName + "] (available: " + Joiner.on(", ")
                            .join(datasourcesByName.keySet()) + ")");
        }
        return datasourcesByName.get(dsName);
    }

    public String getName() {
        if (datasourcesByName.size() == 1) {
            return datasourcesByName.keySet()
                    .iterator()
                    .next();
        } else {
            throw new IllegalArgumentException("Multiple datasources available: " + Joiner.on(", ")
                    .join(datasourcesByName.keySet()));
        }
    }
}
