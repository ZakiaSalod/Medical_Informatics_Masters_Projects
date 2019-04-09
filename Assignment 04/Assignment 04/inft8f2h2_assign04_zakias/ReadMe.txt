-------------------------------------------------------------------------------------------------------------------------------------
					            Medical Distributed Search Engine Program - 2016
							                 A Console Application
						                    Programmer: Zakia Salod
-------------------------------------------------------------------------------------------------------------------------------------
						
What is it?
-----------
The Medical Distributed Search Engine Program - 2016 is a software used to search the database for files on the system with the 
medical term entered by the user. The program does this by consulting the Ontology file, and then using the hierarchy from there, 
together with the search term entered by the user (this is done by the Medical Query Agent), and then searches the database for all 
relevant files (this is done by the Medical Search Agent). If the term is found on the database, then the program displays all file 
(paper) information, in the order of the most relevant - i.e. in terms of the date in which the file was loaded onto the system - 
in reverse chronological order (latest entries first). 

Approach :
Software Agents are used as query agents ("Medical Query Agent") and search agents ("Medical Search Agent") to perform the relevant 
operations, using the Java Agent Development Environment (JADE). The query agent prompts the user for a query. The query agent then
consults the Ontology to discover what term (semantically) needs to be sent to the search agent to query. This is then directed
to the Yellow Pages Service of JADE, by the use of an agent called the Directory Facilitator (DF). The DF then directs the query to
the search agent. The search engine then receives the query from the query agent and searches the database. All results that satisfies
the search from the database, is output to the console.  

This program was created by the programmer, as part of a Medical Informatics Programming course at the University of KwaZulu-Natal 
Nelson R Mandela School of Medicine campus in South Africa in the year 2016.

Note
-----------
Input >>>
	Sample Medical (.OWL) Ontology file can be found here :
		o medicalinputfiles\medicalterm.owl

Output >>>
	From User Search Term Entered :-
		Console :
			o File (paper) information from the database, that satisfies the search term/s
			  entered by the user (which is first directed to the Ontology file to check for, semantically, which term/s need
			  to be sent to the search agent to search its database for).

Contact
-----------
		o If you would like more info about the Medical Distributed Search Engine Program,
	      or require free support for this program, please contact the programmer
	      at http://www.zakiasalod.weebly.com or zakia.salod@gmail.com

-------------------------------------------------------------------------------------------------------------------------------------
                                 Copyright (C) 2016. All Rights Reserved. Zakia Salod		
-------------------------------------------------------------------------------------------------------------------------------------