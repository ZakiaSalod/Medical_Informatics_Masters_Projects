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

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.core.AID.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour; 
import jade.core.behaviours.TickerBehaviour;
import jade.core.*;
import jade.lang.acl.ACLMessage;
import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;
import javax.swing.JFileChooser;
import org.apache.jena.atlas.logging.LogCtl;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;

public class MedicalQueryAgent extends Agent {	
    static final String database = "medassign03";
    static File getOntologyFile;    
	//the medical search term to be searched
	private String searchTerm;	
	//the list of dynamic search agents
	private AID[] searchAgents;
	private MessageTemplate mt;
    
	//agent initializations here
	protected void setup()
    { 
		
		//printout a welcome message
		System.out.println("-----------------------------------------------------------------------------------------------------------------");
	    System.out.println("Medical Distributed Search Engine Program - 2016");
	    System.out.println("Hello! Medical Query-agent "+getAID().getName()+" is ready.");
	    System.out.println("-----------------------------------------------------------------------------------------------------------------");
        
		//System.out.println("Ya Allah, Please help me");
		LogCtl.setCmdLogging();		
		
		System.out.println("Please select an Ontology File >>>");
		JFileChooser fileChooser = new JFileChooser();		
		
		//get the medical search term to be searched as a start-up argument
		Object[] args = getArguments();
		
		if (args != null && args.length > 0) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				getOntologyFile = fileChooser.getSelectedFile();
				searchTerm = (String) args[0];
				System.out.println("Medical search term is "+searchTerm);
				
				//add a TickerBehaviour that schedules a request to medical search agents every 5 seconds
				addBehaviour(new TickerBehaviour(this, 5000){
					protected void onTick(){
						DFAgentDescription template = new DFAgentDescription();
						ServiceDescription sd = new ServiceDescription();
						sd.setType("medical-searching");
						template.addServices(sd);
						try{
							DFAgentDescription [] result = DFService.search(myAgent, template);
							searchAgents = new AID[result.length];
							for(int i = 0; i<result.length; i++){
								searchAgents[i] = result[i].getName();
							}
						}//end try
						catch(FIPAException fe){
							fe.printStackTrace();
						}//end catch
						
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						for(int i=0; i<searchAgents.length; i++){
						    msg.addReceiver(searchAgents[i]);
						}
						
						OntologyTerm getOntologyTerm = searchUserTermAndOntology(searchTerm);
						msg.setContent((getOntologyTerm.getMedTerm().trim() + "|" +getOntologyTerm.getMedCategory().trim()).trim());
						msg.setConversationId("medical-searching");
						msg.setReplyWith("msg"+System.currentTimeMillis());//unique value
						myAgent.send(msg);	
						mt = MessageTemplate.and(MessageTemplate.MatchConversationId("medical-searching"), MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
					}
				});	
		   }//end if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		}//end if (args != null && args.length > 0)
		
		else {
			// Make the agent terminate
			System.out.println("No target medical search term specified");
			doDelete();
		}
    }//end setup()
	
	
	
	//agent clean-up operations here
	protected void takeDown() {
		//printout a dismissal message
		System.out.println("Medical Query-agent "+getAID().getName()+" terminating.");
	}//end takeDown()
	
	
	
	//search the Ontology, by first looking at the user's search term - (searchTerm passed-in as parameter)
	private static OntologyTerm searchUserTermAndOntology(String searchTerm){
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
					    	try{
					    	    //send-out medterm = ontologyClassStr and medcategory = ontologyClassStr
					    		return new OntologyTerm(ontologyClassStr, ontologyClassStr);
					    	}//end try
						    catch(Exception e){
							     System.out.println("Error");
							     e.printStackTrace();
						    }//end catch	
					    }//end if
				         OntClass cla = inf.getOntClass(URI + ontologyClassStr);
                  for (Iterator i = cla.listSubClasses(); i.hasNext();) {
                      OntClass c = (OntClass) i.next();	     
					  if (searchTerm.toLowerCase().contains(c.getLocalName().toString().toLowerCase())){
						    try{
						        //send-out medterm = c.getLocalName().toString() and medcategory = ontologyClassStr
						    	return new OntologyTerm(c.getLocalName().toString(), ontologyClassStr);
						    }//end try
						    catch(Exception e){
							     System.out.println("Error");
							     e.printStackTrace();
						    }//end catch	
					  }//end if
                  }//end for                   
			  }//end if
			  else if(!ontologyClass.hasSubClass() && !ontologyClass.hasSuperClass()){
				     if (searchTerm.toLowerCase().contains(ontologyClassStr.toLowerCase())){
				    	 try{
				    	     //send-out medterm = ontologyClassStr and medcategory = ontologyClassStr
				    		 return new OntologyTerm(ontologyClassStr, ontologyClassStr); 
				    	 }//end try
					     catch(Exception e){
						     System.out.println("Error");
						     e.printStackTrace();
					     }//end catch	
				     }//end if
		  }
		}//end while
		
		 //Medical Term not found in Ontology
		 if(!foundUserMedicalTermInOnt){
			 return new OntologyTerm("", "");
		 }
		 
		 //default return
		 return new OntologyTerm("", "");		 
	}//end searchUserTermAndOntology
}//end MedicalQueryAgent class
