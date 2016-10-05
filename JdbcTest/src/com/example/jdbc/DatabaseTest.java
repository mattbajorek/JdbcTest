package com.example.jdbc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseTest {
	private static final String DB_PASSWORD = "dbPassword";
	private static final String DB_USER = "dbUser";
	private static final String DB_URL = "dbUrl";
	private static final String PROJECT_PROPERTIES = "project.properties";
	private static String dbUrl;
	private String dbUser;
	private String dbPassword;
	private Properties globalProps;
	
	public void writeProperties() throws IOException {
		Properties prop = new Properties();
		prop.setProperty(DB_URL, "localhost");
		prop.setProperty(DB_USER, "username");
		prop.setProperty(DB_PASSWORD, "password");

		try (OutputStream out = new FileOutputStream(PROJECT_PROPERTIES)) { // file name project.properties
			prop.store(out, "Database Properties File");
		}
	}
	
	public void readProperties() throws IOException {
		try (InputStream in = new FileInputStream(PROJECT_PROPERTIES)) {
			Properties globalProps = new Properties();
			globalProps.load(in);
		}
	}
	
	public void connectToDatabase() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/burger_db";
		String username = "root";
		String password = "";

		System.out.println("Connecting database...");

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
		    System.out.println("Database connected!");
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}

	public static void main(String[] args) {
		DatabaseTest app = new DatabaseTest();
		try {
			app.connectToDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
