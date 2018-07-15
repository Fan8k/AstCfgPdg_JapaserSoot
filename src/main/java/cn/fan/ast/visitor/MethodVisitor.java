package cn.fan.ast.visitor;

import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<List<Integer>> {
	private String methodName;

	public MethodVisitor(String methodName) {
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
	}

	@Override
	public void visit(MethodDeclaration n, List<Integer> arg) {
		// TODO Auto-generated method stub
		super.visit(n, arg);
		//获取method的首行和结束行 
		if (n.getName().equals(methodName)) {
			arg.add(n.getBegin().get().line + 1);
			arg.add(n.getEnd().get().line - 1);
		}
	}

}
