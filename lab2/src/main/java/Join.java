import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class Join {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.exit(-1);
        }
        Job job = Job.getInstance();
        job.setJarByClass(Join.class);
        job.setJobName("Airport join");
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, AirportMapper.class);
        MultipleInputs.addInputPath(job, new Path(args[1]), TextInputFormat.class, FlightMapper.class);

        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        job.setPartitionerClass(AirportPartitioner.class);
        job.setGroupingComparatorClass(GroupingComparator.class);
        job.setReducerClass(AirportReducer.class);
        job.setMapOutputKeyClass(AirportWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.

    }
}
