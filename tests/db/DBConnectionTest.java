package db;

import org.junit.Test;

import fr.uv1.bettingServices.db.DBConnection;

import java.sql.Connection;

import static org.junit.Assert.assertTrue;

/**
 */
public class DBConnectionTest {

    @Test
    public void TestNewConnection() throws Exception {
        Connection connection = DBConnection.newConnection();
        assertTrue(connection.isValid(5));
    }



}
