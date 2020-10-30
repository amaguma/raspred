import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


import java.io.DataInput;
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

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeInt(indicator);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        id = dataInput.readInt();
        indicator = dataInput.readInt();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndicator() {
        return indicator;
    }

    public void setIndicator(int indicator) {
        this.indicator = indicator;
    }

    @Override
    public int compareTo(AirportWritable obj) {
        int result = id.compareTo(obj.id)
    }
}
