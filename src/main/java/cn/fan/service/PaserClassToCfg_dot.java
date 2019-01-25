package cn.fan.service;

import java.util.List;
import java.util.UUID;

import soot.G;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.Transformer;

public class PaserClassToCfg_dot<T extends Transformer> {
    public void parseCfg(T transformer, List<String> jarPath, String parseredClass) {
        G.reset();
        // 加入自定义阶段
        PackManager.v().getPack("jtp").add(new Transform("jtp.fan" + UUID.randomUUID(), transformer));
        // 运行参数设置
        execute(parseredClass, jarPath);
    }

    private void execute(String parseredClass, List<String> jarPath) {
        String[] soot_args = new String[5];
        soot_args[0] = "--soot-classpath";
        soot_args[1] = listToString(jarPath);
        // soot_args[2] = "-w";
        soot_args[2] = parseredClass;
        soot_args[3] = "-keep-line-number";
        soot_args[4] = "-print-tags-in-output";
        Scene.v().addBasicClass("java.lang.invoke.LambdaMetafactory", SootClass.SIGNATURES);
        soot.Main.main(soot_args);
    }

    private String listToString(List<String> jarPath) {
        StringBuilder sbBuilder = new StringBuilder("C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\rt.jar;C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\jce.jar;");
        for (String s : jarPath) {
            sbBuilder.append(s + ";");
        }
        return sbBuilder.toString();
    }
}
