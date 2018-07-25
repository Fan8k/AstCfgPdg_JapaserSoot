package SourceCode;

public class Iteration0_1 {
    // 迭代实现
    public static void getSubset_iterative(int list[], int n) {
        int t = 0;// t 代表解空间树的深度
        boolean[] v = new boolean[n + 1];// 实现放还是不放
        int[] init = new int[n + 1]; // 实现回溯
        while (t >= 0) {
            if (2 > init[t]) {
                init[t]++;
                v[t] = !v[t];
                if (t >= n) {
                    for (int i = 0; i <= n; i++) {
                        if (v[i]) {
                            System.out.print(list[i] + " ");
                        }
                    }
                    System.out.println();
                }
                else {
                    t++;
                }
            }
            else {
                init[t] = 0;
                t--;
            }
        }
    }

    public static void getSubset(int list[], boolean v[], int a, int b) {
        if (a == b) {
            for (int i = 0; i < b; i++) {
                if (v[i]) {
                    System.out.print(list[i] + " ");
                }
            }
            System.out.println();
            return;
        }
        v[a] = true;
        getSubset(list, v, a + 1, b);
        v[a] = false;
        getSubset(list, v, a + 1, b);

    }

    public static void getSubset2(int list[], boolean v[], int a) {
        if (a == 4) {
            for (int i = 0; i < 4; i++) {
                if (v[i]) {
                    System.out.print(list[i] + " ");
                }
            }
            System.out.println();
            return;
        }
        for (int j = 0; j < 2; j++) {
            if (j == 0) {
                v[a] = false;
                getSubset2(list, v, a + 1);
            }
            else {
                v[a] = true;
                getSubset2(list, v, a + 1);
            }
        }

    }

    public static void main(String[] args) {
        int li[] = { 1, 2, 3, 4 };
        getSubset_iterative(li, 3);
        // int[] li = {1,2,3,4};
        // boolean v[] = { false, false, false, false };
        // getSubset2(li, v, 0);
    }
}
