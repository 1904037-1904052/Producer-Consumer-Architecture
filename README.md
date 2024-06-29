# Producer-Consumer Architecture

This project implement two different types of concurrent queues to determine which is most efficient. It simulates Bank queue and Grocery queue systems where customers arrive and are served by consumers.

## Parts of Simulation
- ### Producer:
  Produces customers and push them into the queues. (new customers arrive between every 20 and 60 seconds)
- ### Consumer:
  Consumes these customer by popping them out of the queues. (it takes between 60 and 300 seconds to serve any customer)
  - For BankQueue, Teller is the consumer.
  - For GroceryQueues, Cashier is the consumer.
- ### Queues:
  Controlls customer entering the queues.
  - For BankQueue, Contains only one lineup. A customer departs immediately if the queue is full.
  - For GroceryQueues, Contains multiple lineup, one for each Cahier. If a customer arrives, and no queue exists with any space, then he/she waits until a queue becomes available until a maximum of 10 seconds and then promptly leaves.
- ### Customer:
  Represents a customer and contains its properties. Keeps track of :
  - Customer’s arrival time.
  - Time needed for it to be served.
  - It departs without being served.

## Parameters
- Duration
- Number of Tellers
- Number of Cashiers
- Capacity of Bank Queue
- Capacity of Grocery Queue

## Output
For each queue :
- Total customers arrived
- Total customers departed without being served
- Total customers served
- Average amount of time taken to serve each customer
