package SourceCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

//保护暂停
public class Guarded_Suspension {
    public static void main(String[] args) {
        // 初始化系统组件
        System.out.println("系统组件：队列");
        RequestQueue requestQueue = new RequestQueue();
        List<Future_Data> future_Datas = new ArrayList<Future_Data>();
        for (int i = 0; i < 10; i++) {
            future_Datas.add(new Future_Data());
        }
        // tomcat服务器开启，初始化服务器线程
        System.out.println("服务器开启,10个服务线程");
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new Server(requestQueue, future_Datas.get(i)), Integer.toString(i)));
            threads.get(i).start();
        }
        // 初始化页面对象
        System.out.println("页面对象初始化");
        Pages page = new Pages(requestQueue);
        System.out.println("客户上");
        for (int i = 0; i < 10; i++) {
            new Thread(new Customer(page, "customer" + i, future_Datas.get(i)), Integer.toString(i)).start();
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(future_Datas.get(i).getResult());
        }
        // 结果处理完了， 不用让serverThread一直wait等待请求队列了
        for (int i = 0; i < 10; i++) {
            threads.get(i).interrupt();
        }
    }
}

// 模拟用户 每一个用户就是一个线程
class Customer implements Runnable {
    private Pages page; // 用户访问的页面
    private String name;
    private Guarded_Data guarded_Data;

    public Customer(Pages page, String name, Guarded_Data guarded_Data) {
        super();
        this.page = page;
        this.name = name;
        this.guarded_Data = guarded_Data;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        int uid = new Random().nextInt();
        page.click(name, String.valueOf(uid));
        System.out.println("customer:" + name + ",请求查询" + uid);
        try {
            Thread.sleep(1000); // 模拟网络运行
        }
        catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getDealResult() {
        return guarded_Data.getResult();
    }
}

/**
 * customer 获取结果
 * 
 * @author fan
 *
 */
interface Guarded_Data {
    public String getResult();
}

class Future_Data implements Guarded_Data {
    private String result;
    private boolean isReady = false;

    public void setResult(String result) {
        synchronized (this) {
            this.result = result;
            isReady = true;
            notifyAll();
        }
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        synchronized (this) {
            while (!isReady) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}

// 类似Tomcat
class Server implements Runnable {
    private RequestQueue requestQueue;
    private Future_Data guarded_Data;

    public Server(RequestQueue requestQueue, Future_Data guarded_Data) {
        super();
        this.requestQueue = requestQueue;
        this.guarded_Data = guarded_Data;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        // 处理请求
        while (true) {
            MyhttpRequset requset = requestQueue.getRequset();
            if (requset == null) {
                break;
            } // 如果request为空，那就是调用了中断唤醒
            try {
                Thread.sleep(3000); // 服务器查询时间三秒
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println("ServerThread dealed the request" + requset
            // + ",查询结果：李鎔凡" + requset.getUserid());
            guarded_Data.setResult("serverThread:" + Thread.currentThread().getName() + "find the info " + requset);
        }
    }
}

// 类似网页
class Pages {
    private RequestQueue requestQueue;

    public Pages(RequestQueue requestQueue) {
        super();
        this.requestQueue = requestQueue;
    }

    // 模拟页面点击效果 发送一个http消息给服务器队列
    public void click(String requestName, String uid) {
        requestQueue.addRequest(new MyhttpRequset(requestName, uid));
    }
}

// https请求头
// 这次模拟将用户id带到后台，后台查询用户信息
class MyhttpRequset {
    // 请求者名字，也就是用户账号
    private String requeterName;
    // 需要查询的id
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public MyhttpRequset(String requeterName, String userid) {
        super();
        this.requeterName = requeterName;
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "MyhttpRequset [requeterName=" + requeterName + ", userid=" + userid + "]";
    }

}

// 缓冲 请求队列
class RequestQueue {
    // 服务器多个线程都可以处理request 所以需要并发
    private Queue<MyhttpRequset> requsets = new ConcurrentLinkedDeque<MyhttpRequset>();

    // 客户端将请求发给队列
    public void addRequest(MyhttpRequset requset) {
        requsets.add(requset);
        synchronized (this) {
            notifyAll();// 通知哪些服务器运行程序，有新的请求来啦
        }
    }

    // 服务器端获取请求
    public synchronized MyhttpRequset getRequset() {
        // 类似生成者和消费者
        while (requsets.size() == 0) {
            try {
                this.wait(); // 该线程等待 并释放this锁
            }
            catch (InterruptedException e) {
                // TODO Auto-generated catch block
                break;
            }
        }
        return requsets.poll();
    }
}