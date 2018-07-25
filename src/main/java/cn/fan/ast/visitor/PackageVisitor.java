package cn.fan.ast.visitor;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class PackageVisitor extends VoidVisitorAdapter<String> {

    @Override
    public void visit(PackageDeclaration n, String arg) {
        // TODO Auto-generated method stub
        arg = n.getNameAsString();
    }
}
