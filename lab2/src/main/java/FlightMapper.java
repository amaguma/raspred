import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FlightMapper extends Mapper<LongWritable, Text, AirportMapper, Text> {
    private static final int ID = 14;
    private static final int AIRPORT_DELAY = 18;
}
