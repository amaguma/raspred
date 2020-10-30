import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

public class AirportWritable implements Writable, WritableComparable<AirportWritable> {

    private int id;
    private int indicator;


    public AirportWritable () {}

    public AirportWritable(int id, int indicator) {
        this.id = id;
        this.indicator = indicator;
    }

    
}
