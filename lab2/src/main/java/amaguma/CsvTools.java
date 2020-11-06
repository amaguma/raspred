package amaguma;

import org.apache.hadoop.io.Text;

public class CsvTools {

    private String[] separatedComma(Text value) {
        return value.toString().split(",");
    }
}
