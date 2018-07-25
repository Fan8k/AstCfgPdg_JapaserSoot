package SourceCode;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Producter_Consumer_Main {

    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new LinkedBlockingDeque<Integer>();
        Producter producter1 = new Producter(queue);
        Producter producter2 = new Producter(queue);
        Consumer consumer1 = new Consumer(queue);
        Consumer consumer2 = new Consumer(queue);
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(producter1);
        service.execute(producter2);
        service.execute(consumer1);
        service.execute(consumer2);
        // producter1.stop();
        // producter2.stop();
        service.shutdown();
    }
}

class Producter implements Runnable {
    private volatile boolean isRunning = true;
    // 公共区域
    private BlockingQueue<Integer> queue;

    public Producter(BlockingQueue<Integer> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (!queue.offer(1, 2, TimeUnit.SECONDS)) {
                    System.out.println("failed to put data");
                }
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        isRunning = false;
    }
}

class Consumer implements Runnable {
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        super();
        this.queue = queue;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            try {
                System.out.println(queue.take());
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}