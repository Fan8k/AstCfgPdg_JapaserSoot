package cn.fan.fore;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.AstNode;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;
import cn.fan.tool.LoadSourceCode;

import com.github.javaparser.ast.CompilationUnit;

public class Entrance {
    private static List<String> allNeadList;
    static {
        allNeadList = new ArrayList<String>();
        allNeadList.add("com.parable.SemapTest");
        allNeadList.add("com.parable.ThreadStop");
        allNeadList.add("com.parable.VoliTest");
    }

    public static void main(String[] args) {
        for (String className : allNeadList) {
            String name = className.substring(className.lastIndexOf(".") + 1);
            System.out.println(name);
            PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst(name + ".java");
            CompilationUnit cu = paserSourceCodeToAst.getCompilationUnit();
            AstNode root = new AstNode();
            AstNodeInit astNodeInit = new AstNodeInit(true, root);
            astNodeInit.Init(cu);
            // 入口方法
            List<String> methods = paserSourceCodeToAst.getMethodNames();
            System.out.println(methods);
            MethodTransformer methodTransformer = new MethodTransformer(methods);
            PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
            try {
                // 需要解析的jar包和主入口类
                paserClassToCfg.parseCfg(methodTransformer, LoadSourceCode.load(SourceSubPathEnum.JAR_SOURCE_PATH, "Analyze.jar").toString(), className);
            }
            catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DotPrinterAst dotPrinterAst = new DotPrinterAst(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges());
            String output = dotPrinterAst.output(root);
            try (FileWriter fileWriter = new FileWriter("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\one\\AST\\" + (name + UUID.randomUUID()) + ".dot");
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.print(output);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("dot文件生成完毕!");
        }
    }
}
