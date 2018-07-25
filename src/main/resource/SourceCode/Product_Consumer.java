package SourceCode;

import java.util.ArrayList;
import java.util.Random;

/**
 * 生产者消费者
 * 
 * @author fan
 *
 */
public class Product_Consumer {
    private static ArrayList<Integer> list = new ArrayList<Integer>(1);

    static class Producter implements Runnable {

        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                synchronized (list) {
                    if (list.size() == 0) {
                        System.out.println("生成一个零件");
                        list.add(random.nextInt());
                        list.notify();
                    }
                    System.out.println("还有零件没有使用");
                    try {
                        list.wait();
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Consumer implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (list) {
                    if (list.size() != 0) {
                        System.out.println("消耗一个零件");
                        list.remove(0);
                        list.notify();
                    }
                    System.out.println("零件没有了");
                    try {
                        list.wait();
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread producter = new Thread(new Producter());
        Thread comsumer = new Thread(new Consumer());
        producter.start();
        comsumer.start();
    }
}
