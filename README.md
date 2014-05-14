##Introduction 

This is a prototype implementation of the MinHash technique for quickly estimating 
how similar two sets are. 

##Requirements

* Sbt 0.13
* Python (for creating datasets)
* Java 1.7 or higher

##Extra Folders

* generator/: contains a python script (generator.py) to generate datasets.
* input/: contains two sampleInputs ready to be used.
* report/: contains the latex source for this project report.

##Build and Run

* From a console, go to the project source folder and execute: 
	$> sbt "run <path to input> <number of hash functions> <user to be recommended>"
  For example:
    $> sbt "run input/sampleInput2.txt 4 2" 

   The output should give a List[(userId,SimIndex)] in descending order and 
   a list of products to be recommended.
   
   Note: for number of hash functions more than 5, it may need to run the algorithm 
   several times until a recommendation is shown.
   
