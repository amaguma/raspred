package lab7;

import org.zeromq.ZFrame;

public class Config {

    private ZFrame address;
    private String id;
    private long time;
    private int min;
    private int max;

    public Config(ZFrame address, String id, long time, int min, int max) {
        this.address = address;
        this.id = id;
        this.time = time;
        this.min = min;
        this.max = max;
    }

    public void setAddress(ZFrame address) {
        this.address = address;
    }

    public ZFrame getAddress() {
        return this.address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return this.time;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMin() {
        return this.min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMax() {
        return this.max;
    }

    public boolean isNotAlive() {
        return this.time + 2 * Tools.TIMEOUT < System.currentTimeMillis();
    }
}
