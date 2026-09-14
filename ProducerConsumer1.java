import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
public class ProducerConsumer {

    static class SharedBuffer {
        private final Queue<Integer> queue = new LinkedList<>();
        private final int capacity;

        SharedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void produce(int item, String producerName) throws InterruptedException {
            while (queue.size() == capacity) {
                System.out.println(producerName + " -> buffer FULL, waiting...");
                wait();
            }

            queue.add(item);
            System.out.println(producerName + " produced: " + item + "   [buffer size=" + queue.size() + "]");

            notifyAll();
        }

        public synchronized int consume(String consumerName) throws InterruptedException {
            while (queue.isEmpty()) {
                System.out.println(consumerName + " -> buffer EMPTY, waiting...");
                wait();
            }

            int item = queue.poll();
            System.out.println(consumerName + " consumed: " + item + "   [buffer size=" + queue.size() + "]");

            notifyAll();
            return item;
        }
    }

    static class Producer implements Runnable {
        private final SharedBuffer buffer;
        private final String name;
        private final int itemsToProduce;
        private final Random random = new Random();

        Producer(SharedBuffer buffer, String name, int itemsToProduce) {
            this.buffer = buffer;
            this.name = name;
            this.itemsToProduce = itemsToProduce;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= itemsToProduce; i++) {
                    int item = random.nextInt(1000);
                    buffer.produce(item, name);
                    Thread.sleep(random.nextInt(300) + 100); // simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " was interrupted.");
            }
        }
    }

    static class Consumer implements Runnable {
        private final SharedBuffer buffer;
        private final String name;
        private final int itemsToConsume;
        private final Random random = new Random();

        Consumer(SharedBuffer buffer, String name, int itemsToConsume) {
            this.buffer = buffer;
            this.name = name;
            this.itemsToConsume = itemsToConsume;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= itemsToConsume; i++) {
                    buffer.consume(name);
                    Thread.sleep(random.nextInt(400) + 100); // simulate processing
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(name + " was interrupted.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int BUFFER_CAPACITY = 5;
        final int ITEMS_PER_PRODUCER = 10;
        final int NUM_PRODUCERS = 2;
        final int NUM_CONSUMERS = 2;

        SharedBuffer buffer = new SharedBuffer(BUFFER_CAPACITY);

        Thread[] producers = new Thread[NUM_PRODUCERS];
        Thread[] consumers = new Thread[NUM_CONSUMERS];

        
        int itemsPerConsumer = (ITEMS_PER_PRODUCER * NUM_PRODUCERS) / NUM_CONSUMERS;

        for (int i = 0; i < NUM_PRODUCERS; i++) {
            producers[i] = new Thread(new Producer(buffer, "Producer-" + (i + 1), ITEMS_PER_PRODUCER));
        }
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new Thread(new Consumer(buffer, "Consumer-" + (i + 1), itemsPerConsumer));
        }

        System.out.println("=== Producer-Consumer Simulation Starting ===");
        System.out.println("Buffer capacity: " + BUFFER_CAPACITY);
        System.out.println("Producers: " + NUM_PRODUCERS + " x " + ITEMS_PER_PRODUCER + " items each");
        System.out.println("Consumers: " + NUM_CONSUMERS + " x " + itemsPerConsumer + " items each\n");

        for (Thread p : producers) p.start();
        for (Thread c : consumers) c.start();

        for (Thread p : producers) p.join();
        for (Thread c : consumers) c.join();

        System.out.println("\n=== Simulation Complete: all items produced and consumed ===");
    }
}
