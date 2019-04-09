-------------------------------------------------------------------------------------------------------
					        Medical File & Database Tagging Program - 2016
							      A Console Application
						         Programmer: Zakia Salod
-------------------------------------------------------------------------------------------------------
						
What is it?
-----------
The Medical File & Database Tagging Program - 2016 is a software used to tag keywords 
that are found in a text file. The program reads-in a text file, for example,
a medical journal article, Doctor's notes and finds pre-defined keywords
and tags them. The pre-defined keywords are read from a database (medassign02) table (medkeyword).
After tagging, the result is stored onto another text file (name of this file input by user). 
The result/s of the count of number of occurrences of the Medical terms from the input file, are 
stored onto a database (medassign02) table (medpaperkeycount).
This program was created by the programmer, as part of a Medical Informatics Programming course
at the University of KwaZulu-Natal Nelson R Mandela School of Medicine campus in South Africa in
the year 2016.

Note
-----------
Input >>>
	Sample Medical Text File can be found here :
		o medicalinputfiles\sampleinputtxtfile.txt
	Sample Medical Keywords From Database (medassign02) :
		o table = medkeyword

Output >>>
	Directory :
		o Output file/s are stored in the "medicaloutputfiles" directory.
		o If the "medicaloutputfiles" directory does not exist, the program will
	      create this directory - for the first run of this program.
	    o Subsequent runs of the program will store output files onto this directory.
	File/s :
	    o The user is prompted for the output file name. If the file name already
	      exists in the "medicaloutputfiles" directory, the program continues to 
	      prompt the user for a unique file name.
	Database table/s:
		o Number of occurrences of each term from the medkeyword database table, found in
		  input file medicalinputfiles\sampleinputtxtfile.txt, is stored onto database table 
		  medpaperkeycount. Upon first execution of the program, this database table is created.
		  Subsequent runs, stores / inserts data onto this table, by first deleting the current
		  corresponding row for it, from this table. This ensures up-to-date data is stored 
		  for each input file, onto this table - i.e. cater for multiple runs of the same input file/s.

Contact
-----------
		o If you would like more info about the Medical File & Database Tagging Program,
	      or require free support for this program, please contact the programmer
	      at http://www.zakiasalod.weebly.com or zakia.salod@gmail.com

-------------------------------------------------------------------------------------------------------
                   Copyright (C) 2016. All Rights Reserved. Zakia Salod		
-------------------------------------------------------------------------------------------------------