package d2sparser;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.util.Generics;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dawnwords on 2015/1/18.
 */
public class StanfordNLPD2SParser implements D2SParser {

    private static final String[] BLOCK_STRING = {"-lrb-", "-lsb-", "-lcb-", "-rrb-", "-rsb-", "-rcb-"};
    private static final Set<String> block = Generics.newHashSet(Arrays.asList(BLOCK_STRING));

    @Override
    public List<List<String>> toSentence(String docFile) {
        DocumentPreprocessor dp = new DocumentPreprocessor(docFile);
        List<List<String>> result = new LinkedList<List<String>>();
        for (List<HasWord> sentence : dp) {
            List<String> s = null;
            for (HasWord token : sentence) {
                String word = token.word().toLowerCase();
                if (!block.contains(word)) {
                    String[] words = word.split("[^A-Za-z]");
                    for (String w : words) {
                        if (!"".equals(w)) {
                            if (s == null) {
                                s = new LinkedList<String>();
                            }
                            s.add(w);
                        }
                    }
                }
            }
            if (s != null) {
                result.add(s);
            }
        }
        return result;
    }
}
