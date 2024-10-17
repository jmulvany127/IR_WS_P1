
package searcher;

import model.CranDoc;
import model.MyQuery;
import parsers.CranDocParser;
import parsers.MyQueryParser;
//import searcher.Searcher;
import indexer.Indexer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity; 

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Searcher {
    private IndexSearcher searcher;
    private Analyzer analyzer;
    
    //Searcher constructor takes an analyzer, an similarity score method and the path to the index
    public Searcher(String indexPath, Analyzer newAnalyzer, Similarity similarity ) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexPath))));
        analyzer = newAnalyzer;
        this.searcher.setSimilarity(similarity);
    }
    
    // takes a list of queries and a path to publish results if search
    // parses the queries, applies the analyser, searches each query against the index and adds top 50 documents with socres to results file
    public void searchCranQueries(List<MyQuery> queries, String outputFilePath) throws Exception {
        
        //"words" field corresponds to main body of text in index document
        QueryParser parser = new QueryParser("words", analyzer); 
        FileWriter writer = new FileWriter(outputFilePath, false);

        int queryId =1;
        for (MyQuery query : queries) {
            // handles esacpe characters i.e "?"
            String escapedQueryText = QueryParser.escape(query.getText());
            // create a lucene query with preprocessed query text
            Query luceneQuery = parser.parse(escapedQueryText); 

            //Get top 50 documents
            TopDocs results = searcher.search(luceneQuery, 50);

            // writes the results to a file in the format needed by trec_eval
            writeTopResults(writer, Integer.toString(queryId), results);
            queryId++;
        }

        writer.close();
    }
    
    // takes a file writer, query id and top doc results and writes results to file in format needed ofr trec_eval
    private void writeTopResults(FileWriter writer, String queryId, TopDocs results) throws IOException {
        int rank = 0;//relevance rank of each document

        //for each of 50 docs in top doc extract relavnt stats and write to result file
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String docId = doc.get("id"); 
            float score = scoreDoc.score;
            rank++;

            writer.write(String.format("%s Q0 %s %d %.4f STANDARD\n",
                    queryId, docId, rank, score));  
        }
    }

    public void close() throws IOException {
        searcher.getIndexReader().close();
    }

}