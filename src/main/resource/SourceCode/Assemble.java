package SourceCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现组合问题
 * 
 * @author fan
 *
 */
public class Assemble {

    private static final int[] DATAS = new int[] { 0, 1, 2, 3, 4 };
    private static final int K = 2;// 选出包含个数为2的子集个数
    private static final int[] cache = new int[5];
    private static final List<Integer> results = new ArrayList<Integer>();
    private static final int T = 5; // 元素子集加起来等于T

    public static void main(String[] args) {
        CombinationSum(1, 0);
    }

    /**
     * 
     * @param k
     *            表示的子集中包含元素的个数
     */
    public static void assemble(int k) {
        if (k > DATAS.length - 1 || size(cache) == 1) { // k 表示迭代的次数
            if (size(cache) == 1) {
                System.out.print("{");
                for (int v : cache) {
                    System.out.print(v + ",");
                }
                System.out.print("}\n");
            }
        }
        else {
            for (int i = 0; i < 2; i++) {
                if (i == 1) {
                    cache[k] = DATAS[k];
                }
                assemble(k + 1);
                if (i == 1) {
                    cache[k] = 0;
                }
            }
        }
    }

    /**
     * 可重复放入数据 生成的集合
     * 
     * @param k
     * @param sum
     */
    public static void CombinationSum(int k, int sum) {
        if (sum == T) {
            System.out.print("{");
            for (int v : results) {
                System.out.print(v + ",");
            }
            System.out.print("}\n");
        }
        if (k > DATAS.length - 1 || sum + DATAS[k] > T) {
            return;
        }
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                CombinationSum(k + 1, sum);
            }
            else {
                results.add(DATAS[k]); // 放入
                CombinationSum(k, sum + DATAS[k]);
                results.remove(results.size() - 1);
            }
        }
    }

    // 返回数组数据大小
    private static int size(int[] datas) {
        int n = 0;
        for (int data : datas) {
            if (data > 0) {
                n++;
            }
        }
        return n;
    }
}
