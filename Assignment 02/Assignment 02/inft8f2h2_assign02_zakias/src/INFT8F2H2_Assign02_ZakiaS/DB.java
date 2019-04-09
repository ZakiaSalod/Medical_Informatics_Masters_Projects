package INFT8F2H2_Assign02_ZakiaS;
/***************************************************************************
 *Bismillahir Rahmaanir Raheem
 *Almadadh Ya Gause Radi Allahu Ta'alah Anh - Ameen
 *Student Number : 208501583
 *Name : Zakia Salod
 *Course : INFT8F2H2
 *Assignment : 02
 *Masters of Medical Science - Medical Informatics
 *Year : 2016
 ***************************************************************************/

import java.io.*;
import java.sql.*;

public class DB {
	Connection conn;
	
	DB(String database){
		 final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		 final String DATABASE_URL = "jdbc:mysql://localhost:3307/"+database;
		 
		 //connect to mysql driver
		 try{
			 Class.forName(JDBC_DRIVER);
			 System.out.println("Driver Successfully Loaded");
		 }//end try
		 catch(ClassNotFoundException e){
			 System.out.println("Unable to connect");
			 System.exit(1);
		 }//end catch
		 
		 
		 try{
			 conn = DriverManager.getConnection(DATABASE_URL, "root", "usbw");
			 System.out.println("Connection to " + database + " successfully established" );
		 }//end try
		 catch(Exception e){
			 System.out.println(e.getMessage());
		 }//end catch
	}//end DB() Constructor
  
	
	
	//Method executes SQL queries, input as string argument
	ResultSet queryTbl(String sqlStmt) throws SQLException{
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sqlStmt); // select * from a table
		System.out.println("Successfully executed query on table.");
		return rs;
	}
	
	
	
	void updateTbl(String update) throws SQLException{
		Statement stmt = conn.createStatement();
		stmt.executeUpdate(update);
		System.out.println("Successfully updated table.");
		stmt.close();
	}
	
	
	
	void closeDB() throws SQLException{
		conn.close();
		System.out.println("Successfully closed table.");
	}
}//end DB class
