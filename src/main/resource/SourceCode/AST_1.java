package SourceCode;

import java.util.Random;

public class AST_1 {
    int S = 0;

    public void way() {
        try {
            Random random = new Random();
            int a = random.nextInt();
            System.out.println(a + 10);
            int b = random.nextInt();
            System.out.println(b + 15);
            a = random.nextInt() + random.nextInt();
            System.out.println(a + 20);
            if ("ok".equals("ok")) {
                a = random.nextInt() + 40;
            }
            int c = 6 + 9;
            System.out.println(c + 3);
            b = a + random.nextInt();
            System.out.println(b);

        }
        catch (Exception e) {

        }
    }

    public void way2() {
        int[] arr = { 6, 3, 8, 2, 9, 1 };
        System.out.println("排序前数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        for (int i = 0; i < arr.length - 1; i++) {// 外层循环控制排序趟数
            for (int j = 0; j < arr.length - 1 - i; j++) {// 内层循环控制每一趟排序多少次
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        new AST_1().way();
    }
}
