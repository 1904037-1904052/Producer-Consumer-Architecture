import Producer.QueueSimulator;

public class QueueSystem {
    public static void main(String[] args) {
        int numTellers = 4;
        int maxQueueLength = 5;
        int numCashiers = 3; 
        int maxGroceryQueueLength = 2;
        int simulationMinutes = 1; // 2 hours

        QueueSimulator simulator = new QueueSimulator(numTellers, maxQueueLength, numCashiers, maxGroceryQueueLength, simulationMinutes);
        simulator.simulate();
        simulator.printStatistics();
    }
}
