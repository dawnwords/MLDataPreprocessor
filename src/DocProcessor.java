import d2sparser.D2SParser;
import io.OutputWriter;
import io.WriterEnum;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dawnwords on 2015/1/18.
 */
public class DocProcessor {

    private String docTopic;
    private BlockingQueue<FileTrainPair> docQueue;
    private HashMap<String, Integer> vocabulary;
    private OutputWriter writer;
    private D2SParser d2SParser;
    private int wordCount;

    public DocProcessor(String outputPath, String docTopic, D2SParser d2SParser) {
        this.docTopic = docTopic;
        this.docQueue = new LinkedBlockingQueue<FileTrainPair>();
        this.vocabulary = new HashMap<String, Integer>();
        this.writer = new OutputWriter(outputPath, docTopic);
        this.d2SParser = d2SParser;
        this.wordCount = 1;

        execute();
    }

    private void execute() {
        new Thread() {
            @Override
            public void run() {
                try {
                    FileTrainPair file = docQueue.poll(1, TimeUnit.SECONDS);
                    if (file != null) {
                        if (file.isTrain) {
                            processTrainDoc(file.file);
                        } else {
                            processTestFile(file.file);
                        }
                        execute();
                    } else {
                        writer.close();
                        System.out.println(docTopic + " Done: vocabulary size=" + vocabulary.size());
                    }
                } catch (InterruptedException e) {
                    System.err.println(docTopic + " Interrupted");
                }
            }
        }.start();
    }

    public void addTrainFile(String filePath, int label) {
        writer.println(WriterEnum.L, label);
        docQueue.add(new FileTrainPair(filePath, true));
    }

    public void addTestFile(String filePath, int label) {
        writer.println(WriterEnum.Y, label);
        docQueue.add(new FileTrainPair(filePath, false));
    }

    private void processTrainDoc(String docPath) {
        List<List<String>> sentences = d2SParser.toSentence(docPath);
        for (List<String> sentence : sentences) {
            int start = wordCount;
            for (String word : sentence) {
                Integer wordIndex = vocabulary.get(word);
                if (wordIndex == null) {
                    wordIndex = vocabulary.size() + 1;
                    vocabulary.put(word, wordIndex);
                }
                writer.println(WriterEnum.M, wordIndex);
                wordCount++;
            }
            writer.println(WriterEnum.D, start, wordCount - 1);
        }
        writer.println(WriterEnum.D, 0, 0);
    }

    private void processTestFile(String docPath) {
        List<List<String>> sentences = d2SParser.toSentence(docPath);
        for (List<String> sentence : sentences) {
            for (String word : sentence) {
                Integer wordIndex = vocabulary.get(word);
                if (wordIndex != null) {
                    writer.println(WriterEnum.X, wordIndex);
                }
            }
        }
    }

    private class FileTrainPair {
        String file;
        boolean isTrain;

        public FileTrainPair(String file, boolean isTrain) {
            this.file = file;
            this.isTrain = isTrain;
        }
    }

}
