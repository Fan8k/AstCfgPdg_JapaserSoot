package cn.fan.service;

import java.util.List;
import java.util.UUID;

import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.Transformer;

public class PaserClassToCg<T extends Transformer> {
    public void parseCfg(T transformer, List<String> jarPath, String parseredClass) {
        G.reset();
        // 加入自定义阶段
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.cgfetcher" + UUID.randomUUID(), transformer));
        // 运行参数设置
        execute(parseredClass, jarPath);
    }

    private void execute(String parseredClass, List<String> jarPath) {
        String[] soot_args = new String[6];
        soot_args[0] = "-src-prec";
        soot_args[1] = "c";
        soot_args[2] = "-cp";
        soot_args[3] = listToString(jarPath);
        soot_args[4] = "-w";
        soot_args[5] = parseredClass;
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
