package objects;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {

	/**
	 * Establishes connection to MySQL server
	 * 
	 * @return returns Connection
	 */
	public static Connection getConnection() {

		// Connection variable con initialized to null
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");

			con = DriverManager.getConnection("jdbc:sqlite:journalsystem.db");
		} catch (Exception e) {
			System.out.println(e);
		}

		return con;
	}
}
