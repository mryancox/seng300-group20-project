package seng300project;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLConnection {
	/**
	 * Establishes connection to mysql server
	 * @return returns Connection
	 */
	public static Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			//for connecting remotely (use this unless you are on stone's computer)
			//con=DriverManager.getConnection("jdbc:mysql://162.208.182.233:3306/mydb", "journal", "journal!sys1234");

			//for connecting locally (only on stone's computer)
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "journal", "journal");
			
			//con=DriverManager.getConnection("jdbc:mysql://192.168.0.149:3306/mydb", "journal", "journal!sys1234");


		}catch(Exception e) {
			System.out.println(e);
		}
		return con;
	}
}
