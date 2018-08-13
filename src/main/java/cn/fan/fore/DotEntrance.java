package cn.fan.fore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.log4j.Logger;

import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.AstNode;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;
import cn.fan.tool.ExtractClassName;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class DotEntrance {
    private static Logger logger = Logger.getLogger(DotEntrance.class);
    private static List<String> allNeadList;

    public static void main(String[] args) {
        ExtractClassName extractClassName = new ExtractClassName();
        // 一个项目的所有java文件路径 已经入口package.className
        HashMap<String, String> paths = new HashMap<String, String>();
        // 该项目所依赖的jar
        List<String> jarsPath = new ArrayList<String>();
        File projectFile = new File("D:\\JarAndSource\\spring-cloud-config-server");
        // 在项目下面创建一个AST_CFG_PDGInfo目录放dot文件
        File aimPathFile = new File(projectFile.getAbsolutePath() + File.separator + "AST_CFG_PDGdotInfo");
        aimPathFile.mkdir();
        extractClassName.findJavaFile(projectFile, paths, jarsPath);
        // 获取到了paths和jarsPath
        for (Entry<String, String> pathToClassName : paths.entrySet()) {
            String className = pathToClassName.getKey();
            String name = className.substring(className.lastIndexOf(".") + 1);
            System.out.println(name);
            PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst(pathToClassName.getValue(), name + ".java");
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

            paserClassToCfg.parseCfg(methodTransformer, jarsPath, className);

            // 更具java名字在建立一级目录
            File methodDotDirectory = new File(aimPathFile.getAbsolutePath() + File.separator + name);
            methodDotDirectory.mkdir();

            DotPrinterAst dotPrinterAst = new DotPrinterAst(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges());
            String output = dotPrinterAst.output(root);

            try (FileWriter fileWriter = new FileWriter(methodDotDirectory.getAbsolutePath() + File.separator + (name + UUID.randomUUID()) + ".dot");
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {
                printWriter.print(output);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("dot文件生成完毕!");
            // 生成当个method文件
            DotMethodAst dotMethodAst = new DotMethodAst(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges());
            List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                String output2 = dotMethodAst.output(methodDeclaration);
                try (FileWriter fileWriter = new FileWriter(methodDotDirectory.getAbsolutePath() + File.separator + (methodDeclaration.getNameAsString() + UUID.randomUUID())
                        + ".dot");
                        PrintWriter printWriter = new PrintWriter(fileWriter)) {
                    printWriter.print(output2);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("mmethod dot文件生成完毕!");
        }
        System.out.println("dot文件全部生成完毕!");
    }
}
