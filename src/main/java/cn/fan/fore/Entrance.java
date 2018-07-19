package cn.fan.fore;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.AstNode;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;
import cn.fan.tool.LoadSourceCode;

import com.github.javaparser.ast.CompilationUnit;

public class Entrance {
    public static void main(String[] args) {
        PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst("Ast_1.java");
        CompilationUnit cu = paserSourceCodeToAst.getCompilationUnit();
        AstNode root = new AstNode();
        AstNodeInit astNodeInit = new AstNodeInit(true, root);
        astNodeInit.Init(cu);
        // 入口方法
        List<String> methods = new ArrayList<String>();
        methods.add("way");
        methods.add("way2");
        methods.add("main");
        MethodTransformer methodTransformer = new MethodTransformer(methods);
        PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
        try {
            // 需要解析的jar包和主入口类
            paserClassToCfg.parseCfg(methodTransformer, LoadSourceCode.load(SourceSubPathEnum.JAR_SOURCE_PATH, "Analyze.jar").toString(), "cn.li.easy.AST_1");
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DotPrinterAst dotPrinterAst = new DotPrinterAst(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges());
        String output = dotPrinterAst.output(root);
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\one\\AST\\AST_3.dot"); PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(output);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("dot文件生成完毕!");
    }
}
