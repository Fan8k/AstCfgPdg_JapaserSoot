package cn.fan.service;

import soot.PackManager;
import soot.Transform;
import soot.Transformer;

/**
 * 解析class 文件成为 cfg
 * @author fan
 *
 */
public class PaserClassToCfg<T extends Transformer> {
	public void parseCfg(T transformer) {
		// Options.v().keep_line_number();
		// 加入自定义阶段
		PackManager.v().getPack("jtp").add(new Transform("jtp.fan", transformer));
		// 运行参数设置
		execute();
	}

	private void execute() {
		String[] soot_args = new String[5];
		soot_args[0] = "--soot-classpath";
		soot_args[1] = "C:\\Users\\fan\\Desktop\\Data\\Analyze.jar;C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\rt.jar;C:\\Program Files\\Java\\jdk1.8.0_131\\jre\\lib\\jce.jar;";
		soot_args[2] = "cn.li.easy.AST_1";
		soot_args[3] = "-keep-line-number";
		soot_args[4] = "-print-tags-in-output";
	}
}
