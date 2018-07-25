package SourceCode;

/**
 * 全排列
 * 
 * @author fan
 *
 */
public class Arrange {
    private static String[] datas = new String[] { "A", "B", "C" };
    private static String[] cache = new String[3];

    public static void main(String[] args) {
        arrange(0);
    }

    private static void arrange(int ceng) {
        if (ceng == 3) {
            for (String s : datas) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
        else {
            for (int i = ceng; i < 3; i++) {
                swap(ceng, i);
                arrange(ceng + 1);
                swap(ceng, i);
            }
        }
    }

    private static void swap(int a, int b) {
        String w = datas[a];
        datas[a] = datas[b];
        datas[b] = w;
    }

}
