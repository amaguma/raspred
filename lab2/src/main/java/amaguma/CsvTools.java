package amaguma;

import org.apache.hadoop.io.Text;

public class CsvTools {

    public static String[] separatedComma(Text value) {
        return value.toString().split(",");
    }

    public  boolean checkEmptiness(String strId, String strDelay) {
        if (strId.equals("") && strDelay.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
