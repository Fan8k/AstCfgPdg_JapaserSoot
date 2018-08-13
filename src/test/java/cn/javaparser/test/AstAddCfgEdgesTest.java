package cn.javaparser.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.fore.AstNodeInit;
import cn.fan.fore.DotMethodAst;
import cn.fan.model.AstNode;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

public class AstAddCfgEdgesTest {
    public static void main(String[] args) throws IOException {
        SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(StatementsInMethodTest.class).resolve("src/test/resource/SourceCode"));

        /*
         * MethodVisitor methodVisitor = new MethodVisitor();
         * methodVisitor.visit(cu, null);
         */

        /* DotPrintAstTest dotPrintAstTest = new DotPrintAstTest(true); */
        PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst("C:\\Users\\fan\\git\\AstCfgPdg_JapaserSoot\\src\\test\\resource\\SourceCode\\", "Test.java");
        CompilationUnit cu = paserSourceCodeToAst.getCompilationUnit();
        AstNode root = new AstNode();
        AstNodeInit astNodeInit = new AstNodeInit(true, root);
        astNodeInit.Init(cu);
        // 入口方法
        List<String> methods = paserSourceCodeToAst.getMethodNames();
        System.out.println(methods);
        MethodTransformer methodTransformer = new MethodTransformer(methods);
        PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
        // 需要解析的jar包和主入口类
        List<String> jarList = new ArrayList<String>();
        jarList.add("C:\\Users\\fan\\Desktop\\Data\\Analyze.jar");
        paserClassToCfg.parseCfg(methodTransformer, jarList, "SourceCode.Test");

        DotMethodAst dotMethodAst = new DotMethodAst(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges());
        List<MethodDeclaration> findFirst = cu.findAll(MethodDeclaration.class);
        String output = dotMethodAst.output(findFirst.get(0));
        System.out.println(output);
        String output2 = dotMethodAst.output(findFirst.get(1));
        System.out.println(output2);

        /*
         * DotPrinter printer = new DotPrinter(true); try (FileWriter fileWriter
         * = new
         * FileWriter("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\one\\AST\\AST_3.dot"
         * ); PrintWriter printWriter = new PrintWriter(fileWriter)) {
         * printWriter.print(printer.output(cu)); } System.out.println("输出完毕！");
         */
    }
}
