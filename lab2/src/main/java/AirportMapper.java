import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {

    AirportWritable awKey = new AirportWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            int id = Integer.parseInt(columns[0].replaceAll("\"",""));
            awKey.setId(id);
            awKey.setIndicator(0);
            String name = columns[1].replaceAll("\"","");
            context.write(awKey, new Text(name));
        }
    }
}
