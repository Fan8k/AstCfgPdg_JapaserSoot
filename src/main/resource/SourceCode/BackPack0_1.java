package SourceCode;

/**
 * 0-1背包问题 利用回溯法的子集树 剪枝函数1.约束条件就是背包重量必须小于背包
 * 
 * @author fan
 *
 */
public class BackPack0_1 {
    private static final int N = 4;// 物品的数量
    private static final int[] VALUES = new int[] { 0, 9, 10, 7, 4 };// 物品的价值
    private static final int[] WEIGHTS = new int[] { 0, 3, 5, 2, 1 };// 物品的重量
    private static final int CAPACITY = 7;// 背包容量
    private static int BEST_VALUE = 0;// 最大的价值 就是最终结果
    private static final int[] bestx = new int[N + 1];// 从1开始
    private static int cw = 0; // 当前的重量
    private static int cv = 0;// 当前的价值
    private static int[] x = new int[N + 1];// 遍历过程中记录各个节点是否装入

    public static void main(String[] args) {
        System.out.println("0-1背包问题开始,物品个数为:" + N);
        System.out.println("物品的重量和价值分别为:" + WEIGHTS + VALUES);
        System.out.println("背包的容量为:" + CAPACITY);
        backtrack2();
        System.out.println("最大的价值:" + BEST_VALUE);
        System.out.println("存放的方式");
        for (int v : bestx) {
            System.out.print(v + "\t");
        }
    }

    public static void backtrack(int i) { // i 表示递归的层数
        if (i > N) { // 多少个物品就是多少层
            if (cv > BEST_VALUE) {
                BEST_VALUE = cv; // 遍历完一个值 就更新下
                for (int j = 1; j <= N; j++) {
                    bestx[j] = x[j];
                }
            }
        }
        else {
            for (int j = 0; j <= 1; j++) {// 分支的个数
                x[i] = j; // 放不放
                if (cw + x[i] * WEIGHTS[i] <= CAPACITY) { // 判断该物品是否可以进入
                    cw += x[i] * WEIGHTS[i];
                    cv += x[i] * VALUES[i];
                    backtrack(i + 1);
                    cw -= x[i] * WEIGHTS[i];
                    cv -= x[i] * VALUES[i];
                }

            }
        }
    }

    /**
     * 非递归的框架
     */
    public static void backtrack2() {
        int t = 1;
        int[] accessTimes = new int[N + 1]; // 每个节点访问次数
        while (t > 0) {
            if (accessTimes[t] < 2) { // 因为是两个分支
                accessTimes[t]++; // 访问一次就加一
                if (accessTimes[t] == 1) { // 左子树为放入
                    if (cw + WEIGHTS[t] <= CAPACITY) { // 判断该物品是否可以进入
                        cw += WEIGHTS[t];
                        cv += VALUES[t];
                        x[t] = 1;
                    }
                }
                else {
                    x[t] = 0; // 不放
                    cw -= WEIGHTS[t];
                    cv -= VALUES[t];
                }
                if (t >= N) {
                    if (cv > BEST_VALUE) {
                        BEST_VALUE = cv; // 遍历完一个值 就更新下
                        for (int j = 1; j <= N; j++) {
                            bestx[j] = x[j];
                        }
                    }
                }
                else {
                    t++;
                }

            }
            else {
                accessTimes[t] = 0;
                t--;
            }
        }
    }
}
