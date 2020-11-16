package amaguma;

import scala.Serializable;

public class Airport implements Serializable {
    private String departureId;
    private String destinationId;
    private double delay;
    private boolean isCancelled;

    public Airport() {}

    public Airport(String departureId, String destinationId, double delay, boolean isCancelled) {
        this.departureId = departureId;
        this.destinationId = destinationId;
        this.delay = delay;
        this.isCancelled = isCancelled;
    }

    public String getDepartureId() {
        return this.departureId;
    }

    public String getDestinationId() {
        return this.destinationId;
    }

    public double getDelay() {
        return this.delay;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }
}
