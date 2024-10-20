# Information Retrieval and Web Search Project 1 - TCD Year 5
This project involved building a search engine for the Cranfield dataset using Apache Lucene, focusing on indexing, and querying 
1,400 documents with 225 queries. Note the Repository includes the trec_eval evaluation file and is thus indicated as a C repositry however all code work was completed in Java. 
### Pre-requisites
Run the make command in the trec_eval folder within the repository
### Running
Navigate to Code/IR_WS_P1 and execute the following command:

bash run.sh

This consists of the following commands:

mvn clean package // for building the jar
java -jar target/IR_WS_P1-1.2.jar // to run the jar 

The Main class method will run the entire search pipeline.
This include parsing, indexing, searching and evaluating the results given. 
