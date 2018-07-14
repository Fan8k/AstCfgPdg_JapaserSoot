package cn.javaparser.test;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
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
		CompilationUnit cu = sourceRoot.parse("", "AST_1.java");
		List<Node> childNodes = cu.getChildNodes();
		for (Node node : childNodes) {
			if (node instanceof ClassOrInterfaceDeclaration) {
				List<Node> childNodes2 = node.getChildNodes();
				for (Node node2 : childNodes2) {
					if (node2 instanceof SimpleName) {
						SimpleName simpleName = (SimpleName) node2;
						System.out.println(simpleName.getId());
					}
				}
			}
		}
	}

}
