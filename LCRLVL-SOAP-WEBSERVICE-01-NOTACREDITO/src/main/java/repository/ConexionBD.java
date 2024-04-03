package repository;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class ConexionBD {

	public static Connection getDBConnection() throws IOException {

        Connection dbConnection = null;
        InputStream input = new FileInputStream("C:/app/ws_notacredito/config.properties");
    	Properties prop = new Properties();
    	prop.load(input);
    	
    	String url = prop.getProperty("db.url");
        String username = prop.getProperty("db.user");
        String password = prop.getProperty("db.password");	    
        String driverDB = prop.getProperty("db.driver");
       
        input.close();
        try {
        	 Class.forName(driverDB); // Driver name	
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        
        try {
            dbConnection = DriverManager.getConnection(url, username,password);
            return dbConnection;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dbConnection;
    }
}
