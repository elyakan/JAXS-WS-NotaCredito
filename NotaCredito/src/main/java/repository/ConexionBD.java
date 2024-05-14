package repository;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConexionBD {

    public static String msgError;

    public static Connection getDBConnection()  throws IOException, NamingException {
        
        try {
        	
        	Properties props = new Properties();
        	props.setProperty(Context.INITIAL_CONTEXT_FACTORY,"weblogic.jndi.WLInitialContextFactory");
        	props.setProperty(Context.PROVIDER_URL,"tcp://localhost:7001");
        	InitialContext ctx = new InitialContext(props);
        	
        	Connection conn = null;
        	//InitialContext ctx = new InitialContext();
        	//Context ctx = (Context) new InitialContext().lookup("java:comp/env");
        	
        	DataSource src = (DataSource)ctx.lookup("jndi/JNDICostaRica");
        	conn = src.getConnection();
            return conn;
        } catch (SQLException ex) {
        	msgError="SQLException: " + ex.getMessage();
        	System.out.println("SQLException: " + ex.getMessage());
        	return null;
        }
        
    }
     
}
