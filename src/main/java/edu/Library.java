package edu;

import java.lang.reflect.Member;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Library {

    Connection connection = getConnection();

    public Library() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {

        Library sql = new Library();
      //  Connection connection = sql.getConnection();
        System.out.println("connection ready");
        sql.getFullInfo();
        sql.getMemberInfo();
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
    public void getMemberInfo() throws SQLException {
        System.out.println("Írja be a felhasználó azonisítót: ");
        Scanner sc=new Scanner(System.in);
        int memberID=sc.nextInt();
        PreparedStatement preparedStatement=connection.prepareStatement("SELECT Member.Name, Book.Title from Member join Log on Log.Member_idMember=Member.idMember join Stock on stock.idStock=Log.Stock_idStock join Book on Stock.Book_ISBN=Book.ISBN where ? =Member.idMember");
        preparedStatement.setInt(1,memberID);
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            String name = resultSet.getString("Memeber.Name");
            String bookTitle=resultSet.getString("Book.Title");
            System.out.println("Név: " + name+ " Köny: "+bookTitle);
        }

    }

    public void rentedBookData() throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT book.Title ,Takeout FROM library.log join stock on log.Stock_idStock = stock.idStock join book on stock.Book_ISBN = book.ISBN where book.Title like 'első'");


    }

}
