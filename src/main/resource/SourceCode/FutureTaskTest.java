package SourceCode;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<String>(new RealData());
        Thread thread = new Thread(futureTask);
        thread.start();
        System.out.println(futureTask.get());
    }

    static class RealData implements Callable<String> {

        @Override
        public String call() throws Exception {
            // TODO Auto-generated method stub
            return "我是大帅哥!";
        }

    }
}
