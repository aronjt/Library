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
      //  sql.getFullInfo();
      //  sql.getMemberInfo();
      //  sql.rentedBookData();
      //  sql.getDailyLog();
      //  sql.mostPopularBook();
      //  sql.avgTakeOutTime();
        sql.numberOfTakenOutBooks();
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
        System.out.println("Írja be a felhasználó azonosítót: ");
        Scanner sc=new Scanner(System.in);
        int memberID=sc.nextInt();
        PreparedStatement preparedStatement=connection.prepareStatement("SELECT Member.Name, Book.Title from Member join Log on Log.Member_idMember=Member.idMember join Stock on stock.idStock=Log.Stock_idStock join Book on Stock.Book_ISBN=Book.ISBN where ? =Member.idMember");
        preparedStatement.setInt(1,memberID);
        ResultSet resultSet=preparedStatement.executeQuery();
        while(resultSet.next()){
            String name = resultSet.getString("Member.Name");
            String bookTitle=resultSet.getString("Book.Title");
            System.out.println("Név: " + name+ " Köny: "+bookTitle);
        }

    }

    public void getDailyLog() throws SQLException {

        System.out.println("Add meg a keresett dátumot (éééé-hh-nn)");
        Scanner sc = new Scanner(System.in);
        String date = sc.next();
        PreparedStatement preparedStatement= connection.prepareStatement("SELECT * from log where Takeout = ?");
        preparedStatement.setString(1,date);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            int idLog = resultSet.getInt("idLog");
            int memberIDdMember=resultSet.getInt("Member_idMember");
            Date taketout= resultSet.getDate("Takeout");
            Date deadline= resultSet.getDate("Deadline");
            Date actualTakeBack= resultSet.getDate("ActualTakeback");
            int stockIDStock= resultSet.getInt("Stock_idStock");
            System.out.println("LogID: "+idLog+" Felszhasználó azonisító: "+memberIDdMember+" Kikölcsönzés dátuma: "+taketout+" Kiadva: "+deadline+"-ig"+" Visszahozatal dátuma: "+actualTakeBack+" Cikkszám: "+ stockIDStock);
        }
    }

    public void rentedBookData() throws SQLException {

        System.out.println("Írja be a könyv címét");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM library.book");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int ISBN = resultSet.getInt(1);
            String Title = resultSet.getString(2);
            String author = resultSet.getString(3);
            String publisher = resultSet.getString(4);
            int year = resultSet.getInt(5);

            System.out.println(ISBN + " " + Title + " " + author + " " + publisher + " " + year);

       }
        Scanner sc = new Scanner(System.in);
        String title = sc.next();
        preparedStatement = connection.prepareStatement("SELECT book.Title , member.Name, log.Takeout FROM library.log join stock on log.Stock_idStock = stock.idStock join member on log.Member_idMember = member.idMember join book on stock.Book_ISBN = book.ISBN where book.Title like ?");
        preparedStatement.setString(1,title);
        resultSet = preparedStatement.executeQuery();
        System.out.println(title + " című kötet kiadásának adatai: ");
        while (resultSet.next()){
            String bookTitle = resultSet.getString(1);
            String name = resultSet.getString(2);
            Date date = resultSet.getDate(3);
            System.out.println(bookTitle + " " + name + " " + date);
            System.out.println("-------------------------------------");
        }
    }

    public void mostPopularBook() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Title FROM Library.Book WHERE ISBN = (SELECT Stock.Book_ISBN FROM Stock JOIN Log ON Log.Stock_idStock = Stock.idStock GROUP BY Stock.Book_ISBN ORDER BY COUNT(*) DESC LIMIT 1)");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String bookTitle = resultSet.getString("Title");
            System.out.println("A legtöbbször kikölcsönzött könyv: " + bookTitle);
        }
    }

    public void avgTakeOutTime() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Title, (SUM(Log.ActualTakeback) - SUM(Log.Takeout)) / COUNT(*) FROM Library.Book JOIN Stock ON Stock.Book_ISBN = Book.ISBN JOIN Log ON Log.Stock_idStock = Stock.idStock WHERE Log.ActualTakeback IS NOT NULL GROUP BY Stock.Book_ISBN");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String bookTitle = resultSet.getString(1);
            double avgTime = Math.round(resultSet.getDouble(2) * 100.0) / 100.0;
            System.out.println(bookTitle + " átlagos kölcsönzési ideje: " + avgTime + " nap.");
        }
    }

    public void numberOfTakenOutBooks() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT Member.Name, Member.idMember, COUNT(Log.Takeout) FROM Library.Member JOIN Log ON Member.idMember = Log.Member_idMember GROUP BY Member.idMember");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            int id = resultSet.getInt(2);
            int num = resultSet.getInt(3);
            System.out.println("Azonosító: " + id + " Név: " + name + " Kölcsönzések száma: " + num);
        }
    }
}
