package amaguma;

import org.apache.hadoop.io.Text;

public class Tools {

    public static String[] separationComma(Text value) {
        return value.toString().split(",");
    }

    public static String replaceQuote(String str) {
        return str.replaceAll("\"", "");
    }
}
