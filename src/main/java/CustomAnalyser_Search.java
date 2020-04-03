import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.util.CharsRef;

import java.io.IOException;

public class CustomAnalyser_Search extends Analyzer {
    @Override
    protected Analyzer.TokenStreamComponents createComponents(String fieldname) {
        StandardTokenizer source = new StandardTokenizer();
        TokenStream result = new ClassicFilter(source);
        result = new LowerCaseFilter(result);


        SynonymMap.Builder builder = new SynonymMap.Builder(true);
        builder.add(new CharsRef("one"), new CharsRef("first,alpha"), true);
        builder.add(new CharsRef("two"), new CharsRef("second"), true);
        builder.add(new CharsRef("german"), new CharsRef("nazi"), true);
        builder.add(new CharsRef("osteoporosis"), new CharsRef("disease"), true);
        builder.add(new CharsRef("violence"), new CharsRef("fight"), true);
        builder.add(new CharsRef("Parkinson's"), new CharsRef("parkinson,disease"), true);
        builder.add(new CharsRef("storms"), new CharsRef("hurricane,typhoons, cyclones"), true);
        builder.add(new CharsRef("tribunal"), new CharsRef("dispute"), true);
        builder.add(new CharsRef("schengan"), new CharsRef("EEA,EU,europe"), true);
        builder.add(new CharsRef("signatories"), new CharsRef("authority"), true);
        builder.add(new CharsRef("salvaging"), new CharsRef("wrecked"), true);
        builder.add(new CharsRef("minimills"), new CharsRef("steel"), true);
        builder.add(new CharsRef("golden triangle"), new CharsRef("burma,thailand,laos,trafficking"), true);
        builder.add(new CharsRef("gorges"), new CharsRef("political,social,ecological"), true);
        builder.add(new CharsRef("recycle"), new CharsRef("environment"), true);
        builder.add(new CharsRef("monoxide"), new CharsRef("pollution"), true);
        builder.add(new CharsRef("waste"), new CharsRef("garbage"), true);
        builder.add(new CharsRef("markovic"), new CharsRef("serbia,president"), true);
        builder.add(new CharsRef("suicide"), new CharsRef("death,murder,crime"), true);
        builder.add(new CharsRef("counterfeit"), new CharsRef("crime,money"), true);
        builder.add(new CharsRef("ultraviolet"), new CharsRef("harmful, radiation"), true);
        builder.add(new CharsRef("forged"), new CharsRef("counterfeit"), true);
        builder.add(new CharsRef("legionnaire"), new CharsRef("disease"), true);
        builder.add(new CharsRef("attack"), new CharsRef("kill,death,threat,harm"), true);
        builder.add(new CharsRef("police"), new CharsRef("security,"), true);
        builder.add(new CharsRef("stoic"), new CharsRef("philosophy"), true);
        builder.add(new CharsRef("estonia"), new CharsRef("country"), true);
        builder.add(new CharsRef("curbing"), new CharsRef("restrain,suppress,hold back"), true);
        builder.add(new CharsRef("electric"), new CharsRef("electricity"), true);
        builder.add(new CharsRef("scientific"), new CharsRef("scientist,discovery,invention"), true);
        builder.add(new CharsRef("lyme"), new CharsRef("disease"), true);
        builder.add(new CharsRef("heroic"), new CharsRef("brave,courageous,bold"), true);
        builder.add(new CharsRef("u.s."), new CharsRef("united states,country"), true);
        builder.add(new CharsRef("clergy"), new CharsRef("job"), true);
        builder.add(new CharsRef("violence"), new CharsRef("harm,death,threat"), true);
        builder.add(new CharsRef("antibiotic"), new CharsRef("pharmaceutical,drug,medicine"), true);
        builder.add(new CharsRef("king"), new CharsRef("emporer"), true);
        SynonymMap map = null;
        try {
            map = builder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = new SynonymGraphFilter(result, map, true);
        result = new StopFilter(result, EnglishAnalyzer.ENGLISH_STOP_WORDS_SET);
        // removes tokens having lengths less than 5
        result = new LengthFilter(result, 4, 20);
        result = new EnglishMinimalStemFilter(result);

        return new TokenStreamComponents(source, result);
    }
}

