import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;


import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class AirportWritable implements Writable, WritableComparable<AirportWritable> {

    private int id;
    private Indicator type;


    public AirportWritable () {}

    public AirportWritable(int id, Indicator type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(id);
        dataOutput.writeInt(type == Indicator.FLIGHT ? 1 : 0);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        id = dataInput.readInt();
        type = dataInput.readInt() == 1 ? Indicator.FLIGHT : Indicator.AIRPORT;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(AirportWritable obj) {
        if (this.id - obj.id != 0) {
            return this.id - obj.id;
        }
        if (type == 1) {
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

    public enum Indicator {
        AIRPORT,
        FLIGHT
    }
}


