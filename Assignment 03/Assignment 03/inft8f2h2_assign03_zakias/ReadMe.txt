-------------------------------------------------------------------------------------------------------------------------------------
					            Medical Semantic Search Engine Program - 2016
							                 A Console Application
						                    Programmer: Zakia Salod
-------------------------------------------------------------------------------------------------------------------------------------
						
What is it?
-----------
The Medical Semantic Search Engine Program - 2016 is a software used to tag keywords 
that are found in a text file, and to also search the database for files on the system with the medical
term entered by the user. The program reads-in a text file, for example, a medical journal article, 
Doctor's notes and finds pre-defined keywords, and tags them. The program then writes this tagged information onto a 
new .TXT file and stores them onto a "medicaloutputfiles" directory. The pre-defined keywords are read from an (.OWL) 
Ontology file, which was created using Protege. Optionally, the user has a facility to also enter medical term/s to find 
all files (papers) on the system that has this medical term/s. The program does this by consulting the Ontology file, 
and then using the hierarchy from there, together with the search term entered by the user, and then searches the database 
for all relevant files. If the term is found on the database, then the program displays all file (paper) information, in 
the order of the most relevant - i.e. in terms of the date in which the file was loaded onto the system - in reverse 
chronological order (latest entries first). 
This program was created by the programmer, as part of a Medical Informatics Programming course
at the University of KwaZulu-Natal Nelson R Mandela School of Medicine campus in South Africa in
the year 2016.

Note
-----------
Input >>>
	Sample Medical Text File & (.OWL) Ontology file can be found here :
		o medicalinputfiles\sampleinputtxtfile1.txt
	Sample Medical Keywords Text File can be found here :
		o medicalinputfiles\organism.owl

Output >>>
	From Tagging :-
		Directory :
			o Output file/s are stored in the "medicaloutputfiles" directory.
			o If the "medicaloutputfiles" directory does not exist, the program will
			  create this directory - for the first run of this program.
			o Subsequent runs of the program will store output files onto this directory.
		File/s :
			o The user is prompted for the output file name. If the file name already
			  exists in the "medicaloutputfiles" directory, the program continues to 
			  prompt the user for a unique file name.
	From User Search Term Entered :-
		Console :
			o File (paper) information from the database, that satisfies the search term/s
			  entered by the user.

Contact
-----------
		o If you would like more info about the Medical Semantic Search Engine Program,
	      or require free support for this program, please contact the programmer
	      at http://www.zakiasalod.weebly.com or zakia.salod@gmail.com

-------------------------------------------------------------------------------------------------------------------------------------
                                 Copyright (C) 2016. All Rights Reserved. Zakia Salod		
-------------------------------------------------------------------------------------------------------------------------------------