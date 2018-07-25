package SourceCode;

import java.util.ArrayList;

// 原始串行版本
/**
 * 问题 如果同步耽误太多时间 所以用多线程 但是如果运行结果我们需要，也就是多线程需要返回结果
 * 
 * @author fan
 *
 */
class task {
    // 同步耽误太多时间
    public static void main(String[] args) {
        System.out.println("客户端开启");
        dealSomething();
        // 改进一下 用多线程运行
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                dealSomething();
            }
        }.start();
        System.out.println("客户端之前一直等待，现在才可以处理别的事");
    }

    public static void dealSomething() {
        System.out.println("花费3小时处理别的问题");
    }
}

/*
 * 运行有结果线程 比如数据库返回结果
 */
public class FutureTest {
    public static void main(String[] args) {
        clientProxy data = request("查询李鎔凡信息");
        System.out.println("客户端继续干自己的事情");
        try {
            System.out.println(data.getDataFromDatabase());
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static clientProxy request(String sql) {
        clientProxy cliProxy = new clientProxy();
        ConcreateSubject concreateSubject = new WorkerTask(); // 程序员只需要自己实现这个任务就行
        concreateSubject.attach(cliProxy);
        Thread taskThread = new Thread(concreateSubject);
        taskThread.start();
        return cliProxy;
    }

    // 实现第一种方式 观察者模式 就是完成之后通知客户端
    interface ObserverI {
        // 观察者方法
        public void getValue(String value);
    }

    private static class clientProxy implements ObserverI {
        // 来自数据库的数据
        private String dataFromDatabase;
        private boolean isready = false;

        @Override
        public synchronized void getValue(String value) {
            // TODO Auto-generated method stub
            this.dataFromDatabase = value;
            isready = true;
            notifyAll();
        }

        public synchronized String getDataFromDatabase() throws InterruptedException {
            // 必须取数据的时候 必须等待
            while (!isready) {
                System.out.println("数据还没有准备好！你先到边上喝点水吧");
                wait();
            }
            return dataFromDatabase;
        }
    }

    /**
     * 被观察对象
     * 
     * @author fan
     *
     */
    interface SubjectI {
        void attach(ObserverI observer);

        void detach(ObserverI observer);

        void inform(String value);
    }

    static abstract class ConcreateSubject implements SubjectI, Runnable {
        private ArrayList<ObserverI> observerlist = new ArrayList<ObserverI>();

        @Override
        public void attach(ObserverI observer) {
            // TODO Auto-generated method stub
            observerlist.add(observer);
        }

        @Override
        public void detach(ObserverI observer) {
            // TODO Auto-generated method stub
            observerlist.add(observer);
        }

        @Override
        public void inform(String value) {
            // TODO Auto-generated method stub
            System.out.println("多线程运行完毕，系统开始回调");
            for (ObserverI observer : observerlist) {
                observer.getValue(value);
            }
            System.out.println("代理都已经拿到数据");
        }
    }

    static class WorkerTask extends ConcreateSubject {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("从数据库中取数据，很长时间");
            System.out.println("取到数据");
            inform("李鎔凡 男 帅哥一枚");// 通知客户端来取数据
        }
    }
}

class client {
    public Data request(String queryStr) {
        final FutureData futureData = new FutureData();
        new Thread() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                RealData realData = new RealData();
                futureData.setRealData(realData);
            }

        }.start();
        return futureData;
    }
}

interface Data {
    public String getResult();
}

class FutureData implements Data {
    RealData RealData = null;
    boolean isReady = false;

    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }
        this.RealData = realData;
        isReady = true;
        notifyAll();
    }

    @Override
    public synchronized String getResult() {
        // TODO Auto-generated method stub
        while (!isReady) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return RealData.getResult();
    }
}

class RealData implements Data {
    public RealData() {
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return "结果：运行时间花费5分钟";
    }

}
