package io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by Dawnwords on 2015/1/18.
 */
public class OutputWriter {
    private PrintWriter[] writers;

    public OutputWriter(String outputPath, String docTopic) {
        outputPath = outputPath + File.separator + docTopic;
        createOutputDir(outputPath);
        initWriters(outputPath, docTopic);
    }

    public void close() {
        for (PrintWriter writer : writers) {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }

    public void println(WriterEnum writerEnum, int i) {
        printf(writerEnum, "%d\n", i);
    }

    public void println(WriterEnum writerEnum, int i, int j) {
        printf(writerEnum, "%d %d\n", i, j);
    }

    private void printf(WriterEnum writerEnum, String format, Object... args) {
        PrintWriter writer = writers[writerEnum.ordinal()];
        if (writer != null) {
            writer.printf(format, args);
        } else {
            System.err.printf("Writer%s is Null", writerEnum.name());
        }
    }

    private void initWriters(String outputPath, String docTopic) {
        this.writers = new PrintWriter[WriterEnum.values().length];
        for (WriterEnum writer : WriterEnum.values()) {
            try {
                String outFile = outputPath + File.separator + writer.name() + docTopic;
                writers[writer.ordinal()] = new PrintWriter(new FileOutputStream(outFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void createOutputDir(String outputPath) {
        File outputDir = new File(outputPath);
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }
    }
}
