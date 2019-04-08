package objects;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * SQLConnection establishes the connection to the SQLite server for the program
 * 
 * @author - L01-Group20
 */
public class SQLConnection {

	/**
	 * Establishes connection to the SQLite server
	 * 
	 * @return con, The connection to the local database
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
