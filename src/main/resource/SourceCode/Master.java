package SourceCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Master {
    // 任务队列 多线程访问
    private Queue<Object> workQueue = new ConcurrentLinkedDeque<Object>();
    // worker 工作线程
    private Map<String, Thread> threadMap = new HashMap<String, Thread>();
    // 子任务结果
    private Map<String, Object> resultMap = new ConcurrentHashMap<String, Object>();

    public Master(Worker worker, int countWorker) {
        // TODO Auto-generated constructor stub
        worker.setWorkQueue(workQueue);
        worker.setResultMap(resultMap);
        for (int i = 0; i < countWorker; i++) {
            threadMap.put(Integer.toString(i), new Thread(worker, Integer.toString(i)));
        }
    }

    // 是否所有的子任务都结束了
    public boolean isComplete() {
        for (Entry<String, Thread> worker : threadMap.entrySet()) {
            if (worker.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }
        return true;
    }

    // 提交一个任务
    public void submit(Object obj) {
        workQueue.add(obj);
    }

    // 返回子任务结果集
    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void execute() {
        for (Entry<String, Thread> worker : threadMap.entrySet()) {
            worker.getValue().start();
        }
    }

    public static void main(String[] args) {
        Master master = new Master(new plusWorker(), 5);
        // 客户提交任务
        for (int i = 1; i < 101; i++) {
            master.submit(i);
        }
        master.execute();
        int re = 0;
        Map<String, Object> resultMap2 = master.getResultMap();
        while (!master.isComplete()) {

        }
        for (Object result : resultMap2.values()) {
            re += (int) result;
        }
        System.out.println(re);
    }
}

abstract class Worker implements Runnable {
    // 任务队列
    private Queue<Object> workQueue;
    // 子任务结果
    private Map<String, Object> resultMap;

    public void setWorkQueue(Queue<Object> workQueue) {
        this.workQueue = workQueue;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            Object input = workQueue.poll();
            if (input == null)
                break; // 任务队列没有任务
            Object result = handle(input);
            resultMap.put(Integer.toString(input.hashCode()), result);
        }
    }

    // 程序员自己实现处理逻辑
    public abstract Object handle(Object object);
}

class plusWorker extends Worker {

    @Override
    public Object handle(Object object) {
        // TODO Auto-generated method stub
        Integer i = (Integer) object;
        return i * i * i;
    }

}