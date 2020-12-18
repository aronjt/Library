package edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class main {
    public static void main(String[] args) throws SQLException {

        main sql = new main();
        Connection connection = sql.getConnection();
        System.out.println("connection ready");

    }

    public Connection getConnection() throws SQLException {
        String url      = "jdbc:mysql://localhost:3306/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";   //database specific url.

        Properties properties = new Properties( );
        properties.put( "user", "root" );
        properties.put( "password", "123456789" );

        Connection connection =
                DriverManager.getConnection(url, properties);
        return connection;
    }

}
