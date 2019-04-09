/***************************************************************************
 *Bismillahir Rahmaanir Raheem
 *Almadadh Ya Gause Radi Allahu Ta'alah Anh - Ameen
 *Student Number : 208501583
 *Name : Zakia Salod
 *Course : INFT8F2H2
 *Assignment : 03
 *Masters of Medical Science - Medical Informatics
 *Year : 2016
 ***************************************************************************/

package INFT8F2H2_Assign03_ZakiaS;
import java.io.*;
import java.net.URI;
import javax.swing.JFileChooser;
import org.apache.commons.logging.LogFactory;
import org.apache.jena.atlas.logging.Log;
import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.*;
import org.apache.jena.util.iterator.ExtendedIterator;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import javax.swing.JFileChooser;
import java.sql.*;
import java.util.*;

public class Assign03 {
	
	private static String strInput1 = "";
	private static String checkStringDouble1 = "";
	private static String checkStringDouble2 = "";	
    static final String database = "medassign03";
    static File getOntologyFile;
	public static void main (String [] args) throws IllegalAccessException,
                                                    InstantiationException,  ClassNotFoundException,
                                                    FileNotFoundException,   IOException, SQLException{

		System.out.println("Ya Allah, Please help me");
		LogCtl.setCmdLogging();		
		
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
	    System.out.println("Welcome to the Medical Semantic Search Engine Program - 2016");
	    System.out.println("-----------------------------------------------------------------------------------------------------------------");
	    
	    System.out.println("Please select a Medical Text File >>>");
		System.out.println("Please select an Ontology File >>>");
	    JFileChooser fileChooser1 = new JFileChooser();
	    JFileChooser fileChooser2 = new JFileChooser();
	    Scanner input1 = null;
	    
	   //>>>>>>>>>>>>>>>>>>>>>>>  Prompt User To Choose Medical Text File & an Ontology File >>>>>>>>>>>>>>>>>>>>>>>
	    if(fileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && fileChooser2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		    
			File sourceInputTxtFile01 = fileChooser1.getSelectedFile();
			getOntologyFile = fileChooser2.getSelectedFile();
			
			
			try {
				input1 = new Scanner(sourceInputTxtFile01);	    
				
			    if (input1.hasNextLine()){
				    strInput1 = input1.nextLine();	
			    }
			    
				OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);				
				inf.read(getOntologyFile.toString(), "");				
				String URI = "http://www.organism.com/ontologies/organism.owl#";				
				ExtendedIterator classes = inf.listClasses();			    

				boolean printToTargetFile = false;
				
				if (!sourceInputTxtFile01.exists()){
						System.out.println("Source text file " + sourceInputTxtFile01 + " does not exist");
						System.exit(0);
				}//end if
				
				else if (!getOntologyFile.exists()){
						System.out.println("Ontology file " + getOntologyFile + " does not exist");
						System.exit(0);
				}//end else if
				
				//>>>>>>>>>>>>>>>>>>>>>>>  Read-In Medical Keywords Text File & Tag Medical Text File >>>>>>>>>>>>>>>>>>>>>>>
				//>>>>>>>>>>>>>>>>>>>>>>>  Also insert into table as well too >>>>>>>>>>>>>>>>>>>>>>>
				else {		
					   try{			
						        DB connectToAssign03Db = new DB(database);
								createDbTable(connectToAssign03Db);
					            int code = 0;
								
								//iterate through the Ontology file and Tag the Medical input file
								while (classes.hasNext()) {
									  OntClass ontologyClass = (OntClass) classes.next();
									  String ontologyClassStr = ontologyClass.getLocalName().toString().trim();
									  
									  if (ontologyClass.hasSubClass()) {
										    if (strInput1.toLowerCase().contains(ontologyClassStr.toLowerCase())){
											      //tag medical input file
											      tagMedicalText(ontologyClassStr, ontologyClassStr);
											      //delete then insert latest information into database table		
											      code+=1;							
											      deletemedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, ontologyClassStr, ontologyClassStr);
											      insertmedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, ontologyClassStr, ontologyClassStr);
										    }//end if
										      OntClass cla = inf.getOntClass(URI + ontologyClassStr);
					                      for (Iterator i = cla.listSubClasses(); i.hasNext();) {
					                          OntClass c = (OntClass) i.next();	                        
					                          if (strInput1.toLowerCase().contains(c.getLocalName().toString().toLowerCase())){
						                          tagMedicalText(c.getLocalName().toString(), ontologyClassStr);
						                          //delete then insert latest information into database table			
						                          code+=1;							
						                          deletemedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, c.getLocalName().toString(), ontologyClassStr);
						                          insertmedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, c.getLocalName().toString(), ontologyClassStr);
					                          }//end if
					                       }//end for                   
									  }//end if
									  else if(!ontologyClass.hasSubClass() && !ontologyClass.hasSuperClass()){
										   if (strInput1.toLowerCase().contains(ontologyClassStr.toLowerCase())){
										      tagMedicalText(ontologyClassStr, ontologyClassStr);
										      //delete then insert latest information into database table	
										      code+=1;							
										      deletemedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, ontologyClassStr, ontologyClassStr);
										      insertmedontologylogentry(connectToAssign03Db, "medontologylog", sourceInputTxtFile01.getName(), code, ontologyClassStr, ontologyClassStr);
										   }//end if
									  }
								}//end while	
						
						        printToTargetFile = true;
					   }//end try
					   catch(Exception  e){
						   e.printStackTrace();
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
			}//end catch1
            catch (Exception e) {
                System.out.println(e.getMessage());
            }//end catch2
			finally{
				//close the medical input file
				if (input1 !=null)
				   input1.close();
			}//end finally
	  }//end if
	    
	     //handles searching of medical term/s entered by the user, and searches and displays files in the database with this medical term/s
	     userSearchTerm();
	     
	     System.out.println("Exitting program . . . . . . . ");
	     
	     System.out.println();
	     System.out.println("-----------------------------------------------------------------------------------------------------------------");
		 System.out.println("End of Medical Semantic Search Engine Program - 2016");
		 System.out.println("-----------------------------------------------------------------------------------------------------------------");  
	    
    }//end main()	
	
	/**pass-in the same variable (superclass string) for both parameters - for Superclass Ontology term tagging
	 * pass-in Subclass Ontology term for "ontologyTerm" and Superclass Ontology term for "ontologyTagTerm" for Subclass Ontology term tagging
	 */
	private static void tagMedicalText(String ontologyTerm, String ontologyTagTerm){
			strInput1 = strInput1.replace(ontologyTerm, "<"+ontologyTagTerm+">" + ontologyTerm + "</"+ontologyTagTerm+">");
	}
	
	/**creates database table medontologylog**/
	private static void createDbTable(DB connectToAssign03Db){
		   
		   try{
		       String createMedOntologyLog = "CREATE TABLE medontologylog(paper varchar(250) NOT NULL, "+
		                                        "code int(11) NOT NULL, medterm varchar(255) NOT NULL, medcategory varchar(255) NOT NULL, "+
		    		                            "program varchar(15) NOT NULL, userstamp varchar(15) NOT NULL, timestamp datetime NOT NULL, "+
		                                        "PRIMARY KEY(paper, code, medterm, medcategory))";

		       connectToAssign03Db.updateTbl(createMedOntologyLog);
		   }
		   catch(SQLException e){
			   System.out.println("Table already exists in the " + database + " database");
		   }
	}//end createDbTable()
	
	
	/**deletes records from the medontologylog database table, according to the specified Primary Key values**/
	private static void deletemedontologylogentry(DB connectToAssign03Db, String tableName, String paper, int code, String medterm, String medcategory){

		try{
			connectToAssign03Db.updateTbl("DELETE FROM " + tableName +" WHERE paper='" + paper +"' AND code=" + code + " AND medterm='" + medterm + "' AND medcategory ='" + medcategory+"'");			
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}//end deletemedontologylogentry()
	
	
	/**inserts records onto the medontologylog database table**/
	private static void insertmedontologylogentry(DB connectToAssign03Db, String tableName, String paper, int code, String medterm, String medcategory){

		try{
			String program   = "Assign3";
			String userstamp = "ZS";
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			connectToAssign03Db.updateTbl("INSERT INTO " + tableName +"(paper, code, medterm, medcategory, program, userstamp, timestamp) "+
			                              "VALUES('"+paper+"'," + code + ",'" + medterm + "','" + medcategory+"','" + program +"','" + userstamp + "','" + currentTime + "'"  +")");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}//end insertmedontologylogentry()
	
	
	/**prompts the user on whether or not he/she would like to search the database for a medical term
	 * to retrieve file information for this term/s from the database
	 */
	private static void userSearchTerm(){
		String promptUser = "";
		Scanner getpromptUser;
		System.out.print("\nWould you like to search for a medical term from the database? >>> "); 
	    getpromptUser = new Scanner(System.in);
	    promptUser = getpromptUser.nextLine();
	    
	    System.out.println();
	    //user wants to proceed to search the database
	    while (promptUser.equalsIgnoreCase("Y") || promptUser.equalsIgnoreCase("Yes") ){
	    	String searchTerm ="";
	    	System.out.println("Please enter a medical search term  >>> ");
		    Scanner getUserMedicalSearchTerm = new Scanner(System.in);
		    searchTerm = getUserMedicalSearchTerm.nextLine();
			System.out.println("The database currently contains the following medical file information for medical term : " + searchTerm + " >>>");
		    searchUserTermAndOntology(searchTerm);
		    
			System.out.print("\nWould you like to continue searching for medical terms from the database? >>> ");
		    getpromptUser = new Scanner(System.in);
		    promptUser = getpromptUser.nextLine();
	    }
	}//end userSearchTerm()
	
	
	/**search the Ontology, by first looking at the user's search term - (searchTerm passed-in as parameter)**/
	private static void searchUserTermAndOntology(String searchTerm){
		OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);				
		inf.read(getOntologyFile.toString(), "");				
		String URI = "http://www.organism.com/ontologies/organism.owl#";				
		ExtendedIterator classes = inf.listClasses();
		boolean foundUserMedicalTermInOnt = false;
		 
		//iterate through the Ontology file and look for the Medical term entered by the user
		while (classes.hasNext()) {
			foundUserMedicalTermInOnt = true;
			  OntClass ontologyClass = (OntClass) classes.next();
			  String ontologyClassStr = ontologyClass.getLocalName().toString().trim();
			  if (ontologyClass.hasSubClass()) {
					    if (searchTerm.toLowerCase().contains(ontologyClassStr.toLowerCase())){
					    	//search database using medterm = ontologyClassStr and medcategory = ontologyClassStr
					    	try{
					    	   searchDBAndDisplay(ontologyClassStr, ontologyClassStr);
					    	}//end try
						    catch(SQLException sqlException){
							     System.out.println("Error");
							     sqlException.printStackTrace();
						    }//end catch	
					    }//end if
				         OntClass cla = inf.getOntClass(URI + ontologyClassStr);
                  for (Iterator i = cla.listSubClasses(); i.hasNext();) {
                      OntClass c = (OntClass) i.next();	     
					  if (searchTerm.toLowerCase().contains(c.getLocalName().toString().toLowerCase())){
					    	//search database using medterm = c.getLocalName().toString() and medcategory = ontologyClassStr
						    try{
						       searchDBAndDisplay(c.getLocalName().toString(), ontologyClassStr);
						    }//end try
						    catch(SQLException sqlException){
							     System.out.println("Error");
							     sqlException.printStackTrace();
						    }//end catch	
					  }//end if
                  }//end for                   
			  }//end if
			  else if(!ontologyClass.hasSubClass() && !ontologyClass.hasSuperClass()){
				     if (searchTerm.toLowerCase().contains(ontologyClassStr.toLowerCase())){
				    	//search database using medterm = ontologyClassStr and medcategory = ontologyClassStr
				    	 try{
				    	   searchDBAndDisplay(ontologyClassStr, ontologyClassStr);
				    	 }//end try
					     catch(SQLException sqlException){
						     System.out.println("Error");
						     sqlException.printStackTrace();
					     }//end catch	
				     }//end if
		  }
		}//end while
		
		 //Medical Term not found in Ontology
		 if(!foundUserMedicalTermInOnt){
			 System.out.println("Sorry, there are currently no medical files on the system with medical term : " + searchTerm);
		 }
	}//end searchUserTermAndOntology
	
	
	/**searches the database and displays the output from the database for the specified medical term, after consulting the Ontology**/
	private static void searchDBAndDisplay(String medterm, String medcategory) throws SQLException{
		   try{		
		         DB connectToAssign03Db = new DB(database);
				 ResultSet resultSet = connectToAssign03Db.queryTbl("SELECT * FROM medontologylog WHERE medterm = '" + medterm + "'" + 
				                                                    " AND medcategory = '" + medcategory + "'"  + "ORDER BY timestamp DESC");
				 ResultSetMetaData metaData = resultSet.getMetaData();
				 int numberOfColumns = metaData.getColumnCount();
				 System.out.println();
				 
				 boolean foundUserMedicalTerm = false;
				 while(resultSet.next()){
					 foundUserMedicalTerm = true;
					 System.out.println("-----------------------------------------------------------------------------------------------------------------");
					 System.out.println("Paper : " + resultSet.getString("paper"));
					 System.out.println("Code : " + resultSet.getString("code"));
					 System.out.println("Medical Term : " + resultSet.getString("medterm"));
					 System.out.println("Medical Category : " + resultSet.getString("medcategory"));
					 System.out.println("Program : " + resultSet.getString("program"));
					 System.out.println("Userstamp : " + resultSet.getString("userstamp"));
					 System.out.println("Timestamp : " + resultSet.getString("timestamp"));
					 System.out.println("-----------------------------------------------------------------------------------------------------------------");
					 System.out.println();
				 }
				 
				//Medical Term not found in Database
				 if(!foundUserMedicalTerm){
					 System.out.println("Sorry, there are currently no medical files on the database with medical term : " + medterm);
				 }
				 
				 System.out.println();
		   }
		   catch(SQLException sqlException){
			     System.out.println("Error");
			     sqlException.printStackTrace();
		   }		 
	}//end searchDBAndDisplay
}//end class Assign03
