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

    private static final int DEPARTURE_AIRPORT_ID = 11;
    private static final int DESTINATION_AIRPORT_ID = 14;
    private static final int TIME_DELAY = 18;
    private static final int ZERO_DELAY = 0;
    private static final int IS_CANCELLED_ID = 19;
    private static final int ID = 0;
    private static final int NAME = 1;
    private static final String DELIMITER = ",";
    private static final String QUOTE = "\"";
    private static final String RESULT_PATH = "output";

    public static JavaRDD<String> removeHeader(JavaRDD<String> file){
        String firstline = file.first();
        return file.filter(str -> !str.equals(firstline));
    }

    public static String removeQuotes(String str) {
        return str.replace(QUOTE, "");
    }

    public static double getPersentage (double piece, double whole) {
        return piece / whole * 100;
    }

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("lab3");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> airports = removeHeader(sc.textFile(args[0]));
        JavaRDD<String> flights = removeHeader(sc.textFile(args[1]));


        JavaPairRDD<String, String>  airportsData = airports
                .mapToPair(str -> {
                    String[] columns = str.split(DELIMITER);
                    String airportId = removeQuotes(columns[ID]);
                    String name = removeQuotes(columns[NAME]);
                    return new Tuple2<>(airportId, name);
                });
        final Broadcast<Map<String, String>> broadcast = sc.broadcast(airportsData.collectAsMap());

        JavaPairRDD<Tuple2<String, String>, Flight> flightsData = flights
                .mapToPair(str -> {
                    String[] columns = str.split(DELIMITER);
                    String departureId = columns[DEPARTURE_AIRPORT_ID];
                    String destinationId = columns[DESTINATION_AIRPORT_ID];
                    double delay = columns[TIME_DELAY].isEmpty() ? ZERO_DELAY : Double.parseDouble(columns[TIME_DELAY]);
                    boolean isCancelled = columns[IS_CANCELLED_ID].isEmpty();
                    return new Tuple2<>(new Tuple2<>(departureId, destinationId),
                            new Flight(departureId, destinationId, delay, isCancelled));
                });
        flightsData
                .groupByKey()
                .mapValues(item -> {
                    Iterator<Flight> iterator = item.iterator();
                    double maxDelay = 0;
                    double cancelledFlights = 0;
                    double delayedFlights = 0;
                    double countFlights = 0;
                    while (iterator.hasNext()) {
                        Flight flight = iterator.next();
                        if (flight.getDelay() > 0) {
                            delayedFlights++;
                            maxDelay = Double.max(maxDelay, flight.getDelay());
                        }
                        if (flight.isCancelled()) {
                            cancelledFlights++;
                        }
                        countFlights++;
                    }
                    String output = "maxDelay: " + maxDelay + "\t\tpercentage of late + canceled flights: " + getPersentage(delayedFlights + cancelledFlights, countFlights) + "%";
                    return output;
                })
                .map(item -> {
                    String departureAirportName = broadcast.value().get(item._1._1);
                    String destinationAirportName = broadcast.value().get(item._1._2);
                    String output = departureAirportName + "\t->\t" + destinationAirportName + "\t" + item._2;
                    return output;
                })
                .saveAsTextFile(RESULT_PATH);
    }
}

