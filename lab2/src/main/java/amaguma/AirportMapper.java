package amaguma;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class AirportMapper extends Mapper<LongWritable, Text, AirportWritable, Text> {
    private static final int AIRPORT_ID = 0;
    private static final int DESCRIPTION = 1;

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] columns = CsvTools.separatedComma(value);
        if (key.get() > 0) {
            int id = Integer.parseInt(CsvTools.replaceSlashes(columns[AIRPORT_ID]));
            String name = CsvTools.replaceSlashes(columns[DESCRIPTION]);
            context.write(new AirportWritable(id, AirportWritable.Indicator.AIRPORT), new Text(name));
        }
    }
}
