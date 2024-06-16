import java.util.concurrent.ThreadLocalRandom;

public class Customer {
    private final int arrivalTime;
    private final int serviceTime;
    private boolean served;
    private boolean departed;

    public Customer(int arrivalTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = ThreadLocalRandom.current().nextInt(60, 301);
        this.served = false;
        this.departed = false;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public boolean isDeparted() {
        return departed;
    }

    public void setDeparted(boolean departed) {
        this.departed = departed;
    }
}
