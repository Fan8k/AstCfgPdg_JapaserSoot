package SourceCode;

public class ThreadStop {
    static class One implements Runnable {
        private volatile boolean stoped = false;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!stoped) {
                System.out.println("运行中");
                try {
                    Thread.sleep(100000);
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    System.out.println("梦中被我叫醒");
                }
            }
        }

    }

    public static void main(String[] args) throws InterruptedException {
        One one = new One();
        Thread oneThread = new Thread(one);
        oneThread.start();
        // oneThread.join();
        Thread.sleep(1000);
        one.stoped = true;
        System.out.println("执行");
        oneThread.interrupt();
    }
}
