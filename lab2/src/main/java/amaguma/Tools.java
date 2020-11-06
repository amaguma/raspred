package amaguma;

import org.apache.hadoop.io.Text;

public class Tools {

    public static String[] separatedComma(Text value) {
        return value.toString().split(",");
    }

    public static boolean checkEmptiness(String strId, String strDelay) {
        if (strId.equals("") && strDelay.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static String replaceSlashes(String str) {
        return str.replaceAll("\"", "");
    }
}
