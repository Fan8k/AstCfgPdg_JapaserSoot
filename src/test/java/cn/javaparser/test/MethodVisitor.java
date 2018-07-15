package cn.javaparser.test;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Void> {

	@Override
	public void visit(MethodDeclaration n, Void arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		if (n.getName().getId().equals("way")) { //获取
			/*StmtVisitor stmtVisitor = new StmtVisitor();
			stmtVisitor.visit(n, arg);*/
			System.out.println(n.getBegin().get().line);
			System.out.println(n.getEnd().get().line);
		}
	}
}
