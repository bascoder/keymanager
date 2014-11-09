package nl.bascoder.keymanager;

import org.sqlite.SQLiteJDBCLoader;

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

    private DatabaseManager() throws ExceptionInInitializerError {
        Connection tryConnection = null;
        try {
            System.setProperty("sqlite.purejava", "true");
            Class.forName(DATABASE_CONNECTION_DRIVER);
            tryConnection = DriverManager.getConnection(CONNECTION_URL);

            System.out.println(String.format("running in %s mode", SQLiteJDBCLoader.isNativeMode() ? "native" : "pure-java"));

        } catch (ClassNotFoundException e) {
            Logger.getGlobal().severe("Database connection driver not found: "
                    + DATABASE_CONNECTION_DRIVER);
            throw new ExceptionInInitializerError(e);
        } catch (SQLException e) {
            //TODO filter error for database not existing
            Logger.getGlobal().severe("Could not connect with database: "
                    + CONNECTION_URL);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                tryConnection.close();
            } catch (Exception e) {
                System.err.print("Could not close connection");
            }
        }
    }

    public static DatabaseManager getInstance() {
        if(instance == null){
            instance = new DatabaseManager();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(CONNECTION_URL);
        } catch (SQLException e) {
            //TODO filter error for database not existing
            Logger.getGlobal().severe("Could not connect with database: "
                    + CONNECTION_URL);
            throw e;
        }
    }


}

