import d2sparser.D2SParser;
import d2sparser.StanfordNLPD2SParser;

import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        final String dataPath = ".." + File.separator + "20news-18828" + File.separator;
        final String IBM_HARDWARE = dataPath + "comp.sys.ibm.pc.hardware";
        final String MAC_HARDWARE = dataPath + "comp.sys.mac.hardware";
        final String BASEBALL = dataPath + "rec.sport.baseball";
        final String HOCKEY = dataPath + "rec.sport.hockey";
        final String MED = dataPath + "sci.med";
        final String SPACE = dataPath + "sci.space";
        final String ATHEISM = dataPath + "alt.atheism";
        final String CHRISTIAN = dataPath + "soc.religion.christian";


        D2SParser parser = new StanfordNLPD2SParser();
        process("hardware", IBM_HARDWARE, MAC_HARDWARE, 0.8, parser);
        process("sport", BASEBALL, HOCKEY, 0.8, parser);
        process("science", MED, SPACE, 0.8, parser);
        process("religion", ATHEISM, CHRISTIAN, 0.8, parser);
    }

    static void process(String topic, String class1, String class0, double trainTestRatio, D2SParser parser) {
        File[] filesClass1 = new File(class1).listFiles();
        File[] filesClass0 = new File(class0).listFiles();

        int class1Size = filesClass1.length;
        int class0Size = filesClass0.length;
        int trainSizeClass1 = (int) (class1Size * trainTestRatio);
        int trainSizeClass0 = (int) (class0Size * trainTestRatio);

        DocProcessor processor = new DocProcessor("out", topic, parser);
        for (int i = 0; i < trainSizeClass1; i++) {
            processor.addTrainFile(filesClass1[i].getAbsolutePath(), 1);
        }
        for (int i = 0; i < trainSizeClass0; i++) {
            processor.addTrainFile(filesClass0[i].getAbsolutePath(), 0);
        }
        for (int i = trainSizeClass1; i < class1Size; i++) {
            processor.addTestFile(filesClass1[i].getAbsolutePath(), 1);
        }
        for (int i = trainSizeClass0; i < class0Size; i++) {
            processor.addTestFile(filesClass0[i].getAbsolutePath(), 0);
        }
    }
}
