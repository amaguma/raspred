package amaguma;

import org.apache.spark.SparkConf;

public class Application {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(-1);
        }

        SparkConf conf = new SparkConf().setAppName("lab3");
    }
}
