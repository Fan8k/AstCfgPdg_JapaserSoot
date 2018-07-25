package SourceCode;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemapTest {
    private final Semaphore semaphore;
    private boolean resourceArray[];
    private final ReentrantLock lock;

    public SemapTest() {
        this.resourceArray = new boolean[10];// 存放厕所状态
        this.semaphore = new Semaphore(10, true);// 控制10个共享资源的使用，使用先进先出的公平模式进行共享;公平模式的信号量，先来的先获得信号量
        this.lock = new ReentrantLock(true);// 公平模式的锁，先来的先选
        for (int i = 0; i < 10; i++) {
            resourceArray[i] = true;// 初始化为资源可用的情况
        }
    }

    public void useResource(int userid) {
        try {
            semaphore.acquire();
            int id = getResourceId();// 占到一个坑
            System.out.println("userId:" + userid + "正在使用资源，资源id:" + id + "\n");
            Thread.sleep(5000);
            resourceArray[id] = true; // 由于semaphore 管理 所以id=-1 不可能执行到
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            semaphore.release();
        }

    }

    private int getResourceId() {
        int id = -1;
        lock.lock();
        try {
            // lock.lock();//虽然使用了锁控制同步，但由于只是简单的一个数组遍历，效率还是很高的，所以基本不影响性能。
            for (int i = 0; i < 10; i++) {
                if (resourceArray[i]) {
                    resourceArray[i] = false;
                    id = i;
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
        return id;
    }

    public static void main(String[] args) {
        SemapTest semapTest = new SemapTest();
        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            final int id = i;
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    semapTest.useResource(id);
                }
            });// 创建多个资源使用者
            threads[i] = thread;
        }
        for (int i = 0; i < 100; i++) {
            Thread thread = threads[i];
            try {
                thread.start();// 启动线程
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
