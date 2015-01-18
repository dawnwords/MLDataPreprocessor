package d2sparser;

import java.util.List;

/**
 * Created by Dawnwords on 2015/1/18.
 */
public interface D2SParser {
    /**
     * Parse the given document into a list of
     * word list of each sentences in the document
     *
     * @param docFile given document file path
     * @return a list of word list of each sentences for docFile
     */
    List<List<String>> toSentence(String docFile);
}
