import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class QueueSimulator {
    private final BankQueue bankQueue;
    private final int simulationTime; // in seconds
    private final List<Customer> customers;
    private int totalCustomersArrived;
    private int totalCustomersDeparted;
    private int totalCustomersServed;
    private int totalServiceTime;

    public QueueSimulator(int numTellers, int maxQueueLength, int simulationMinutes) {
        this.bankQueue = new BankQueue(maxQueueLength);
        this.simulationTime = simulationMinutes * 60; // convert minutes to seconds
        this.customers = new ArrayList<>();
        this.totalCustomersArrived = 0;
        this.totalCustomersDeparted = 0;
        this.totalCustomersServed = 0;
        this.totalServiceTime = 0;

        // Start teller threads
        for (int i = 0; i < numTellers; i++) {
            new Thread(new Teller(bankQueue, i, this)).start();
        }
    }

    public void simulate() {
        Thread queueThread = new Thread(bankQueue);
        queueThread.start();

        int currentTime = 0;

        while (currentTime < simulationTime) {
            // New customer arrival
            if (currentTime % ThreadLocalRandom.current().nextInt(20, 61) == 0) {
                Customer customer = new Customer(currentTime);
                customers.add(customer);
                totalCustomersArrived++;
                if (!bankQueue.addCustomer(customer)) {
                    totalCustomersDeparted++;
                }
            }

            // Increment time
            currentTime++;
            try {
                Thread.sleep(1000); // Each tick represents one second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Stop the queue manager and wait for its thread to finish
        bankQueue.stop();
        try {
            queueThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void customerServed(int serviceTime) {
        totalCustomersServed++;
        totalServiceTime += serviceTime;
    }

    public void printStatistics() {
        double averageServiceTime = totalCustomersServed > 0 ? (double) totalServiceTime / totalCustomersServed : 0;
        System.out.println("Total customers arrived: " + totalCustomersArrived);
        System.out.println("Total customers departed without being served: " + totalCustomersDeparted);
        System.out.println("Total customers served: " + totalCustomersServed);
        System.out.println("Average service time per customer: " + averageServiceTime + " seconds");
    }

    public static void main(String[] args) {
        int numTellers = 3;
        int maxQueueLength = 5;
        int simulationMinutes = 1; // 2 hours

        QueueSimulator simulator = new QueueSimulator(numTellers, maxQueueLength, simulationMinutes);
        simulator.simulate();
        simulator.printStatistics();
    }
}
