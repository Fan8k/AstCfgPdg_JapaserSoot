package cn.fan.fore;

import java.net.URISyntaxException;

import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;
import cn.fan.service.TranformSourceCodeToLine;
import cn.fan.tool.LoadSourceCode;

public class Entrance {
	public static void main(String[] args) {
		TranformSourceCodeToLine tranformSourceCodeToLine = new TranformSourceCodeToLine("Ast_1.java");
		PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst("Ast_1.java");
		//入口方法
		MethodTransformer methodTransformer = new MethodTransformer("way");
		PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
		try {
			//需要解析的jar包和主入口类
			paserClassToCfg.parseCfg(methodTransformer, LoadSourceCode.load(SourceSubPathEnum.JAR_SOURCE_PATH, "Analyze.jar").toString(),
								"cn.li.easy.AST_1");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DotPrinterCfg dotPrinter = new DotPrinterCfg(tranformSourceCodeToLine, paserSourceCodeToAst, methodTransformer.getAllEdges());
		dotPrinter.print("way");
	}
}
