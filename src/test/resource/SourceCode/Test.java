package SourceCode;

public class Test {
    int S = 0;

    public void way() {
        try {
            int a = 10;
            if (a == 10) {
                System.out.println(way2());
            }
            for (int i = 0; i < 2; i++) {
                System.out.println(i);
                if (i == 1) {
                    continue;
                }
                if (i == 0) {
                    break;
                }
            }
        }
        catch (Exception e) {

        }
    }

    public String way2() {
        try {
            int a = 10;
            if (a == 10) {
                System.out.println(way2());
            }
            for (int i = 0; i < 2; i++) {
                System.out.println(i);
                if (i == 1) {
                    continue;
                }
                if (i == 0) {
                    break;
                }
            }
        }
        catch (Exception e) {

        }
        return "fafasfasd";
    }

    public static void main(String[] args) {

    }
}
