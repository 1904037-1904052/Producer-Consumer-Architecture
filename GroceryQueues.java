import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GroceryQueues {
    private final GroceryQueue[] queues;
    private final Lock lock;
    private Thread[] Queuethread;
    int numQueues;

    public GroceryQueues(int numQueues, int maxQueueLength) {
        this.numQueues = numQueues;
        this.queues = new GroceryQueue[numQueues];
        this.Queuethread = new Thread[numQueues];
        for (int i = 0; i < numQueues; i++) {
            this.queues[i] = new GroceryQueue(maxQueueLength, i);
            Queuethread[i] = new Thread(this.queues[i]);
            Queuethread[i].start();
        }
        this.lock = new ReentrantLock();
    }

    public boolean addCustomer(Customer customer) {
        lock.lock();
        try {
            GroceryQueue minQueue = queues[0];
            int minLength = minQueue.getQueueSize();
            for (GroceryQueue queue : queues) {
                int length = queue.getQueueSize();
                if (length < minLength) {
                    minQueue = queue;
                    minLength = length;
                }
            }
            System.out.println(minLength);
            return minQueue.addCustomer(customer);
        } finally {
            lock.unlock();
        }
    }

    public GroceryQueue getQueue(int index) {
        return queues[index];
    }

    public void stopAllQueues() throws InterruptedException {
        for (GroceryQueue queue : queues) {
            queue.stop();
        }
        for(int i = 0; i < numQueues; i++)
        Queuethread[i].join();
    }
}
