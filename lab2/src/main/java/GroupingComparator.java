import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupingComparator extends WritableComparator {

    protected GroupingComparator() {
        super(AirportWritable.class, true);
    }

    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        return 
    }

}
