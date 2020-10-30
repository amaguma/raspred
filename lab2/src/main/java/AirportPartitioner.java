import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportPartitioner extends Partitioner<AirportWritable, Text> {
    @Override
    public int gerPartition(AirportWritable key, Text value, int numReuceTasks) {
        return Integer.hashCode(key.getId() & Integer.MAX_VALUE) % numReuceTasks;
    }
}
