import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = value.toString().split(",");
        if (key.get() > 0) {
            int id = Integer.parseInt(columns[0].replaceAll("\"",""));
            String name = columns[1].replaceAll("\"","");
            context.write(new AirportWritable(id, AirportWritable.Indicator.AIRPORT), new Text(name));
        }
    }

    private void replaceSlashes(String regex, )
}
