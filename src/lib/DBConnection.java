package lib;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DBConnection {
	private static Connection connection = null;
	
	/**
	 * Connect to the MySql DB with given credentials
	 * @return null | connection 
	 */
	public static Connection getDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			String dbUrl = "jdbc:mysql://localhost:3306/javacontacts";
			String username = "root";
			String password = "root1234";

			connection = DriverManager.getConnection(dbUrl, username, password);
		} catch (Exception e) {
			System.out.println(e);
		}

		return connection;
	}

}
