package amaguma;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {
    private static final int AIRPORT_ID = 14;
    private static final int DELAY = 18;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = Tools.separationComma(value);
        if (key.get() > 0) {
            if (!columns[AIRPORT_ID].isEmpty() && !columns[DELAY].isEmpty()) {
                int id = Integer.parseInt(columns[AIRPORT_ID]);
                double timeDelay = Double.parseDouble(columns[DELAY]);
                if (timeDelay != 0) {
                    context.write(new AirportWritable(id, AirportWritable.Indicator.FLIGHT), new Text(columns[DELAY]));
                }
            }
        }
    }
}
