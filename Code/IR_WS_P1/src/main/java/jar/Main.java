

import model.CranDoc;
import model.MyQuery;
import parsers.CranDocParser;
import parsers.MyQueryParser;
import searcher.Searcher;
import indexer.Indexer;
import eval.Eval;

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

public class Main{
    public static void main(String[] args) {
        //String CustomAnalyzerIndexPath = "Code/IR_WS_P1/Indexes/CustomAnalyzerIndex";
        //String EnglishAnalyzerIndexPath = "Code/IR_WS_P1/Indexes/EnglishAnalyzerIndex";
        //String WhitespaceAnalyzerIndexPath = "Code/IR_WS_P1/Indexes/WhitespaceAnalyzerIndex";
        //String StandardAnalyzerIndexPath = "Code/IR_WS_P1/Indexes/StandardAnalyzerIndex";
      
        Object[][] analyzerObjects = {
            //[,,"Code/IR_WS_P1/Indexes/CustomAnalyzerIndex"],
            ["EnglishAnalyzer",new EnglishAnalyzer(),"Code/IR_WS_P1/Indexes/EnglishAnalyzerIndex", "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/englishAnSearchResults.txt"],
            ["WhitespaceAnalyzer",new WhitespaceAnalyzer(),"Code/IR_WS_P1/Indexes/WhitespaceAnalyzerIndex","/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/whitespaceAnSearchResults.txt"],
            ["StandardAnalyzer",new StandardAnalyzer(),"Code/IR_WS_P1/Indexes/StandardAnalyzerIndex","/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/standardAnSearchResults.txt"]
        };

                
        String cranDocPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.1400";
        String queryPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.qry";
        String searchResultsPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/results.txt";

        String qrelFilePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cranqrel";
        String trecEvalPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/trec_eval-9.0.7/trec_eval";
        String trecEvalResultsPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/trec_eval_results.txt";

  
        List<MyQuery> queries = MyQueryParser.queryReader(queryPath);
        List<CranDoc> cranDocs = CranDocParser.docReader(cranDocPath);
      
        for (Object[] analyzerObject : analyzerObjects){
            try {
                Indexer indexer = new Indexer((String) analyzerObject[2], (Analyzer) analyzerObject[1]);
                indexer.indexCranDocs(cranDocs);
                indexer.close();
                System.out.println("Indexing completed successfully.");
            } catch (IOException e) {
                System.err.println("Error during indexing: " + e.getMessage());
            }
        

        // Step 3: Search the index using the queries and save results to a file
            try {
                Searcher searcher = new Searcher((String) analyzerObject[2], (Analyzer) analyzerObject[1]);  // Initialize the searcher with the index path
                searcher.searchCranQueries(queries, analyzerObject[3]);  // Perform the search and write results to file
              
                searcher.close();
                System.out.println("Search completed and results written to " + searchResultsPath);
            } catch (Exception e) {
                System.err.println("Error during search: " + e.getMessage());
            }
        }


        try {
            Eval.trecEval(trecEvalPath, qrelFilePath, searchResultsPath,trecEvalResultsPath);
            } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}


/*
./trec_eval -q /home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cranqrel /home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/results.txt


*/