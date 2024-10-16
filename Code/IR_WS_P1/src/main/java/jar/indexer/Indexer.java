package indexer;

import model.CranDoc;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
//import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Indexer {

    private Directory indexDirectory;
    private Analyzer analyzer;

    public Indexer(String indexPath) throws IOException {
        // Create the index directory
        indexDirectory = FSDirectory.open(Paths.get(indexPath));
        analyzer = new EnglishAnalyzer(); 
    }

    public void indexCranDocs(List<CranDoc> cranDocs) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(indexDirectory, config);

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
        analyzer.close();
        indexDirectory.close();
    }
}
