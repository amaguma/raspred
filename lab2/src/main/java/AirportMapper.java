import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {
    private static final int AIRPORT_ID = 0;
    private static final int DELAY = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            int id = Integer.parseInt(replaceSlashes(columns[0], "\"", ""));
            String name = replaceSlashes(columns[1], "\"","");
            context.write(new AirportWritable(id, AirportWritable.Indicator.AIRPORT), new Text(name));
        }
    }

    private String replaceSlashes(String str, String regex, String replacement) {
        return str.replaceAll(regex, replacement);
    }
}
