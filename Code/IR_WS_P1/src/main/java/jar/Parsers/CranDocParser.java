package Parsers;

import jar.model.CranDoc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CranDocParser {

    // Constants for document indicators
    private static final String  DOC_ID = ".I";
    private static final String TITLE  = ".T";
    private static final String AUTHOR  = ".A";
    private static final String WORDS  = ".W";
    
    //takes path to cran docs and returns list of of cran doc objects
    public static List<CranDoc> docReader(String path){

        List<CranDoc> cranDocs = new ArrayList();

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path))) {
            String docLine;

            while (bufferedReader.readLine() != null){
                docLine = bufferedReader.readLine();
                if (docLine.startsWith(DOC_ID)){
                    cranDocs.add(docParser(bufferedReader, docLine));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return cranDocs;
    }

    private static CranDoc docParser( BufferedReader bufferedReader, String docLine) throws IOException{
        CranDoc cranDoc = new CranDoc();
        System.out.println(docLine);
        
        return cranDoc;
    }

    // Main method for testing
    public static void main(String[] args) {
        String filePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.1400"; // Replace with your file path
        List<CranDoc> cranDocs = docReader(filePath);

        // Display parsed documents

    }
}
