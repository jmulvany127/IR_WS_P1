

import model.CranDoc;
import model.MyQuery;
import parsers.CranDocParser;
import parsers.MyQueryParser;
import searcher.Searcher;
import indexer.Indexer;
import eval.Eval;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity; 

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
      
        Object[][] analyzerObjects = {
            {"EnglishAnalyzer",new EnglishAnalyzer()},
            {"WhitespaceAnalyzer",new WhitespaceAnalyzer()},
            {"StandardAnalyzer",new StandardAnalyzer()}
        };

        Object[][] similarityObjects = {
            {"BM25",new BM25Similarity()},
            {"VSM",new ClassicSimilarity()},
        };

    
        String baseIndexPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/IR_WS_P1/Indexes/";
        String baseResultPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/";
        String cranDocPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.1400";
        String queryPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.qry";
        String qrelFilePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cranqrel";
        String trecEvalPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/trec_eval-9.0.7/trec_eval";
  
        List<MyQuery> queries = MyQueryParser.queryReader(queryPath);
        List<CranDoc> cranDocs = CranDocParser.docReader(cranDocPath);

        
        
        for (Object[] analyzerObject : analyzerObjects){

            String analyzerName = (String) analyzerObject[0];
            Analyzer analyzer = (Analyzer) analyzerObject[1];
        
            for (Object[] similarityObject : similarityObjects){
            
                String similarityName = (String) similarityObject[0];
                Similarity similarity = (Similarity) similarityObject[1];

                // Construct paths 
                String indexPath = baseIndexPath +similarityName +"_" +  analyzerName + "Index";
                String searchResultPath = baseResultPath + "searchResults/" + similarityName +"_"+ analyzerName + "SearchResults.txt";
                String evalResultPath = baseResultPath + "trecEvalResults/" + similarityName +"_" + analyzerName + "EvalResults.txt";

                try {
                    Indexer indexer = new Indexer(indexPath, analyzer);
                    indexer.indexCranDocs(cranDocs);
                    indexer.close();
                    //System.out.println("Indexing completed successfully.");
                } catch (IOException e) {
                    System.err.println("Error during indexing: " + e.getMessage());
                }
            

            // Step 3: Search the index using the queries and save results to a file
                try {
                    Searcher searcher = new Searcher(indexPath, analyzer, similarity);  // Initialize the searcher with the index path
                    searcher.searchCranQueries(queries, searchResultPath);  // Perform the search and write results to file
                
                    searcher.close();
                    //System.out.println("Search completed and results written to " + searchResultPath + ".");
                } catch (Exception e) {
                    System.err.println("Error during search: " + e.getMessage());
                }
            
                try {
                    Eval.trecEval(trecEvalPath, qrelFilePath, searchResultPath, evalResultPath);
                } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                }
                String[] searcherType = {similarityName, analyzerName};
                Eval.parseResults(evalResultPath, searcherType );
            }
        analyzer.close();
        }
    }
}

