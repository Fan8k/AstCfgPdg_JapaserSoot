package cn.javaparser.test;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

/**
 * 测试在method 所有可能的结构体
 * @author fan
 *
 */
public class StatementsInMethodTest {

	public static void main(String[] args) {
		SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(StatementsInMethodTest.class).resolve(
							"src/test/resource/SourceCode"));
		CompilationUnit cu = sourceRoot.parse("", "Test.java");

		MethodVisitor methodVisitor = new MethodVisitor();

		methodVisitor.visit(cu, null);
	}

	//使用javaParser 自带的Visitor遍历树，而非自己for循环 还要判断数据类型
}
