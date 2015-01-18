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
        process("hardware", IBM_HARDWARE, MAC_HARDWARE, parser);
        process("sport", BASEBALL, HOCKEY, parser);
        process("science", MED, SPACE, parser);
        process("religion", ATHEISM, CHRISTIAN, parser);
    }

    static void process(String topic, String class1, String class2, D2SParser parser) {
        DocProcessor processor = new DocProcessor("out", topic, parser);
        processor.execute();
        File[] filesClass1 = new File(class1).listFiles();
        File[] filesClass2 = new File(class2).listFiles();
        int i;
        for (i = 0; i < filesClass1.length && i < filesClass2.length; i++) {
            processor.addFile(filesClass1[i].getAbsolutePath(), 1);
            processor.addFile(filesClass2[i].getAbsolutePath(), 0);
        }
        for (; i < filesClass1.length; i++) {
            processor.addFile(filesClass1[i].getAbsolutePath(), 1);
        }
        for (; i < filesClass2.length; i++) {
            processor.addFile(filesClass2[i].getAbsolutePath(), 0);
        }
    }
}
