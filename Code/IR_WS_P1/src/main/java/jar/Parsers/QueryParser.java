package Parsers;

import model.CranDoc;

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
    private static final String BIB  = ".B";
    private static final String AUTHOR  = ".A";
    private static final String WORDS  = ".W";
    
    //takes path to cran docs and returns list of of cran doc objects
    public static List<CranDoc> docReader(String path){

        List<CranDoc> cranDocs = new ArrayList();
        Object[] lineDoc;

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path))) {
            String docLine;
            docLine =bufferedReader.readLine();
            while (docLine  != null){
                //System.out.println("readDoc: " + docLine);
                if (docLine.startsWith(DOC_ID)){
                    lineDoc = docParser(bufferedReader, docLine);
                    docLine = (String) lineDoc[0];
                    cranDocs.add((CranDoc) lineDoc[1]);
                }else{docLine =bufferedReader.readLine();}
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return cranDocs;
    }

    private static Object[] docParser( BufferedReader bufferedReader, String docLine) throws IOException{
        CranDoc cranDoc = new CranDoc();
        cranDoc.setId(docLine.substring(3));

        
        Object[] lineSection;

        System.out.println("docID: " + docLine);
     
        while (docLine  != null){
            //System.out.println("docparse: " + docLine);
            if (docLine.startsWith(TITLE)) {
                lineSection = parseSection(bufferedReader, AUTHOR);
                docLine = (String)lineSection[0];

                cranDoc.setTitle((String)lineSection[1]); // Implement this method

            } else if (docLine.startsWith(AUTHOR)) {
                lineSection = parseSection(bufferedReader, BIB);
                docLine = (String)lineSection[0];

                cranDoc.setAuthor((String)lineSection[1]); // Implement this method
            } else if (docLine.startsWith(WORDS)) {
                lineSection = parseSection(bufferedReader, DOC_ID);
                docLine = (String)lineSection[0];

                cranDoc.setWords((String)lineSection[1]); // Implement this method
                break; // End of document, exit the loop
            } else{
                docLine =bufferedReader.readLine();
            }

        }
        return new Object[]{docLine, cranDoc};
    }

    private static Object[] parseSection(BufferedReader bufferedReader, String nextSection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String docLine;
        while ((docLine = bufferedReader.readLine()) != null && !docLine.startsWith(nextSection)) {
            //System.out.println("parseSec: " + docLine);
            stringBuilder.append(docLine).append(" ");
        }
        //System.out.println("parseSec2: " + docLine);
        String section = stringBuilder.toString().trim();


        return new Object[]{docLine, section};  // Return trimmed section

    }



    public static void printCranDoc(CranDoc cranDoc) {
        System.out.println("Cran Doc:");
        System.out.println("ID: " + cranDoc.getId());
        System.out.println("Title: " + cranDoc.getTitle());
        System.out.println("Author: " + cranDoc.getAuthor());
        System.out.println("Words: " + cranDoc.getWords());
    }

    // Main method for testing
    public static void main(String[] args) {
        String filePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.1400"; // Replace with your file path
        List<CranDoc> cranDocs = docReader(filePath);
        CranDoc cranDoc;
        for (int i = 0; i < 10; i++){        
            cranDoc = cranDocs.get(i);
            printCranDoc(cranDoc);
        }
        // Display parsed documents

    }
}
