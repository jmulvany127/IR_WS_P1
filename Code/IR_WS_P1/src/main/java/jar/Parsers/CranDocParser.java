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

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(path))) {
            String docLine;
            
            while ((docLine =bufferedReader.readLine()) != null){
                System.out.println("readDoc: " + docLine);
                if (docLine.startsWith(DOC_ID)){
                    cranDocs.add(docParser(bufferedReader, docLine));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return cranDocs;
    }

    private static CranDoc docParser( BufferedReader bufferedReader, String docId) throws IOException{
        CranDoc cranDoc = new CranDoc();

        cranDoc.setId(docId.substring(3));
        String docLine;
        System.out.println("docID: " + docId);
        while ((docLine =bufferedReader.readLine()) != null){
            System.out.println("docparse: " + docLine);
            if (docLine.startsWith(TITLE)) {
                cranDoc.setTitle(parseSection(bufferedReader, AUTHOR)); // Implement this method
            } else if (docLine.startsWith(AUTHOR)) {
                cranDoc.setAuthor(parseSection(bufferedReader, BIB)); // Implement this method
            } else if (docLine.startsWith(WORDS)) {
                cranDoc.setWords(parseWordsSection(bufferedReader)); // Implement this method
                break; // End of document, exit the loop
            } 

        }
        return cranDoc;
    }

    private static String parseSection(BufferedReader bufferedReader, String nextSection) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String docLine;

        while ((docLine = bufferedReader.readLine()) != null && !docLine.startsWith(nextSection)) {
            System.out.println("parseSec: " + docLine);
            stringBuilder.append(docLine).append(" ");
        }
        System.out.println("parseSec2: " + docLine);
        return stringBuilder.toString().trim(); // Return trimmed section

    }

    private static String parseWordsSection(BufferedReader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String docLine;

        while ((docLine = bufferedReader.readLine()) != null && !docLine.startsWith(DOC_ID)) {
            System.out.println("parseWords: " + docLine);
            stringBuilder.append(docLine).append(" ");
        }
        System.out.println("parseWords2: " + docLine);
        return stringBuilder.toString().trim(); // Return trimmed words
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
        String filePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.10"; // Replace with your file path
        List<CranDoc> cranDocs = docReader(filePath);
        CranDoc cranDoc;
        /*for (int i = 0; i < 10; i++){        
            cranDoc = cranDocs.get(i);
            printCranDoc(cranDoc);
        }*/
        // Display parsed documents

    }
}
