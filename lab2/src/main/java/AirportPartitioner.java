import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class AirportPartitioner extends Partitioner<AirportWritable, Text>{
    @Override
    public int getPartition(AirportWritable key, Text value, int numReduceTasks) {
        return Integer.hashCode(key.getId() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
