package amaguma;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Map;

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

        JavaPairRDD<String, Airport>  airportsData = airports
                .mapToPair(str -> {
                    int ind = str.indexOf(",");
                    String airportId = str.substring(0, ind);
                    String name = str.substring(ind + 1);
                    return new Tuple2<>(airportId, new Airport(name, Integer.parseInt(airportId)));
                });
        final Broadcast<Map<String, Airport>> broadcast = sc.broadcast(airportsData.collectAsMap());

        JavaPairRDD<Tuple2<String, String>, Flight> flightsData = flights
                .map()
    }
}
