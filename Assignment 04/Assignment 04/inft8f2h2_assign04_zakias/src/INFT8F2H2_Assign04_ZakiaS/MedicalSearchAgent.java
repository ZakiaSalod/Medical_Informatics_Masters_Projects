/***************************************************************************
 *Bismillahir Rahmaanir Raheem
 *Almadadh Ya Gause Radi Allahu Ta'alah Anh - Ameen
 *Student Number : 208501583
 *Name : Zakia Salod
 *Course : INFT8F2H2
 *Assignment : 04
 *Masters of Medical Science - Medical Informatics
 *Year : 2016
 ***************************************************************************/

package INFT8F2H2_Assign04_ZakiaS;

import java.sql.*;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class MedicalSearchAgent extends Agent{	
	//private AID[] searchAgents = {new AID("AllahYay", AID.ISLOCALNAME)};
    static final String database = "medassign03";
	
    //agent initializations here
	protected void setup()
    { 
		
		//printout a welcome message        
        System.out.println("-----------------------------------------------------------------------------------------------------------------");
	    System.out.println("Medical Distributed Search Engine Program - 2016");
	    System.out.println("Hello! Medical Search-agent "+getAID().getName()+" is ready.");
	    System.out.println("-----------------------------------------------------------------------------------------------------------------");
        
        
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("medical-searching");
        sd.setName("JADE-medical-searching");
        dfd.addServices(sd);
        
        try{
        	DFService.register(this, dfd);
        }//end try
        catch(FIPAException fe){
        	fe.printStackTrace();
        }//end catch
               
        
        ACLMessage msg = null;
        msg = blockingReceive();
        System.out.println("Medical Search-agent "+getAID().getName() + " just received message = " + msg.getContent());
        
	    	try{
	    		//search the database and display results
	    		searchDBAndDisplay(msg.getContent().substring(0, msg.getContent().indexOf("|")), msg.getContent().substring(msg.getContent().indexOf("|")+1));
	    	}//end try
		    catch(SQLException sqlException){
			     System.out.println("Error");
			     sqlException.printStackTrace();
		    }//end catch			
    }//end setup()
	
	
	
	//agent clean-up operations here
	protected void takeDown() {
		//de-register from the yellow pages
		try{
			DFService.deregister(this);
		}
		catch(FIPAException fe){
			fe.printStackTrace();
		}
		
		//print-out a dismissal message
		System.out.println("Medical Search-agent "+getAID().getName()+" terminating.");
	}//end takeDown()
	
	
	
	//searches the database and displays the output from the database for the specified medical term
	private static void searchDBAndDisplay(String medterm, String medcategory) throws SQLException{
		    int countTracker = 1;
		    try{		
		         DB connectToAssign03Db = new DB(database);
				 ResultSet resultSet = connectToAssign03Db.queryTbl("SELECT * FROM medontologylog WHERE medterm = '" + medterm + "'" + 
				                                                    " AND medcategory = '" + medcategory + "'"  + "ORDER BY timestamp DESC");
				 ResultSetMetaData metaData = resultSet.getMetaData();
				 int numberOfColumns = metaData.getColumnCount();
				 System.out.println();
				 
				 boolean foundUserMedicalTerm = false;
				 while(resultSet.next()){
					 if (countTracker ==1){
					   System.out.println("The database currently contains the following medical file information for medical term : " + medterm + " >>>");
					 }
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
					 
					 countTracker +=1;
				 }//end while
				 
				//Medical Term not found in Database
				 if(!foundUserMedicalTerm){
					 System.out.println("Sorry, there are currently no medical files on the database with medical term : " + medterm);
				 }//end if
				 
				 System.out.println();
		   }
		   catch(SQLException sqlException){
			     System.out.println("Error");
			     sqlException.printStackTrace();
		   }//end catch		 
	}//end searchDBAndDisplay
}//end MedicalSearchAgent class
