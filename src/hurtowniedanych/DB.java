/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hurtowniedanych;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author daniel
 */
public class DB {
    private final int port = 5432;
    private final String host = "localhost";
    private final String database = "Ksiegarnia";
    private final String password = "szupek";
    private Connection c;

    public DB() {
        c = null;
    }
    
    public boolean connect() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://"+host+":"+port+"/"+database,"postgres", password);
            System.out.println("Połączona z bazą: "+database);
        } catch (Exception e) {
            System.out.println("Nie udało się połączyć z bazą danych! "+e.getMessage());
            return false;
        }
        return true;
    }
    
    public ResultSet select(String query) throws SQLException {
        return c.createStatement().executeQuery(query);
    }
    
     
    public void query(String query) throws SQLException {
        c.createStatement().execute(query);
    }
}
