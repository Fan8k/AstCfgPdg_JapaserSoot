package cn.javaparser.test;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class StmtVisitor extends VoidVisitorAdapter<Void> {

	@Override
	public void visit(IfStmt n, Void arg) {
		System.out.println(n.getChildNodes());
	}

	@Override
	public void visit(ExpressionStmt n, Void arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		System.out.println(n.getExpression());
	}

	@Override
	public void visit(ForStmt n, Void arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		System.out.println(n.getBody());
	}

	@Override
	public void visit(WhileStmt n, Void arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		for (Node node : n.getChildNodes()) {
			System.out.println(node.getClass());
		}
	}
}
