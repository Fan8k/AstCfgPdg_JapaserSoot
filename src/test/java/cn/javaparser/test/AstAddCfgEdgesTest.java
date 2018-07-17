package cn.javaparser.test;

import java.io.IOException;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

public class AstAddCfgEdgesTest {
	public static void main(String[] args) throws IOException {
		SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(StatementsInMethodTest.class).resolve(
							"src/test/resource/SourceCode"));
		CompilationUnit cu = sourceRoot.parse("", "Test.java");

		/*MethodVisitor methodVisitor = new MethodVisitor();
		methodVisitor.visit(cu, null);*/

		DotPrintAstTest dotPrintAstTest = new DotPrintAstTest(true);

		dotPrintAstTest.output(cu);

		/*DotPrinter printer = new DotPrinter(true);
		try (FileWriter fileWriter = new FileWriter("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\one\\AST\\AST_3.dot");
							PrintWriter printWriter = new PrintWriter(fileWriter)) {
			printWriter.print(printer.output(cu));
		}
		System.out.println("输出完毕！");*/
	}
}
