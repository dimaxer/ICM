package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** This class represents the mysql connection */
public class mysqlConnection {

	// Instance variables **********************************************
	private static mysqlConnection singletonInstance = null;
	private Connection con = null;
	private SqlRequestHandler requestHandler = null;

	// Constructors ****************************************************

	/**
	 * This returns the request handler
	 * @return request handler
	 */
	public SqlRequestHandler handle() {
		return requestHandler;
	}
	
	/**
	 * This returns the connection
	 * @return connection connection
	 */
	public Connection getConnection() {
		return con;
	}

	/**
	 * Constructs an instance of the server singleton.
	 */
	private mysqlConnection() {
		singletonInstance = this;
		requestHandler = new SqlRequestHandler();
	}

	// Instance methods ************************************************

	/**
	 * Get the Singleton's Instance
	 * @return mysqlConnection Singleton Instance
	 */
	public static mysqlConnection getInstance() {
		if (singletonInstance == null)
			singletonInstance = new mysqlConnection();
		return singletonInstance;
	}
	
	// Class methods ***************************************************

	/**
	 * this method defines the jdbc driver so that we can connect to the database
	 */
	private void defineDriver() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
	}

	/**
	 * a method to connect to a db using custom values
	 * 
	 * @param hostname host name
	 * @param dbname db name
	 * @param username user name
	 * @param password pass word
	 * @return was successful
	 */
	public boolean connectToDB(String hostname, String dbname, String username, String password) {
		defineDriver();
		return establishDBConnection(hostname, dbname, username, password);
	}

	/**
	 * a method that connects the braude default db
	 * 
	 * @return was successful
	 */
	public boolean connectToBraudeDefault() {
		defineDriver();
		return establishDBConnection("localhost", "request_db", "root", "Aa123456");
	}

	/** this method establishes a connection to the database
	 * 
	 * @param hostname host name
	 * @param dbname db name
	 * @param username user name
	 * @param password pass word
	 * @return was successful
	 */
	private boolean establishDBConnection(String hostname, String dbname, String username, String password) {
		try {
			// if any of the fields is empty connect to this db
			if (hostname.isEmpty() || dbname.isEmpty() || password.isEmpty() || username.isEmpty())
				con = DriverManager.getConnection("jdbc:mysql://remotemysql.com/ZZUehgwcK6?serverTimezone=IST&autoReconnect=true",
						"ZZUehgwcK6", "hfwrGUeVv7");
			else
				// if the fields are filled connect to the db using those fields
				con = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + dbname + "?serverTimezone=IST&autoReconnect=true",
						username, password);
			System.out.println("SQL connection succeed! : " + con);
			
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			return false;
		}
		return true;
	}

	/**
	 * This method close's the db connection
	 * 
	 * @return was successful
	 */
	public boolean closeConnectionToDB() {
		try {
			if (con != null)
				con.close();
		} catch (SQLException ex) {
			System.out.println("an Error occured while trying to close the DB connection");
			return false;
		}
		return true;
	}
}
