import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class GroceryQueue implements Runnable {
    private final int index;
    private final int maxQueueLength;
    private final Queue<Customer> queue;
    private final Lock lock;
    private final Condition queueNotEmpty;
    private volatile boolean running;

    public GroceryQueue(int maxQueueLength, int index) {
        this.index = index;
        this.maxQueueLength = maxQueueLength;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.queueNotEmpty = lock.newCondition();
        this.running = true;
    }

    public boolean addCustomer(Customer customer) {
        lock.lock();
        try {
            if (queue.size() < maxQueueLength) {
                System.out.printf("GroceryQueue %d takes one customer\n", index);
                queue.offer(customer);
                queueNotEmpty.signal(); // Signal cashier that a customer is available
                return true;
            } else {
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    public Customer getCustomerForService() {
        lock.lock();
        try {
            while (queue.isEmpty() && running) {
                queueNotEmpty.await(); // Wait until a customer is available
            }
            return queue.poll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public int getQueueSize() {
        lock.lock();
        try {
            System.out.println("Queue index :" + index + "size" + queue.size());
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            running = false;
            queueNotEmpty.signalAll(); // Wake up all waiting cashiers
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void run() {
        // This thread just manages the queue and waits for new customers
        System.out.printf("GroceryQueue queue %d starts working.\n", index);
        while (running) {
            try {
                Thread.sleep(1000); // Just keep this thread alive
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
