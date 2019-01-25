package cn.java.test;

import java.io.File;
import java.io.IOException;

public class SourceInfoTest {
    public static void main(String[] args) {
        File file = new File(
                "C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\poi-4.0.0\\AST_CFG_PDGdotInfo\\PasswordRev4Record\\(int,int,org.apache.poi.ss.formula.eval.ValueEval,org.apache.poi.ss.formula.eval.ValueEval,org.apache.poi.ss.formula.eval.ValueEval,org.apache.poi.ss.formula.eval.ValueEval,org.apache.poi.ss.formula.eval.ValueEval).dot");
        try {
            file.createNewFile();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getLineInfo() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return ste.getFileName() + ": Line " + ste.getLineNumber();
    }
}
