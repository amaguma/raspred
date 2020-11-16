package amaguma;

public class Flight {
    private String departureId;
    private String destinationId;
    private double delay;
    private boolean isCancelled;

    public Flight() {};

    public Flight(String departureId, String destinationId, double delay, boolean isCancelled) {
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

    
}
