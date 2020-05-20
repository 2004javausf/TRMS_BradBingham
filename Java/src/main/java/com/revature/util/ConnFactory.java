package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnFactory {
	private static ConnFactory cf = new ConnFactory();
	private static final String url = "jdbc:oracle:thin:@java2004usf.c3ze8kvqgwxn.us-east-2.rds.amazonaws.com:1521:ORCL";
	private static final String user = "trms";
	private static final String password = "p4ssw0rd";

	private ConnFactory() {
		super();
	}

	public static synchronized ConnFactory getInstance() {
		if (cf == null)
			cf = new ConnFactory();
		return cf;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}
}
