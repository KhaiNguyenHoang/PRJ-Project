package model.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryContext {
    protected Connection conn = null;

    public LibraryContext() {
        try {
            String dbURL = "jdbc:sqlserver://localhost:1433;"
                    + "databaseName=LibraryPRJ;"
                    + "user=sa;"
                    + "password=123456;"
                    + "encrypt=true;trustServerCertificate=true;";
            conn = DriverManager.getConnection(dbURL);
            System.out.println("Connected to database.");
        } catch (SQLException ex) {
            System.out.println("Error connecting to database.");
            Logger.getLogger(LibraryContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
