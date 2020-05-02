import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Indexer {
    static IndexWriter iwriter;

    public static void parse(Path path) throws IOException
    {
        List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
        CharArraySet stopWordSet = new CharArraySet(stopWordList, true);
        //Analyzer analyzer = new StandardAnalyzer(stopWordSet);
        //Analyzer analyzer = new EnglishAnalyzer(stopWordSet);
        Analyzer analyzer= new CA();
        //Analyzer analyzer = new CustomAnalyzerNew();
        //Analyzer analyzer = new CustomAnalyser_Index();
        Directory directory = FSDirectory.open(Paths.get("src/main/resources/index"));
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        //config.setSimilarity(new LMDirichletSimilarity());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        iwriter = new IndexWriter(directory, config);
        org.apache.lucene.document.Document parseDoc;
        File input = new File(String.valueOf(path));
        Document doc;
        doc = Jsoup.parse(input, "UTF-8");
        Elements documents = doc.select("DOC");
        for (Element document : documents) {
            parseDoc = new org.apache.lucene.document.Document();
            if (document.select("DOCNO").text().contains("FR")) {
                parseDoc.add(new TextField("DOCNO", document.select("DOCNO").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("HEADLINE", document.select("DOCTITLE").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("TEXT", document.select("TEXT").text().toLowerCase() + "", Field.Store.YES));
            } else if (document.select("DOCNO").text().contains("FB")) {
                parseDoc.add(new TextField("DOCNO", document.select("DOCNO").text().toLowerCase() + "", Field.Store.YES));
//                parseDoc.add(new TextField("HT", document.select("HT").text() + "", Field.Store.YES));
                parseDoc.add(new TextField("HEADLINE", document.select("HEADER").select("TI").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("TEXT", document.select("TEXT").text().toLowerCase() + "", Field.Store.YES));
            } else if (document.select("DOCNO").text().contains("FT")) {
                parseDoc.add(new TextField("DOCNO", document.select("DOCNO").text().toLowerCase() + "", Field.Store.YES));
//                parseDoc.add(new TextField("PROFILE", document.select("PROFILE").text() + "", Field.Store.YES));
//                parseDoc.add(new TextField("DATE", document.select("DATE").text() + "", Field.Store.YES));
                parseDoc.add(new TextField("HEADLINE", document.select("HEADLINE").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("TEXT", document.select("TEXT").text().toLowerCase() + "", Field.Store.YES));
//                parseDoc.add(new TextField("PUBLISHER", document.select("PUB").text() + "", Field.Store.YES));
//                parseDoc.add(new TextField("PAGE", document.select("PAGE").text() + "", Field.Store.YES));
                parseDoc.add(new TextField("BYLINE", document.select("BYLINE").text().toLowerCase() + "", Field.Store.YES));
            } else if (document.select("DOCNO").text().contains("LA")) {
                parseDoc.add(new TextField("DOCNO", document.select("DOCNO").text().toLowerCase() + "", Field.Store.YES));
//                parseDoc.add(new TextField("DOCID", document.select("DOCID").text() + "", Field.Store.YES));
//                parseDoc.add(new TextField("DATE", document.select("DATE").text() + "", Field.Store.YES));
//                parseDoc.add(new TextField("SECTION", document.select("SECTION").text() + "", Field.Store.YES));
//                parseDoc.add(new TextField("LENGTH", document.select("LENGTH").text() + "", Field.Store.YES));
                parseDoc.add(new TextField("HEADLINE", document.select("HEADLINE").select("P").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("BYLINE", document.select("BYLINE").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("TEXT", document.select("TEXT").select("P").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("GRAPHIC", document.select("GRAPHIC").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("TYPE", document.select("TYPE").text().toLowerCase() + "", Field.Store.YES));
                parseDoc.add(new TextField("SUBJECT", document.select("SUBJECT").text().toLowerCase() + "", Field.Store.YES));
            }
            iwriter.addDocument(parseDoc);
        }
        iwriter.close();
        directory.close();
    }
    public static void main(String args[]) throws IOException {
        List<Path> fileList = ReadFiles.readFilesWithinFolders(Paths.get("src/main/resources/Assignment Two/"));
        for (Path p : fileList) {
            if (!(p.endsWith("readchg.txt") ||
                    p.endsWith("readmefb.txt") ||
                    p.endsWith("readmela.txt") ||
                    p.endsWith("readmefr") ||
                    p.endsWith("ReadMe.txt") ||
                    p.endsWith("fbisdtd.dtd") ||
                    p.endsWith("fr94dtd") ||
                    p.endsWith("ftdtd") ||
                    p.endsWith("latimesdtd.dtd") ||
                    p.endsWith("readchg") ||
                    p.endsWith("readfrcg") ||
                    p.endsWith("readmeft") ||
                    p.endsWith(".DS_Store"))) {
                parse(p);
            }
            System.out.println("Indexing:" +p);
        }
    }
}
