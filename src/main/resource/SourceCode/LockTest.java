package SourceCode;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTest {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final LockTest lockTest = new LockTest();
        Thread one = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    lockTest.insert(Thread.currentThread());
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println(this.getName() + "中断唤醒");
                }
            }

        };
        one.start();
        Thread two = new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    lockTest.insert(Thread.currentThread());
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println(this.getName() + "中断唤醒");
                }
            }

        };
        two.start();
    }

    public void insert(Thread thread) throws InterruptedException {
        lock.readLock().lock();
        System.out.println("got锁");
        try {
            for (int i = 0; i < 5000; i++) {
                System.out.println(thread.getName() + ":" + i);
                // thread.sleep(1000);
            }
        }
        finally {
            System.out.println("释放锁");
            lock.readLock().unlock();
        }
    }
}
