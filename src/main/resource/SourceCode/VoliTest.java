package SourceCode;

public class VoliTest {
    private static volatile int count = 0;

    static class Test implements Runnable {

        @Override
        public void run() {
            VoliTest.count++;
        }

    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Test()).start();
        }
        System.out.println(count);
    }

}
