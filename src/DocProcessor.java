import d2sparser.D2SParser;
import io.OutputWriter;
import io.WriterEnum;

import java.io.File;
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
    private BlockingQueue<String> docQueue;
    private String outputPath;
    private HashMap<String, Integer> vocabulary;
    private OutputWriter writer;
    private D2SParser d2SParser;
    private int wordCount;

    public DocProcessor(String outputPath, String docTopic, D2SParser d2SParser) {
        this.docTopic = docTopic;
        this.outputPath = outputPath;
        this.docQueue = new LinkedBlockingQueue<String>();
        this.vocabulary = new HashMap<String, Integer>();
        this.writer = new OutputWriter(outputPath, docTopic);
        this.d2SParser = d2SParser;
        this.wordCount = 1;
    }

    public void addFile(String filePath, int label) {
        writer.println(WriterEnum.L, label);
        docQueue.add(filePath);
    }

    public void execute() {
        new Thread() {
            @Override
            public void run() {
                try {
                    String filePath = docQueue.poll(1, TimeUnit.SECONDS);
                    if (filePath != null) {
                        System.out.println("Process " + filePath);
                        processDoc(outputPath + File.separator + docTopic + File.separator + filePath);
                        System.out.println("Finish " + filePath);
                        execute();
                    } else {
                        writer.close();
                        System.out.println("Done");
                    }
                } catch (InterruptedException e) {
                    System.err.println(docTopic + " Interrupted");
                }
            }
        }.start();
    }

    private void processDoc(String docPath) {
        List<List<String>> sentences = d2SParser.toSentence(docPath);
        for (List<String> sentence : sentences) {
            int start = wordCount;
            for (String word : sentence) {
                Integer wordIndex = vocabulary.get(word);
                if (wordIndex == null) {
                    vocabulary.put(word, wordCount);
                    wordIndex = wordCount++;
                }
                writer.println(WriterEnum.M, wordIndex);
            }
            writer.println(WriterEnum.D, start, wordCount - 1);
        }
        writer.println(WriterEnum.D, 0, 0);
    }

}
