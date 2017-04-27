package fr.uv1.bettingServices.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * This class is used to do the connection to the database
 */
public class DBConnection {

    // Default database parameters: work's only in the local network of telecom bretagne
	private final static String DEFAULT_HOST = "oracle-tp.svc.enst-bretagne.fr";
	private final static String DEFAULT_NUMPORT = "1521";
    private final static String DEFAULT_NAME = "enseig";
    private final static String DEFAULT_USER = "rmaliki";
    private final static String DEFAULT_PASS = "ORA5816";


	//-----------------------------------------------------------------------------
    //REGISTER THE ORACLE JDBC DRIVER
    static
	{
		try
		{
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Connection to the database.
	 *
	 * @return an instance of the Connection class.
	 * @throws SQLException
	 */
	public static Connection newConnection() throws SQLException
	{

		return DriverManager.getConnection("jdbc:oracle:thin:@"+DEFAULT_HOST+":"+DEFAULT_NUMPORT+":"+DEFAULT_NAME,DEFAULT_USER,DEFAULT_PASS);

	}

}
