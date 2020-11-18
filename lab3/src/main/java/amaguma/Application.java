package amaguma;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class Application {

    public static JavaRDD<String> removeHeader(JavaRDD<String> file) {
        return file.filter(str -> str.equals(file.first()));
    }

    public static JavaRDD<String> removeQuotes(JavaRDD<String> file) {
        return file.map(str -> str.replaceAll("\"", ""));
    }

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(-1);
        }

        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flights = sc.textFile(args[0]);
        JavaRDD<String> airports = sc.textFile(args[1]);
        removeHeader(flights);
        removeHeader(airports);

        JavaPairRDD<String, Airport>  airportData = airports
                .map(s -> s.split(","))
                .mapToPair(s -> {
                    
                });
    }
}
