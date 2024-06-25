public class Teller implements Runnable {
    private final BankQueue bankQueue;
    private final int id;
    private final QueueSimulator queueSimulator;

    public Teller(BankQueue bankQueue, int id, QueueSimulator queueSimulator) {
        this.bankQueue = bankQueue;
        this.id = id;
        this.queueSimulator = queueSimulator;
    }

    @Override
    public void run() {
        System.out.printf("Teller %d is starting!\n", id);
        while (true) {
            Customer customer = bankQueue.getCustomerForService();
            if (customer == null) {
                break; // Stop if the queue manager has stopped
            }
            // System.out.printf("Teller %d is taking new customer with service time : %d, arrival time : %d\n", id, customer.getServiceTime(), customer.getArrivalTime());
            if (!customer.isDeparted()) {
                customer.setServedTime(System.currentTimeMillis());
                customer.setServed(true);
                try {
                    Thread.sleep(customer.getServiceTime() * 1000); // Simulate service time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                // System.out.printf("Teller %d ends work\n", id);
            }
        }
        System.out.printf("Teller %d ends its works!\n", id);
    }
}
