package SourceCode;

public class Queen {
    private int[] column;
    private int[] rup;// 右上到左下皇后
    private int[] lup;
    private int[] queen;
    private int num;

    // 8*8的棋盘 从1开始方便
    public Queen() {
        column = new int[9];
        rup = new int[2 * 8 + 1]; // 利用坐标相加标记右上到左下整个对角线
        lup = new int[2 * 8 + 1];
        queen = new int[9];
    }

    /*
     * 关键函数 i表示行数 也表示第一个皇后 j 从1到8 表示可以选择的元素 类似全排列 对角线全部用一个元素标记了一整条
     * 左边特点就是元素相减结果相等 (i,j)-->(i-1,j-1) 右边特点就是元素相加结果相等(i,j)--->(i-1,j+1)
     * 注意的就是回溯之后的状态恢复
     */
    public void backtrack(int i) { // 节点的个数 也是层数
        if (i > 8) {
            showAnswer();
        }
        else {
            for (int j = 1; j < 9; j++) { // 从剩余列中选取一个
                if (column[j] == 0 && rup[i + j] == 0 && lup[i - j + 8] == 0) {
                    queen[i] = j;// 第几行存储第几列的数据
                    column[j] = rup[i + j] = lup[i - j + 8] = 1;
                    backtrack(i + 1);
                    column[j] = rup[i + j] = lup[i - j + 8] = 0;
                    queen[i] = 0;
                }
            }
        }
    }

    protected void showAnswer() {
        num++;
        System.out.println("\n解答" + num);

        for (int y = 1; y <= 8; y++) {
            for (int x = 1; x <= 8; x++) {
                if (queen[y] == x) { // 某一行某一列有数据
                    System.out.print("Q");
                }
                else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Queen queen = new Queen();
        queen.backtrack(1);
    }

    class queen2 {
        private int N = 8;
        private int[] cszStack = new int[9]; // 保存结果
        private int sum = 0;
        private int top;

        private void put() {
            top = 1;
            while (top > 0) {
                cszStack[top]++;

            }
        }
    }
}
