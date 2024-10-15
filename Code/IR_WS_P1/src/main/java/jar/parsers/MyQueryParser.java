package parsers;

import model.MyQuery; //causing issues with exitsing lucene class

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyQueryParser {

    // Constants for query indicators
    private static final String  QUERY_ID = ".I";
    private static final String WORDS  = ".W";
    
    //takes path to query doc and returns list of of query objects
    public static List<MyQuery> queryReader(String path){

        List<MyQuery> queries = new ArrayList();
        Object[] lineQuery;

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path))) {
            String queryLine;
            //read first line
            queryLine =bufferedReader.readLine();
            while (queryLine  != null){
                //start of query found, start parsing
                if (queryLine.startsWith(QUERY_ID)){
                    lineQuery = queryParser(bufferedReader, queryLine);
                    queryLine = (String) lineQuery[0];//unpack first line of new query
                    queries.add((MyQuery) lineQuery[1]);
                // if problem with query formatting, this prevents loops/stuck
                }else{queryLine =bufferedReader.readLine();}
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return queries;
    }

    //takes buffered reader and the first line of query
    //returns an object containing and query and the first line of the next query 
    private static Object[] queryParser( BufferedReader bufferedReader, String queryLine) throws IOException{
        
       MyQuery query= new MyQuery();
        query.setId(queryLine.substring(3));

        Object[] lineSection;

        //adds each field to the query
        while (queryLine  != null){
            if (queryLine.startsWith(WORDS)) {
                lineSection = parseSection(bufferedReader, QUERY_ID);
                queryLine = (String)lineSection[0];
                query.setText((String)lineSection[1]); 

                break; // End of query, exit 
            } else{
                queryLine =bufferedReader.readLine(); // for the Bibliography field/errors in query format, prevents loops
            }

        }
        return new Object[]{queryLine, query};
    }

    //takes buffered line reader and indicator of next section of dquery
    // returns an object containg a section of the query and the first line of the next section
    private static Object[] parseSection(BufferedReader bufferedReader, String nextSection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String queryLine;
        //build string from the query section
        while ((queryLine = bufferedReader.readLine()) != null && !queryLine.startsWith(nextSection)) {
            stringBuilder.append(queryLine).append(" ");
        }
        String section = stringBuilder.toString().trim();
        return new Object[]{queryLine, section};  // Return trimmed section

    }



    public static void printQuery(MyQuery query) {
        System.out.println("Query:");
        System.out.println("ID: " + query.getId());
        System.out.println("Words: " + query.getText());
    }

    // Main method for testing
    public static void main(String[] args) {
        String filePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.qry"; 
        List<MyQuery> queries = queryReader(filePath);
       MyQuery query;
        for (int i = 0; i < 10; i++){        
            query= queries.get(i);
            printQuery(query);
        }
    

    }
}
