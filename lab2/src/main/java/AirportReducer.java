import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class AirportReducer extends Reducer<AirportWritable, Text, IntWritable, Text> {
    @Override
    protected void reduce(AirportWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iterator = values.iterator();
        String airportName = iterator.next().toString();
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double count = 0;
        double sum = 0;
        while(iterator.hasNext()) {
            double delay = Double.parseDouble(iterator.next().toString());
            
        }
    }
}
