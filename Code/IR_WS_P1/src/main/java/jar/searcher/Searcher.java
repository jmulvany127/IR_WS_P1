
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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Searcher {
    private IndexSearcher searcher;
    private Analyzer analyzer;

    public Searcher(String indexPath, Analyzer newAnalyzer) throws IOException {
        searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(Paths.get(indexPath))));
        analyzer = newAnalyzer;
    }

    public void searchCranQueries(List<MyQuery> queries, String outputFilePath) throws Exception {
        QueryParser parser = new QueryParser("words", analyzer); // "words" field corresponds to document content
        FileWriter writer = new FileWriter(outputFilePath, false);
        int queryId =1;
        for (MyQuery query : queries) {
            // Process each query, assuming customQuery.getText() returns the raw query text
            String escapedQueryText = QueryParser.escape(query.getText());
            Query luceneQuery = parser.parse(escapedQueryText); //fix this to set Query

            // Get top 50 documents
            TopDocs results = searcher.search(luceneQuery, 50);

            // Write the results to a file in the format needed by trec_eval
            writeTopResults(writer, Integer.toString(queryId), results);
            queryId++;
        }

        writer.close();
    }

    private void writeTopResults(FileWriter writer, String queryId, TopDocs results) throws IOException {
        int rank = 0;
        for (ScoreDoc scoreDoc : results.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String docId = doc.get("id");  // Ensure your CranDoc ID is stored in the index as "id"
            float score = scoreDoc.score;
            rank++;

            // Write in the format required for trec_eval: query_id Q0 doc_id rank score STANDARD
            writer.write(String.format("%s Q0 %s %d %.4f STANDARD\n",
                    queryId, docId, rank, score));  // Adjust rank as needed
        }
    }

    public void close() throws IOException {
        searcher.getIndexReader().close();
        analyzer.close();
    }

}