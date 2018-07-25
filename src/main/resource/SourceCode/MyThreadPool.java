package SourceCode;

import java.util.List;
import java.util.Vector;

public class MyThreadPool {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            new Thread(new MyThread()).start();
        }
        long endofNO = System.currentTimeMillis();

        System.out.println((endofNO - start));

        long start1 = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ThreadPool.getInstance().submit(new MyThread());
        }
        long endof1 = System.currentTimeMillis();
        System.out.println(endof1 - start1);
        System.out.println(ThreadPool.getInstance().getThreadCounter());
    }
}

class MyThread implements Runnable {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class ThreadPool {
    private static ThreadPool instancePool = null;
    // 空闲线程队列
    private List<PThread> idleThreads;
    // 已有线程总数
    private int threadCounter;
    private boolean isShutDown = false;

    private ThreadPool() {
        this.idleThreads = new Vector<PThread>(5);
        threadCounter = 0;
    }

    public int getCreatedThreadsCount() {
        return threadCounter;
    }

    public synchronized static ThreadPool getInstance() {
        if (instancePool == null) {
            instancePool = new ThreadPool();
        }
        return instancePool;
    }

    protected synchronized void repool(PThread repoolingThread) {
        if (!isShutDown) {
            idleThreads.add(repoolingThread);
        }
        else {
            repoolingThread.shutdown();
        }
    }

    // 停止池中所有线程
    public synchronized void shutdown() {
        isShutDown = true;
        for (int index = 0; index < idleThreads.size(); index++) {
            idleThreads.get(index).shutdown();
        }
    }

    // 开始
    public synchronized void submit(Runnable target) {
        PThread thread = null;
        if (idleThreads.size() > 0) {
            int lastIndex = idleThreads.size() - 1;
            thread = (PThread) idleThreads.get(lastIndex);
            idleThreads.remove(lastIndex);
            // 设置空闲线程任务
            thread.setTarget(target); // 空闲线程没有死，直接唤醒执行
        }
        else {
            threadCounter++;
            thread = new PThread("PThread#" + threadCounter, this, target);
            thread.start();
        }
    }

    public int getThreadCounter() {
        return threadCounter;
    }
}

// 需要定制线程 这种线程执行完任务是不能退出的！
class PThread extends Thread {
    private ThreadPool pool;
    private Runnable target;
    private boolean isShutdown = false;

    public PThread(String name, ThreadPool pool, Runnable target) {
        super(name);
        this.pool = pool;
        this.target = target;
    }

    public Runnable getTarget() {
        return target;
    }

    // 设置任务
    public synchronized void setTarget(Runnable target) {
        this.target = target;
        notify(); // 通知一个想要获取该对象的线程
    }

    // 关闭线程
    public synchronized void shutdown() {
        isShutdown = true;
        notify();
    }

    // 该线程不能停止！这就是线程池核心
    @Override
    public void run() {
        while (!isShutdown) {
            if (target != null) {
                target.run();
            }
            pool.repool(this);
            synchronized (this) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
