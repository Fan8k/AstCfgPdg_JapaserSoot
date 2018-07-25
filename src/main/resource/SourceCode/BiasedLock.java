package SourceCode;

import java.util.Vector;

public class BiasedLock {
    private static Vector<Integer> listIntegers = new Vector<Integer>();

    public static void main(String[] args) {
        long begin = System.currentTimeMillis();
        int count = 0;
        int start = 0;
        while (count < 1000000) {
            listIntegers.add(start);
            start += 2;
            count++;
        }
        long end = System.currentTimeMillis();
        System.out.println((end - begin));
    }
}
