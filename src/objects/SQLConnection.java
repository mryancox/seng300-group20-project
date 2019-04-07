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

			// for connecting remotely (use this unless you are on stone's computer)
			// con=DriverManager.getConnection("jdbc:mysql://162.208.182.233:3306/mydb",
			// "journal", "journal!sys1234");

			con = DriverManager.getConnection("jdbc:sqlite:journalsystem.db");

			// for connecting locally (uncomment only on stone's computer)
			// con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb",
			// "journal", "journal");

		} catch (Exception e) {
			System.out.println(e);
		}

		return con;
	}
}
