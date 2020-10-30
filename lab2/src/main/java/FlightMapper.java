import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlightMapper extends Mapper<LongWritable, Text, AirportMapper, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            int id = Integer.parseInt(columns[14]);
            float delay = Float.parseFloat(columns[18]);
            if (delay == 0) {
                return;
            }
            context.write(new AirportMapper(id, ));
        }
    }
}
