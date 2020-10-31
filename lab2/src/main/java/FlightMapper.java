import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {
    private static final int AIRPORT_ID = 14;
    private static final int DELAY = 18;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            if (columns[DELAY].trim().length() != 0) {
                int id = Integer.parseInt(columns[AIRPORT_ID]);
                context.write(new AirportWritable(id, 1), new Text(columns[DELAY]));
            }
        }
    }
}
