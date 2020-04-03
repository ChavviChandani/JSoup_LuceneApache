import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Searcher {
    public static final String INDEX_DIR = "src/main/resources/index";

    public static void search(BufferedReader br) throws Exception {
        IndexSearcher searcher = createSearcher();
        PrintWriter writer = new PrintWriter("src/main/resources/results.txt", "UTF-8");
        String currentLine = br.readLine();
        StringBuilder search = new StringBuilder();
        int i = 1;
        int index = 401;
        boolean flag = false;
        while (currentLine != null) {
            if (currentLine.matches("</top>")) {
                TopDocs topDocs = SearchByAll(search.toString().replaceAll("[^\\w\\s]", ""), searcher);
                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                for (int sd = 0; sd < scoreDocs.length; sd++) {
                    Document d = searcher.doc(scoreDocs[sd].doc);
//                    System.out.println(index + "\t" + "Q0" + "\t" + String.format(d.get("DOCNO")) + "\t" + i + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
//                    writer.println(index + "\t" + "Q0" + "\t" + String.format(d.get("DOCNO")) + "\t" + i + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
                    System.out.println(index + "\t" + "0" + "\t" + String.format(d.get("DOCNO")) + "\t" + "0" + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
                    writer.println(index + "\t" + "0" + "\t" + String.format(d.get("DOCNO")) + "\t" + "0" + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
                    i++;
                }
                index = index + 1;
                flag = false;
                search = new StringBuilder();
            } else if (currentLine.contains("<title>")) {
                search.append(currentLine.substring(8) + " ");
            } else if (currentLine.contains("<desc>")) {
                //search = new StringBuilder();
                flag = true;
            } else if (currentLine.contains("<narr>")) {
                //search = new StringBuilder();
                flag = true;
            } else {
                if (flag) {
                    search.append(currentLine + " ");
                }
            }
            currentLine = br.readLine();
        }
        writer.close();
    }

    private static TopDocs SearchByAll(String search, IndexSearcher searcher) throws Exception {
        List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
        CharArraySet stopWordSet = new CharArraySet(stopWordList, true);
        //MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"DOCNO","TEXT","PARENT","HT","HEADER","PROFILE","DATE","HEADLINE","PUBLISHER","PAGE","BYLINE","DOCID","SECTION","LENGTH","GRAPHIC","TYPE","SUBJECT"}, new StandardAnalyzer(stopWordSet)); //0.1743
        //MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"DOCNO","TEXT","PARENT","HT","HEADER","PROFILE","DATE","HEADLINE","PUBLISHER","PAGE","BYLINE","DOCID","SECTION","LENGTH","GRAPHIC","TYPE","SUBJECT"}, new CustomAnalyser_Index()); //0.1366
        MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"DOCNO","TEXT","PARENT","HT","HEADER","PROFILE","DATE","HEADLINE","PUBLISHER","PAGE","BYLINE","DOCID","SECTION","LENGTH","GRAPHIC","TYPE","SUBJECT"}, new EnglishAnalyzer(stopWordSet));
        Query query = qp.parse(search);
        TopDocs hits = searcher.search(query, 1000);
        return hits;
    }

    private static IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        searcher.setSimilarity(new LMDirichletSimilarity());
        return searcher;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("src/main/resources/topics");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        search(bufferedReader);
    }
}