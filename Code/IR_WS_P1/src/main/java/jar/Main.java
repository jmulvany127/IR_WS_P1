

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
    //main pipeline to create indexer and searcher, search over the index using different analyser and similarity scores, and then evaluate and present all results
    public static void main(String[] args) {
       
       //object to store info of different analysers being tested
        Object[][] analyzerObjects = {
            {"EnglishAnalyzer",new EnglishAnalyzer()},
            {"WhitespaceAnalyzer",new WhitespaceAnalyzer()},
            {"StandardAnalyzer",new StandardAnalyzer()}
        };

        //object to store info of different similarity score methods being tested
        Object[][] similarityObjects = {
            {"BM25",new BM25Similarity()},
            {"VSM",new ClassicSimilarity()},
        };

        // paths of various directories needed for pipeline
        String baseIndexPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/IR_WS_P1/Indexes/";
        String baseResultPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/results/";
        String cranDocPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.all.1400";
        String queryPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cran.qry";
        String qrelFilePath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/cranfield/cranqrel";
        String trecEvalPath = "/home/jsmulvany127/IR_WS_P1/IR_WS_P1/Code/trec_eval-9.0.7/trec_eval";

        //use custom query and doc parser to get lists of queries and docs
        List<MyQuery> queries = MyQueryParser.queryReader(queryPath);
        List<CranDoc> cranDocs = CranDocParser.docReader(cranDocPath);

        
        //for all combinations of anlayser and similarity score method search the index and evaluate and present the results
        for (Object[] analyzerObject : analyzerObjects){
          
            String analyzerName = (String) analyzerObject[0];
            Analyzer analyzer = (Analyzer) analyzerObject[1];
        
            for (Object[] similarityObject : similarityObjects){
            
                String similarityName = (String) similarityObject[0];
                Similarity similarity = (Similarity) similarityObject[1];

                // Construct paths to index and results folders
                String indexPath = baseIndexPath +similarityName +"_" +  analyzerName + "Index";
                String searchResultPath = baseResultPath + "searchResults/" + similarityName +"_"+ analyzerName + "SearchResults.txt";
                String evalResultPath = baseResultPath + "trecEvalResults/" + similarityName +"_" + analyzerName + "EvalResults.txt";
                
                //create indexer for list of cranDocs
                try {
                    Indexer indexer = new Indexer(indexPath, analyzer);
                    indexer.indexCranDocs(cranDocs);
                    indexer.close();
                } catch (IOException e) {
                    System.err.println("Error during indexing: " + e.getMessage());
                }
            

                // Search the index using the queries and save results to a file
                try {
                    Searcher searcher = new Searcher(indexPath, analyzer, similarity);  
                    searcher.searchCranQueries(queries, searchResultPath);  
                    searcher.close();
                } catch (Exception e) {
                    System.err.println("Error during search: " + e.getMessage());
                }

                // evaluate the results using trec_eval and save results to file
                try {
                    Eval.trecEval(trecEvalPath, qrelFilePath, searchResultPath, evalResultPath);
                } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                }

                // Parse and output only necessary stats
                String[] searcherType = {similarityName, analyzerName};
                Eval.parseResults(evalResultPath, searcherType );
            }
        analyzer.close();
        }
    }
}

