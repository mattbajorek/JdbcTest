package com.example.jdbc;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class DatabaseTest {
	// Constants
	private static final String DB_PASSWORD = "dbPassword";
	private static final String DB_USER = "dbUser";
	private static final String DB_URL = "dbUrl";
	private static final String PROJECT_PROPERTIES = "project.properties";
	// Global properties
	private static Properties globalProps;
	
	public void writeProperties() throws IOException {
		Properties prop = new Properties();
		// Sets properties
		prop.setProperty(DB_URL, "jdbc:mysql://localhost:3306/burger_db");
		prop.setProperty(DB_USER, "root");
		prop.setProperty(DB_PASSWORD, "");
		// Saves properties to file
		try (OutputStream out = new FileOutputStream(PROJECT_PROPERTIES)) { // file name project.properties
			prop.store(out, "Database Properties File");
		}
	}
	
	public void readProperties() throws IOException {
		try (InputStream in = new FileInputStream(PROJECT_PROPERTIES)) {
			globalProps = new Properties();
			// Loads properties
			globalProps.load(in);
			// Prints out properties
			globalProps.list(System.out);
		}
	}
	
	public Connection connectToDatabase() throws SQLException {
		String url = globalProps.getProperty(DB_URL);
		String username = globalProps.getProperty(DB_USER);
		String password = globalProps.getProperty(DB_PASSWORD);

		System.out.println("Connecting database...");

		try {
			Connection conn = DriverManager.getConnection(url, username, password);
		    System.out.println("Database connected!");
			return conn;
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	}
	
	public void readAllData(Connection conn) throws SQLException {
		try (Statement query = conn.createStatement()) {
			String sql = "SELECT * FROM burgers";
			query.setFetchSize(100);
			
			try (ResultSet rs = query.executeQuery(sql)) {
				while (!rs.isClosed() && rs.next()) {
					int id = rs.getInt(1);
					String burger_name = rs.getString(2);
					boolean devoured = rs.getBoolean(3);
					Timestamp date = rs.getTimestamp(4);
					System.out.println(id + "; " + burger_name + "; " + devoured + "; " + date);
				}
			}
			
		}
	}
	
	public void addData(Connection conn) throws SQLException {
		String sql = "INSERT INTO burgers (burger_name) VALUES (?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, "Ostrich");
			ps.executeUpdate();
		}
	}

	public static void main(String[] args) {
		DatabaseTest app = new DatabaseTest();
		try {
			app.writeProperties();
			app.readProperties();
			Connection conn = app.connectToDatabase();
			app.addData(conn);
			app.readAllData(conn);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

}
