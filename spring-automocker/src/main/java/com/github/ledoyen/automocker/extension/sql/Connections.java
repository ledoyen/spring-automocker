package com.github.ledoyen.automocker.extension.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Connections {

	public static List<String> tables(Connection c) {
		List<String> tables = new ArrayList<>();
		try (PreparedStatement ps = c.prepareStatement("SHOW TABLES"); ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				tables.add(rs.getString(1));
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Unable to list tables", e);
		}
		return tables;
	}

	public static void truncate(Connection c, String tableName) {
		try (PreparedStatement p = c.prepareStatement("TRUNCATE TABLE " + tableName)) {
			p.execute();
		} catch (SQLException e) {
			throw new IllegalStateException("Could not truncate Table [" + tableName + "]");
		}
	}
}
