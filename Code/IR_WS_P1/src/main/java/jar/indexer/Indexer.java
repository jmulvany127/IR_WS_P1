package indexer;

import model.CranDoc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Indexer {

    private Directory indexDirectory;
    private Analyzer analyzer;
    
    //indxer constructor takes a directory to store index and an analyser to use
    public Indexer(String indexPath, Analyzer newAnalyzer) throws IOException {
     
        indexDirectory = FSDirectory.open(Paths.get(indexPath));
        analyzer = newAnalyzer; 
    }
    
    //takes a list of Crandocs, creates a lucene document for each and adds it to the index
    public void indexCranDocs(List<CranDoc> cranDocs) throws IOException {
        //create index writer with config file set to use specified analyser
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

        //adds each of the crandocs fields to the lucene index documents
        for (CranDoc cranDoc : cranDocs) {
            Document document = new Document();
            document.add(new StringField("id", cranDoc.getId(), Field.Store.YES));
            document.add(new TextField("title", cranDoc.getTitle(), Field.Store.YES));
            document.add(new TextField("author", cranDoc.getAuthor(), Field.Store.YES));
            document.add(new TextField("words", cranDoc.getWords(), Field.Store.YES));

            writer.addDocument(document);
        }

        writer.close();
    }

    public void close() throws IOException {
       
        indexDirectory.close();
    }
}
