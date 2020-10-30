import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


import java.io.DataOutput;
import java.io.IOException;

public class AirportWritable implements Writable, WritableComparable<AirportWritable> {

    private int id;
    private int indicator;


    public AirportWritable () {}

    public AirportWritable(int id, int indicator) {
        this.id = id;
        this.indicator = indicator;
    }

    public void write(DataOutput dataOutput) throws IOException {

    }
}
