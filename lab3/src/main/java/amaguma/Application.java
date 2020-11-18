package amaguma;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Iterator;
import java.util.Map;

public class Application {

    public static JavaRDD<String> removeHeader(JavaRDD<String> file) {
        return file.filter(str -> str.equals(file.first()));
    }

    public static JavaRDD<String> removeQuotes(JavaRDD<String> file) {
        return file.map(str -> str.replaceAll("\"", ""));
    }

    public static double getPersen

    public static void main(String[] args) {
        if (args.length != 3) {
            System.exit(-1);
        }

        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> flights = removeQuotes(removeHeader(sc.textFile(args[0])));
        JavaRDD<String> airports = removeQuotes(removeHeader(sc.textFile(args[1])));



        JavaPairRDD<String, Airport>  airportsData = airports
                .mapToPair(str -> {
                    int ind = str.indexOf(",");
                    String airportId = str.substring(0, ind);
                    String name = str.substring(ind + 1);
                    return new Tuple2<>(airportId, new Airport(name, Integer.parseInt(airportId)));
                });
        final Broadcast<Map<String, Airport>> broadcast = sc.broadcast(airportsData.collectAsMap());

        JavaPairRDD<Tuple2<String, String>, Flight> flightsData = flights
                .map(str -> str.split(","))
                .mapToPair(str -> {
                   String departureId = str[11];
                   String destinationId = str[14];
                   double delay = str[18].isEmpty() ? 0 : Double.parseDouble(str[18]);
                   boolean isCancelled = str[19].isEmpty();
                   return new Tuple2<>(new Tuple2<>(departureId, destinationId),
                           new Flight(departureId, destinationId, delay, isCancelled));
                });
        flightsData
                .groupByKey()
                .mapValues(item -> {
                    Iterator<Flight> iterator = item.iterator();
                    double maxDelay = Double.MIN_VALUE;

                });
    }
}
