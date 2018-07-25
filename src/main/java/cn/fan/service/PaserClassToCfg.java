package cn.fan.service;

import java.util.UUID;

import soot.G;
import soot.PackManager;
import soot.Transform;
import soot.Transformer;

/**
 * 解析class 文件成为 cfg
 * 
 * @author fan
 *
 */
public class PaserClassToCfg<T extends Transformer> {

    public void parseCfg(T transformer, String jarPath, String parseredClass) {
        G.reset();
        // 加入自定义阶段
        PackManager.v().getPack("jtp").add(new Transform("jtp.fan" + UUID.randomUUID(), transformer));
        // 运行参数设置
        execute(parseredClass, jarPath);
    }

    private void execute(String parseredClass, String jarPath) {
        String[] soot_args = new String[5];
        StringBuilder sbBuilder = new StringBuilder("C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\rt.jar;C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\jce.jar;");
        sbBuilder.append(jarPath + ";");
        soot_args[0] = "--soot-classpath";
        soot_args[1] = sbBuilder.toString();
        soot_args[2] = parseredClass;
        soot_args[3] = "-keep-line-number";
        soot_args[4] = "-print-tags-in-output";
        soot.Main.main(soot_args);
    }

    public static void main(String[] args) {

    }
}
