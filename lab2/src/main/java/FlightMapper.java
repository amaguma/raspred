import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            int id = Integer.parseInt(columns[14]);
            double delay = Double.parseDouble(columns[18]);
            if (delay == 0) {
                return;
            }
            context.write(new AirportWritable(id, 1), new Text(columns[18]));
        }
    }
}
