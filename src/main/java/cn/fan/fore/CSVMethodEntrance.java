package cn.fan.fore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import cn.fan.ast.visitor.CSVHandAdapter;
import cn.fan.cfg.assistant.MethodTransformer;
import cn.fan.model.AstNode;
import cn.fan.service.PaserClassToCfg;
import cn.fan.service.PaserSourceCodeToAst;
import cn.fan.tool.ExtractClassName;

import com.github.javaparser.ast.CompilationUnit;

public class CSVMethodEntrance {

    public static void main(String[] args) {
        List<String> canIn = new ArrayList<String>();
        canIn.add("src");
        canIn.add("lib");
        List<String> notIn = new ArrayList<String>();
        notIn.add("integrationtest");
        notIn.add("examples");
        notIn.add("contrib");
        notIn.add("documentation");
        notIn.add("models");
        notIn.add("resources");
        notIn.add("types");
        notIn.add("testcases");
        ExtractClassName extractClassName = new ExtractClassName(canIn, notIn);
        // 一个项目的所有java文件路径 已经入口package.className
        HashMap<String, String> paths = new HashMap<String, String>();
        // 该项目所依赖的jar
        List<String> jarsPath = new ArrayList<String>();
        File projectFile = new File("D:\\JarAndSource\\spring-cloud-config-server");
        // 在项目下面创建一个AST_CFG_PDGInfo目录放dot文件
        File aimPathFile = new File(projectFile.getAbsolutePath() + File.separator + "AST_CFG_PDGInfo");
        aimPathFile.mkdir();
        extractClassName.findJavaFile(projectFile, paths, jarsPath);
        // 获取到了paths和jarsPath
        for (Entry<String, String> pathToClassName : paths.entrySet()) {
            String className = pathToClassName.getKey();
            String name = className.substring(className.lastIndexOf(".") + 1);
            System.out.println(name);
            if (name.equals("MDPFactory")) {
                System.out.println("停下");
            }
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

            // csv 处理方式
            CSVHandAdapter csvHandAdapter = new CSVHandAdapter(new File(aimPathFile.getAbsolutePath() + File.separator + (name + "$" + UUID.randomUUID()) + "Node.csv"), new File(
                    aimPathFile.getAbsolutePath() + File.separator + (name + "$" + UUID.randomUUID()) + "Edge.csv"));
            FormatFilePattern filePattern = new FormatFilePattern(true, true, methodTransformer.getMethodToCfgEdges(), methodTransformer.getMethodToallDataFlowEdges(),
                    csvHandAdapter);
            filePattern.output(root);
            csvHandAdapter.close();
            System.out.println("csv文件生成完毕!");
        }
        System.out.println("结束");
    }
}
