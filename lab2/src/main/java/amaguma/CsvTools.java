package amaguma;

import org.apache.hadoop.io.Text;

public class CsvTools {

    public static String[] separatedComma(Text value) {
        return value.toString().split(",");
    }
}
