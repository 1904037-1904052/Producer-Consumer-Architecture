package Producer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import Consumer.Teller;
import Model.Customer;
import Queue.BankQueue;
import Queue.GroceryQueues;

public class QueueSimulator {
    private final BankQueue bankQueue;
    private final GroceryQueues groceryQueues;
    private final int simulationTime; // in seconds
    private final List<Customer> BankCustomers, GroceryCustomer;
    private int totalCustomersArrived;
    private int[] totalCustomersDeparted = new int[2];
    private int[] totalCustomersServed = new int[2];
    private int[] totalServiceTime = new int[2];
    private int numTellers;
    Thread[] tellers;

    public QueueSimulator(int numTellers, int maxBankQueueLength, int numCashiers, int maxGroceryQueueLength, int simulationMinutes) {
        this.bankQueue = new BankQueue(maxBankQueueLength);
        this.groceryQueues = new GroceryQueues(numCashiers, maxGroceryQueueLength, this);
        this.numTellers = numTellers;
        this.simulationTime = simulationMinutes * 60; // convert minutes to seconds
        this.BankCustomers = new ArrayList<>();
        this.GroceryCustomer = new ArrayList<>();
        this.totalCustomersArrived = 0;
        for(int i = 0; i < 2; i++) {
            this.totalCustomersDeparted[i] = 0;
            this.totalCustomersServed[i] = 0;
            this.totalServiceTime[i] = 0;
        }

        // Start teller threads
        tellers = new Thread[numTellers];
        for (int i = 0; i < numTellers; i++) {
            tellers[i] = new Thread(new Teller(bankQueue, i));
            tellers[i].start();
        }
        // Start Cashier threads
        // Cashier = new Thread[numCashiers];
        // for(int i = 0; i < numCashiers; i++) {
        //     Cashier[i] = new Thread(new Cashier(groceryQueues.getQueue(i), i, this));
        //     Cashier[i].start();
        // }
    }

    public void simulate() {
        Thread bankQueueThread = new Thread(bankQueue);
        bankQueueThread.start();
        groceryQueues.start();

        int currentTime = 0;

        while (currentTime < simulationTime) {
            // New customer arrival
            if (currentTime % ThreadLocalRandom.current().nextInt(2, 6) == 0) {
                Customer customer = new Customer(System.currentTimeMillis());
                BankCustomers.add(customer);
                Customer copyOfCustomer = new Customer(customer);
                GroceryCustomer.add(copyOfCustomer);

                totalCustomersArrived++;
                if (!bankQueue.addCustomer(customer)) {
                    customer.setDeparted(true);
                }
                groceryQueues.addCustomer(copyOfCustomer);


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
        groceryQueues.stopAllCashiers();
        try {
            for(int i = 0; i < numTellers; i++) {
                tellers[i].join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void BankcustomerCalculate() {
        for(Customer customer : BankCustomers) {
            if(customer.isDeparted()) totalCustomersDeparted[0]++;
            else if(customer.isServed()) {
                totalCustomersServed[0]++;
                totalServiceTime[0] += customer.getServedTime() - customer.getArrivalTime();
            }
        }
    }
    public synchronized void GrocerycustomerCalculate() {
        for(Customer customer : GroceryCustomer) {
            if(customer.isDeparted()) totalCustomersDeparted[1]++;
            else if(customer.isServed()) {
                totalCustomersServed[1]++;
                totalServiceTime[1] += customer.getServedTime() - customer.getArrivalTime();
            }
        }
    }

    public void printStatistics() {
        double[] averageServiceTime = new double[2];
        GrocerycustomerCalculate();
        BankcustomerCalculate();
        for(int i = 0; i < 2; i++) {
            averageServiceTime[i] = totalCustomersServed[i] > 0 ? (double) totalServiceTime[i] / totalCustomersServed[i] : 0;
            averageServiceTime[i] /= 1000.0;
            System.out.println("Total customers arrived: " + totalCustomersArrived);
            System.out.println("Total customers departed without being served: " + totalCustomersDeparted[i]);
            System.out.println("Total customers served: " + totalCustomersServed[i]);
            System.out.println("Average amount of time taken to serve each customer: " + averageServiceTime[i] + " seconds");
        }
    }

    
}
