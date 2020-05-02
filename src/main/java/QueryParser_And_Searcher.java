import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QueryParser_And_Searcher
{
    public static final String INDEX_DIR = "src/main/resources/index";
    public static void search(BufferedReader br) throws Exception {
        IndexSearcher searcher = createSearcher();
        PrintWriter writer = new PrintWriter("src/main/resources/trec_eval-9.0.7/results_synonym.txt", "UTF-8");  //0.3384 results_withoutsynonym

        String currentLine = br.readLine();
//        StringBuilder search = new StringBuilder();
        String title = "";
        StringBuilder description = new StringBuilder();
        StringBuilder narrative = new StringBuilder();
        int i = 1;
        int index = 401;
        boolean flag = false;
        int check = 0;
        while (currentLine != null) {
            if (currentLine.matches("</top>")) {
                //System.out.println(title + "\t" + processQuery(narrative.toString()) + "\t" + description.toString());
//                TopDocs topDocs = SearchByAll((title + " " + processQuery(narrative.toString()) + " " +
//                        description.toString()).replaceAll("[^\\w\\s]", ""), searcher);
                HashMap<String, String> map = new HashMap<>();
                map.put("title",title.replaceAll("[^\\w\\s]", "").toLowerCase());
                map.put("description",description.toString().replaceAll("[^\\w\\s]", "").toLowerCase());
                map.put("narrative",processQuery(narrative.toString()).replaceAll("[^\\w\\s]", "").toLowerCase());
                TopDocs topDocs = SearchByAll(map,searcher);
                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                for (int sd = 0; sd < scoreDocs.length; sd++) {
                    Document d = searcher.doc(scoreDocs[sd].doc);
//                    System.out.println(index + "\t" + "Q0" + "\t" + String.format(d.get("DOCNO")) + "\t" +
//                    i + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
//                    writer.println(index + "\t" + "Q0" + "\t" + String.format(d.get("DOCNO")) + "\t" + i
//                    + "\t" + scoreDocs[sd].score + "\t" + "STANDARD");
                    System.out.println(index + "\t" + "0" + "\t" + String.format(d.get("DOCNO").toUpperCase()) + "\t" + "0" + "\t" +
                            scoreDocs[sd].score + "\t" + "STANDARD");
                    writer.println(index + "\t" + "0" + "\t" + String.format(d.get("DOCNO").toUpperCase()) + "\t" + "0" + "\t" +
                            scoreDocs[sd].score + "\t" + "STANDARD");
                    i++;
                }
                index = index + 1;
                flag = false;
//                search = new StringBuilder();
                narrative = new StringBuilder();
                description = new StringBuilder();
            } else if (currentLine.contains("<title>")) {
//                search.append(currentLine.substring(8) + " ");
                title = currentLine.substring(8);
            } else if (currentLine.contains("<desc>")) {
                //search = new StringBuilder();
                flag = true;
                check = 0;
            } else if (currentLine.contains("<narr>")) {
                //search = new StringBuilder();
                flag = true;
                check = 1;
            } else {
                if (flag)
                {
                    if(check == 0){
                        description.append(currentLine + " ");
                    }
                    else if(check == 1)
                    {
                        narrative.append(currentLine + " ");
                    }
                }
            }
            currentLine = br.readLine();
        }
        writer.close();
    }
    private static TopDocs SearchByAll(HashMap<String,String> queryMap, IndexSearcher searcher) throws Exception {

        HashMap<String, Float> boosts = new HashMap<String, Float>();
        boosts.put("HEADLINE", (float) 0.1);
        boosts.put("TEXT", (float) 0.9);
        MultiFieldQueryParser qp = new MultiFieldQueryParser(new String[]{"TEXT","HEADLINE"},new CA(),boosts);

        String title =  queryMap.get("title");
        String description =  queryMap.get("description");
        String narrative =  queryMap.get("narrative");

        Query q1 = qp.parse(QueryParserUtil.escape(title));
        Query q2 = qp.parse(QueryParserUtil.escape(description));
        Query q3 = qp.parse(QueryParserUtil.escape(narrative));


        BooleanQuery finalQuery = new BooleanQuery.Builder()
                .add(new BoostQuery(q1,5), BooleanClause.Occur.MUST)
                .add(new BoostQuery(q2,(float) 1.8), BooleanClause.Occur.MUST)
                .add(new BoostQuery(q3,(float)1.3), BooleanClause.Occur.MUST)
                .build();

        TopDocs hits = searcher.search(finalQuery, 1000);
        return hits;
    }

    private static IndexSearcher createSearcher() throws IOException {
        Directory dir = FSDirectory.open(Paths.get(INDEX_DIR));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        //searcher.setSimilarity(new LMDirichletSimilarity());
        return searcher;
    }

    public static String processQuery(String qry)
    {
        int i,j;

        StringBuffer sb = new StringBuffer("a ");
        String[] some = qry.split("\\.");
        for(i=0;i<some.length;i++) {
            if(some[i].contains(",")) {
                String[] somemore = some[i].split("\\,");
                for(j=0;j<somemore.length;j++) {
                    if(!(somemore[j].contains("not relevant") ||somemore[j].contains("irrelevant")))
                    {
                        sb.append(somemore[j]);
                    }
                }
            }
            else
            {
                if(!(some[i].contains("not relevant") ||some[i].contains("irrelevant")))
                {
                    sb.append(some[i]);
                }
            }
        }
        return sb.toString().trim().replaceAll(" +", " ");
    }
    public static void main(String[] args) throws Exception {
        File file = new File("src/main/resources/topics");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        search(bufferedReader);
    }
}
