package utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryContext {
    private static final Logger logger = Logger.getLogger(LibraryContext.class.getName());
    protected Connection conn = null;

    public LibraryContext() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String dbURL = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=LibraryPRJ;"
                    + "user=sa;"
                    + "password=123456;"
                    + "encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(dbURL);
            if (conn != null) {
                try {
                    DatabaseMetaData dm = conn.getMetaData();
                    System.out.println("Driver name: " + dm.getDriverName());
                    System.out.println("Driver version: " + dm.getDriverVersion());
                    System.out.println("Product name: "
                            + dm.getDatabaseProductName());
                    System.out.println("Product version: "
                            + dm.getDatabaseProductVersion());
                } catch (SQLException e) {
                    // import org.slf4j.Logger
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(LibraryContext.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
}
