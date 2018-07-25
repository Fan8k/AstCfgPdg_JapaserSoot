package cn.fan.ast.visitor;

import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Set<String>> {

    @Override
    public void visit(MethodDeclaration n, Set<String> set) {
        // TODO Auto-generated method stub
        super.visit(n, set);
        // 获取method的首行和结束行
        set.add(n.getNameAsString());

    }

}
