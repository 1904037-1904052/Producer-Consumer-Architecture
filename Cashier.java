public class Cashier implements Runnable {
    private final GroceryQueue groceryQueue;
    private final int id;
    private final QueueSimulator queueSimulator;

    public Cashier(GroceryQueue groceryQueue, int id, QueueSimulator queueSimulator) {
        this.groceryQueue = groceryQueue;
        this.id = id;
        this.queueSimulator = queueSimulator;
    }

    @Override
    public void run() {
        System.out.printf("Cashier %d starts working.\n", id);
        while (true) {
            Customer customer = groceryQueue.getCustomerForService();
            if (customer == null) {
                break; // Stop if the queue manager has stopped
            }
            if (!customer.isDeparted()) {
                System.out.printf("Cashier %d is taking new customer with service time : %d, arrival time : %d\n", id, customer.getServiceTime(), customer.getArrivalTime());
                customer.setServed(true);
                queueSimulator.GrocerycustomerServed(customer.getServiceTime());
                try {
                    Thread.sleep(customer.getServiceTime() * 10L); // Simulate service time
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.printf("Cashier %d ends work\n", id);
            }
        }
        System.out.printf("Cashier %d end wokring!\n", id);
    }
}
