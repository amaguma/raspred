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

    @Override
    public int compareTo(AirportWritable obj) {
        if (this.id - obj.id != 0) {
            return this.id - obj.id;
        }
        if (indicator == 1) {
            if (obj.indicator == 0) {
                return 1;
            }
        } else {
            if (obj.indicator == 1) {
                return -1;
            }
        }
        return 0;
    }
}
