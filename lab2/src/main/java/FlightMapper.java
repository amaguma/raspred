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
            if (!checkEmptiness(columns[AIRPORT_ID], columns[DELAY])) {
                int id = Integer.parseInt(columns[AIRPORT_ID]);
                double delayTime = Double.parseDouble(columns[DELAY]);
                if (delayTime != 0) {
                    context.write(new AirportWritable(id, AirportWritable.Indicator.FLIGHT), new Text(columns[DELAY]));
                }
            }
        }
    }

    private boolean checkEmptiness(String strId, String strDelay) {
        if (strId.equals("") && strDelay.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
