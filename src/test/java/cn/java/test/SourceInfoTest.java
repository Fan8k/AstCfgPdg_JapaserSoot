package cn.java.test;

public class SourceInfoTest {
	public static void main(String[] args) {
		System.out.println(getLineInfo());
		int a = 10;
	}

	public static String getLineInfo() {
		StackTraceElement ste = new Throwable().getStackTrace()[1];
		return ste.getFileName() + ": Line " + ste.getLineNumber();
	}
}
