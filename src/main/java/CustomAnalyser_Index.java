import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.Arrays;
import java.util.List;

public class CustomAnalyser_Index extends Analyzer {
    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        return super.normalize(fieldName, in);
    }

    @Override
    public int getOffsetGap(String fieldName) {
        return super.getOffsetGap(fieldName);
    }

    @Override
    public int getPositionIncrementGap(String fieldName) {
        // increases the position of tokens between different fields
        // could be helpful in carrying out field specific queries
        return 5;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldname) {
        StandardTokenizer source = new StandardTokenizer();
        TokenStream result = new ClassicFilter(source);
        result = new LowerCaseFilter(result);
        List<String> stopWordList = Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "as", "at", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "could", "did", "do", "does", "doing", "down", "during", "each", "few", "for", "from", "further", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "same", "she", "she'd", "she'll", "she's", "should", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves");
        CharArraySet stopWordSet = new CharArraySet(stopWordList, true);
        result = new StopFilter(result,stopWordSet);
        // removes tokens having lengths less than 5
        result = new LengthFilter(result, 5, 20);
        result = new EnglishMinimalStemFilter(result);

        return new TokenStreamComponents(source, result);
    }
}

