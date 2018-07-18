package cn.fan.service;

import java.net.URISyntaxException;

import soot.PackManager;
import soot.Transform;
import soot.Transformer;
import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.tool.LoadSourceCode;

/**
 * 解析class 文件成为 cfg
 * 
 * @author fan
 *
 */
public class PaserClassToCfg<T extends Transformer> {
    public void parseCfg(T transformer, String jarPath, String parseredClass) {
        // Options.v().keep_line_number();
        // 加入自定义阶段
        PackManager.v().getPack("jtp").add(new Transform("jtp.fan", transformer));
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
        MethodTransformer methodTransformer = new MethodTransformer("way");
        PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
        try {
            paserClassToCfg.parseCfg(methodTransformer, LoadSourceCode.load(SourceSubPathEnum.JAR_SOURCE_PATH, "Analyze.jar").toString(), "cn.li.easy.AST_1");
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
