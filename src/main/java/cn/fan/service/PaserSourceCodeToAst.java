package cn.fan.service;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.fan.ast.visitor.MethodVisitor;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.tool.LoadSourceCode;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

/**
 * 利用javaPaser 解析语法树
 * @author fan
 *
 */
public class PaserSourceCodeToAst {
	private Logger logger = Logger.getLogger(PaserSourceCodeToAst.class);
	/**
	 * Soot AST树的树根
	 */
	private CompilationUnit compilationUnit;
	/**
	 * java sourceCode的原路径
	 */
	private Path path;

	/**
	 * @param filename  XXX.java
	 */
	public PaserSourceCodeToAst(String filename) {
		try {
			this.path = LoadSourceCode.load(SourceSubPathEnum.SOURCE_CODE_PATH, "");
		} catch (URISyntaxException e) {
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
		compilationUnit = new SourceRoot(path).parse("", filename);
	}

	/**
	 * 根据方法的名字解析某一个方法 获取方法的开始到结束的行号
	 * @param methodName 方法的名字
	 */
	public List<Integer> getLinesFromOneMethod(String methodName) {
		List<Integer> results = new ArrayList<Integer>();
		MethodVisitor methodVisitor = new MethodVisitor(methodName);
		methodVisitor.visit(compilationUnit, results);
		return results;
	}
}
