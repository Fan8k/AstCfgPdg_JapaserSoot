package SourceCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleTest {
    public static void main(String[] args) {
        defineSchedule2();
    }

    // 指定时间间隔后进行固定延迟period执行
    public static void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("执行timer1");
            }
        }, 2000, 100);
        System.out.println("执行1");
    }

    // 指定时间间隔后进行固定频率 （次数固定）period执行
    public static void timer2() {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").parse("2018-03-01,16:01:00");
        }
        catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                System.out.println("执行timer2");
            }
        }, date, 10000);
        System.out.println("执行1");
    }

    // 自定义线程实现定时任务 精髓就是sleep一定时间
    public static void defineSchedule1() {
        final long timeInterval = 1000;
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (true) {
                    System.out.println("hello!");
                    try {
                        Thread.sleep(timeInterval);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * 
     * 
     * ScheduledExecutorService是从Java
     * SE5的java.util.concurrent里，做为并发工具类被引进的，这是最理想的定时任务实现方式。 相比于上两个方法，它有以下好处：
     * 1>相比于Timer的单线程，它是通过线程池的方式来执行任务的 2>可以很灵活的去设定第一次执行任务delay时间
     * 3>提供了良好的约定，以便设定执行的时间间隔
     * 
     * 下面是实现代码，我们通过ScheduledExecutorService#scheduleAtFixedRate展示这个例子，通过代码里参数的控制
     * ，首次执行加了delay时间。
     * 
     * 
     * @author GT
     * 
     */
    public static void defineSchedule2() {
        Runnable runnable = new Runnable() {
            public void run() {
                // task to run goes here
                System.out.println("Hello !!");
            }
        };
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
        System.out.println("结束");
        // service.shutdown();
    }
}
