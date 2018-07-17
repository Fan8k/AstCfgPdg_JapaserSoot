package cn.javaparser.test;

import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.PrimitiveType.Primitive;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Void> {

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		if (n.getName().getId().equals("way")) { //获取
			/*StmtVisitor stmtVisitor = new StmtVisitor();
			stmtVisitor.visit(n, arg);*/
			List<Node> childNodes = n.getChildNodes();
			VariableDeclarator variableDeclarator = new VariableDeclarator(new PrimitiveType(Primitive.INT), "a", new IntegerLiteralExpr(10));
			System.out.println(variableDeclarator);
			for (Node node : childNodes) {
				System.out.println(node);
				System.out.println(node.getParentNode());
				if (node instanceof BlockStmt) {
					ExpressionStmt expressionStmt = new ExpressionStmt(new VariableDeclarationExpr(variableDeclarator));
					((BlockStmt) node).addStatement(expressionStmt);
				}
			}
		}
	}
}
