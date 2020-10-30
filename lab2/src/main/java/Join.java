import org.apache.hadoop.mapreduce.Job;

public class Join {
    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.exit(-1);
        }
        Job job = Job.getInstance();
        job.setJarByClass(Join.class);
        job.setJobName("Airport join");
        
    }
}
