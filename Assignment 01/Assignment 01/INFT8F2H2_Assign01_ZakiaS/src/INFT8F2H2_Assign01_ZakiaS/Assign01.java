/***************************************************************************
 *Bismillahir Rahmaanir Raheem
 *Almadadh Ya Gause Radi Allahu Ta'alah Anh - Ameen
 *Student Number : 208501583
 *Name : Zakia Salod
 *Course : INFT8F2H2
 *Assignment : 01
 *Masters of Medical Science - Medical Informatics
 *Year : 2016
 ***************************************************************************/
package INFT8F2H2_Assign01_ZakiaS;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import javax.swing.JFileChooser;
import java.util.*;

public class Assign01 {
	
	public static void main (String [] args) throws FileNotFoundException, IOException{
		
		System.out.println("--------------------------------------------------");
	    System.out.println("Welcome to the Medical File Tagging Program - 2016");
	    System.out.println("-------------Programmer: Zakia Salod--------------\n");
	    
	    System.out.println("Please select a Medical Text File >>>");
		System.out.println("Please select a Medical Keywords Text File >>>");
	    JFileChooser fileChooser1 = new JFileChooser();
	    JFileChooser fileChooser2 = new JFileChooser();
	    Scanner input1 = null;
	    Scanner input2 = null;
	    
	   //>>>>>>>>>>>>>>>>>>>>>>>  Prompt User To Choose Medical Text File & Medical Keywords Text File >>>>>>>>>>>>>>>>>>>>>>>
	    if(fileChooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION && fileChooser2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
		    
			File sourceInputTxtFile01 = fileChooser1.getSelectedFile();
			File sourceInputTermsFile01 = fileChooser2.getSelectedFile();
			
			
			try {
				input1 = new Scanner(sourceInputTxtFile01);	    		 
			    input2 = new Scanner(sourceInputTermsFile01);
				
			    String strInput1 = "";
			    String strInput2 = "";
			    if (input1.hasNextLine()){
				    strInput1 = input1.nextLine();	
			    }
			    
			    if (input2.hasNextLine()){
				    strInput2 = input2.nextLine();		
			    }
			    
				String [] stringsTerms = strInput2.split(", ");
				String checkStringDouble1 = "";
				String checkStringDouble2 = "";		
				boolean printToTargetFile = false;
				
				if (!sourceInputTxtFile01.exists()){
						System.out.println("Source text file " + sourceInputTxtFile01 + " does not exist");
						System.exit(0);
				}//end if
				
				else if (!sourceInputTermsFile01.exists()){
						System.out.println("Source terms file " + sourceInputTermsFile01 + " does not exist");
						System.exit(0);
				}//end else if
				
				//>>>>>>>>>>>>>>>>>>>>>>>  Read-In Medical Keywords Text File & Tag Medical Text File >>>>>>>>>>>>>>>>>>>>>>>
				else {		
						for (int i=0; i<stringsTerms.length; i++){
							if (strInput1.toLowerCase().contains(stringsTerms[i].toLowerCase())){
								checkStringDouble1 =  " <"+stringsTerms[i]+">" + stringsTerms[i] + "</"+stringsTerms[i]+">>";
								checkStringDouble2  = " <<"+stringsTerms[i]+">" + stringsTerms[i] + "</"+stringsTerms[i]+">";
								strInput1 = strInput1.replace(stringsTerms[i], "<"+stringsTerms[i]+">" + stringsTerms[i] + "</"+stringsTerms[i]+">");
								if (strInput1.toLowerCase().contains(checkStringDouble1.toLowerCase())){
								   strInput1 = strInput1.replace(checkStringDouble1, " "+stringsTerms[i]+">");
								}
								if (strInput1.toLowerCase().contains(checkStringDouble2.toLowerCase())){
								   strInput1 = strInput1.replace(checkStringDouble2, " "+stringsTerms[i]+"<");
								}
							}//end if
						}//end for
						
						printToTargetFile = true;
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
				if (input2 !=null)
				   input2.close();
				
				 System.out.println("--------------------------------------------------");
				 System.out.println("End of Medical File Tagging Program - 2016");
				 System.out.println("--------------------------------------------------");
			}//end finally
	  }//end if
    }//end main()	
}//end class Assign01
