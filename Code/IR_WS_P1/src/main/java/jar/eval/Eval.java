package eval;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Eval{

    public static void trecEval(String trecEvalPath, String qrelFilePath, String searchResultsPath, String trecEvalResultsPath) throws IOException, InterruptedException{
    
        // Build the command to call trec_eval
        ProcessBuilder processBuilder = new ProcessBuilder(trecEvalPath, "-q", qrelFilePath, searchResultsPath);

        // Redirect the output of trec_eval to a file
        processBuilder.redirectOutput(new File(trecEvalResultsPath));

        // Start the process
        Process process = processBuilder.start();

        // Wait for the process to finish
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Trec_eval ran successfully. Results are stored in: " + trecEvalResultsPath);
        } else {
            System.out.println("Trec_eval encountered an error. Exit code: " + exitCode);
        }

   
    }



}