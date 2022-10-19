package ticket.persistance.jdbc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class JdbcUtils {

    private Properties jdbcProps;

    public JdbcUtils(Properties props){
        jdbcProps=props;
    }

    private  Connection instance=null;

    private Connection getNewConnection(){
        String driver = jdbcProps.getProperty("ticket.jdbc.driver");
        String url = jdbcProps.getProperty("ticket.jdbc.url");
        String user = jdbcProps.getProperty("ticket.jdbc.user");
        String pass = jdbcProps.getProperty("ticket.jdbc.pass");

        Connection con=null;
//        try {
//
//            if (user!=null && pass!=null)
//                con= DriverManager.getConnection(url,user,pass);
//            else
//                con=DriverManager.getConnection(url);
//        } catch (SQLException e) {
//            System.out.println("Error getting connection "+e);
//        }
        try {
            Class.forName(driver);
            con= DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            System.out.println("Error getting connection " + e);
        }

        return con;
    }

    public Connection getConnection(){
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            System.out.println("Error DB "+e);
        }
        return instance;
    }
}
