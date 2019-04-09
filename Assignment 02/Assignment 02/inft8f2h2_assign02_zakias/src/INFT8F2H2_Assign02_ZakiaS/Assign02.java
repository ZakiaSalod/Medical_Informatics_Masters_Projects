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
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;

import javax.swing.JFileChooser;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class Assign02 {
    static final String database = "medassign02";
	
	public static void main (String [] args) throws FileNotFoundException, IOException, SQLException{
		
		System.out.println("-------------------------------------------------------------");
	    System.out.println("Welcome to the Medical File & Database Tagging Program - 2016");
	    System.out.println("-------------------Programmer: Zakia Salod-------------------\n");
	    
	    System.out.println("Please select a Medical Text File >>>");
	    JFileChooser fileChooser1 = new JFileChooser();
	    Scanner input1 = null;
	    
	   //>>>>>>>>>>>>>>>>>>>>>>>  Prompt User To Choose Medical Text File >>>>>>>>>>>>>>>>>>>>>>>
	    if(fileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		    
			File sourceInputTxtFile01 = fileChooser1.getSelectedFile();

			try {
				input1 = new Scanner(sourceInputTxtFile01);	    		 
				
			    String strInput1 = "";
			    String strInput2 = "";
			    if (input1.hasNextLine()){
				    strInput1 = input1.nextLine();	
			    }
			    

				String checkStringDouble1 = "";
				String checkStringDouble2 = "";		
				boolean printToTargetFile = false;
				
				if (!sourceInputTxtFile01.exists()){
						System.out.println("Source text file " + sourceInputTxtFile01 + " does not exist");
						System.exit(0);
				}//end if
				
				
				//>>>>>>>>>>>>>>>>>>>>>>>  Read-In Medical Keywords Text from Database & Tag Medical Text File >>>>>>>>>>>>>>>>>>>>>>>
				//>>>>>>>>>>>>>>>>>>>>>>>  Also insert into table as well too >>>>>>>>>>>>>>>>>>>>>>>
				else {	
					 try{
						 //create output database table here
						 //createDbTable();
						 DB connectToAssign02Db = new DB(database);
						 //create output database table here
						 createDbTable(connectToAssign02Db);
						 ResultSet resultSet = connectToAssign02Db.queryTbl("SELECT * FROM medkeyword");
						 ResultSetMetaData metaData = resultSet.getMetaData();
						 int numberOfColumns = metaData.getColumnCount();
						 //loop through database table medkeyword, and tag text, and insert into database table
						 while(resultSet.next()){
									if (strInput1.toLowerCase().contains(resultSet.getString("term").toLowerCase())){
										checkStringDouble1 =  " <"+resultSet.getString("code")+">" + resultSet.getString("code") + "</"+resultSet.getString("code")+">>";
										checkStringDouble2  = " <<"+resultSet.getString("code")+">" + resultSet.getString("code") + "</"+resultSet.getString("code")+">";
										strInput1 = strInput1.replace(resultSet.getString("term"), "<"+resultSet.getString("code")+">" + resultSet.getString("term") + "</"+resultSet.getString("code")+">");
										
										//insert into database output table
										int getNumKeyOccurrences = 0;
										getNumKeyOccurrences = getNumStrOccurrences(strInput1, resultSet.getString("term"));
										
										deletemedpaperkeyentry(connectToAssign02Db, "medpaperkeycount", sourceInputTxtFile01.getName(), Integer.parseInt(resultSet.getString("code")), resultSet.getString("term"), getNumKeyOccurrences);
										insertmedpaperkeycount(connectToAssign02Db, "medpaperkeycount", sourceInputTxtFile01.getName(), Integer.parseInt(resultSet.getString("code")), resultSet.getString("term"), getNumKeyOccurrences);
									}//end if
						 }//end while
						 
							printToTargetFile = true;
					 }//end try
					 catch(SQLException e){
						 System.out.println("");
					 }//end catch
					
				}//end else
		
				//>>>>>>>>>>>>>>>>>>>>>>>  Create Medical Output Directory If Does Not Exist >>>>>>>>>>>>>>>>>>>>>>> 
				File medicalOutputDir = new File("medicaloutputfiles");
				if (!medicalOutputDir.exists()){
					System.out.println("\nCreating directory: "+ '"' + "medicaloutputfiles" + '"');
					medicalOutputDir.mkdir();
					System.out.println("Successfully created " + '"' + "medicaloutputfiles" + '"' + " directory");
				}
				else{
					System.out.println("Output file/s will be written to directory: "+ '"' + "medicaloutputfiles" + '"');
				}
				
				//>>>>>>>>>>>>>>>>>>>>>>>  Write to File >>>>>>>>>>>>>>>>>>>>>>> 
				if(printToTargetFile == true && medicalOutputDir.exists() && medicalOutputDir.isDirectory()){
					String outputFileName = "";
					System.out.println("\nPlease enter output file name >>> "); 
				    Scanner getOutputFileName = new Scanner(System.in);
				    outputFileName = getOutputFileName.nextLine();
					File targetOutputTxtFile;
				    boolean uniqueOutputFileName = true;
				    
					targetOutputTxtFile = new File(medicalOutputDir+"/"+outputFileName.trim()+".txt");
				
				     while (targetOutputTxtFile.exists()){
				    	    uniqueOutputFileName = false;
				    	    System.out.println("Target text file: " + targetOutputTxtFile + " already exists");
							System.out.println("Please enter a different output file name >>> "); 
						    outputFileName = getOutputFileName.nextLine();
							targetOutputTxtFile = new File(medicalOutputDir+"/"+outputFileName.trim()+".txt");
						    
						    if(!targetOutputTxtFile.exists()){
						    	uniqueOutputFileName = true;
						    }//end if
					 }//end while
				     
				     if (uniqueOutputFileName == true) {
				    	 PrintWriter output = null;
				    	 
				    	 try{
				    		 output = new PrintWriter(targetOutputTxtFile);
					    	 output.println(strInput1);					    	 
					    	 System.out.println("\nSuccessfully wrote to file: " + targetOutputTxtFile);
					    	 System.out.println("Full path to written file is: " + targetOutputTxtFile);
					    	 System.out.println();
				    	 }//end try
						 catch(FileNotFoundException e){
							 e.printStackTrace();
						 }//end catch
				    	 finally{
				    		    //close the file
								if (output !=null)
									output.close(); 
				    	 }//end finally					 
				     }//end if		
				}//end if
			}//end try
			catch(FileNotFoundException e){
				e.printStackTrace();
			}//end catch
			finally{
				//close the files
				if (input1 !=null)
				   input1.close();
				
				 System.out.println("-------------------------------------------------------------");
				 System.out.println("End of Medical File & Database Tagging Program - 2016");
					System.out.println("-------------------------------------------------------------");
			}//end finally
	  }//end if
    }//end main()	
	
	
	private static void createDbTable(DB connectToAssign02Db){
		   
		   try{
		       String createMedPaperKeyCount = "CREATE TABLE medpaperkeycount(paper varchar(250) NOT NULL, "+
		                                        "code int(11) NOT NULL, term varchar(255) NOT NULL, no_occurrences int(11) NULL NULL, PRIMARY KEY(paper, code, term))";

		       connectToAssign02Db.updateTbl(createMedPaperKeyCount);
		   }
		   catch(SQLException e){
			   System.out.println("Table already exists in the " + database + " database");
		   }
	}//end createDbTable()
	
	private static int getNumStrOccurrences(String fileStr, String findStr){
		   int counter = 0;
		   
		   Pattern p = Pattern.compile(findStr);
		   Matcher m = p.matcher(fileStr);
		   
		   while(m.find()){
			   counter++;
		   }
		   return counter;
	}//end getNumStrOccurrences()
	
	private static void deletemedpaperkeyentry(DB connectToAssign02Db, String tableName, String paper, int code, String term, int no_occurrences){

		try{
			connectToAssign02Db.updateTbl("DELETE FROM " + tableName +" WHERE paper='" + paper +"' AND code=" + code + " AND term='" + term + "' AND no_occurrences =" + no_occurrences);			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}//end deletemedpaperkeyentry()
	
	private static void insertmedpaperkeycount(DB connectToAssign02Db, String tableName, String paper, int code, String term, int no_occurrences){

		try{
			connectToAssign02Db.updateTbl("INSERT INTO " + tableName +"(paper, code, term, no_occurrences) VALUES('"+paper+"'," + code + ",'" + term + "'," + no_occurrences+")");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}//end insertmedpaperkeycount()
}//end class Assign02
