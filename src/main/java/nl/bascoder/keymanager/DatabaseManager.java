package nl.bascoder.keymanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * @author Bas van Marwijk
 * @since 9-11-14
 * @version 1.0 - creation
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private static final String DATABASE_CONNECTION_DRIVER = "org.sqlite.JDBC";
    private static final String CONNECTION_URL = "jdbc:sqlite:keys.db";

    private Connection connection;

    private DatabaseManager() throws ExceptionInInitializerError{
        try {
            Class.forName(DATABASE_CONNECTION_DRIVER);
            connection = DriverManager.getConnection(CONNECTION_URL);
        } catch (ClassNotFoundException e) {
            Logger.getGlobal().severe("Database connection driver not found: "
                    + DATABASE_CONNECTION_DRIVER);
            throw new ExceptionInInitializerError(e);
        } catch (SQLException e) {
            //TODO filter error for database not existing
            Logger.getGlobal().severe("Could not connect with database: "
                    + CONNECTION_URL);
        }
    }

    public static DatabaseManager getInstance() {
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        return connection;
    }

    public synchronized void closeConnection() {
        try {
            connection.close();
            instance = null;
        } catch (SQLException e) {
            Logger.getGlobal().severe("Could not close database");
        }
    }
}

