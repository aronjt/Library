package edu;

import java.sql.*;
import java.util.Properties;

public class Library {

    Connection connection = getConnection();

    public Library() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {

        Library sql = new Library();
      //  Connection connection = sql.getConnection();
        System.out.println("connection ready");
        sql.getFullInfo();
    }

    public void getFullInfo() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Member.name, Member.idMember, Book.Title, Book.ISBN FROM Member JOIN Log ON Member.idMember = Log.Member_idMember JOIN Stock ON Log.Stock_idStock = idStock JOIN Book ON Book_ISBN = ISBN WHERE Log.ActualTakeBack IS NULL;");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("Member.name");
            int memberId = resultSet.getInt("Member.idMember");
            String booktitle = resultSet.getString("Book.Title");
            System.out.println("Név " + name + " azonosító " + memberId + " könyv: " + booktitle);
        }
    }

    public Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";   //database specific url.
        Properties properties = new Properties( );
        properties.put( "user", "root" );
        properties.put( "password", "123456789" );

        return DriverManager.getConnection(url, properties);
    }

}
