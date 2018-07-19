package cn.fan.fore;


import java.net.URISyntaxException;

import java.util.List;

import cn.fan.cfg.assistant.MethodTransformer;

import cn.fan.model.SourceSubPathEnum;
import cn.fan.service.PaserClassToCfg;
import cn.fan.tool.LoadSourceCode;


/**
 * ast树中加入cfg的边 思想就是cfg中已经拿到了对应源代码不同行号的的边，所以只需要在ast中找目标行号 加入一个statement就行了
 * 
 * @author fan
 *
 */
public class AstAddCfgEdges {
    // 先加载cfg 获取cfg中所有的边
    public static MethodTransformer loadCfg(List<String> ways) {

        MethodTransformer methodTransformer = new MethodTransformer(ways);
        PaserClassToCfg<MethodTransformer> paserClassToCfg = new PaserClassToCfg<MethodTransformer>();
        try {
            // 需要解析的jar包和主入口类
            paserClassToCfg.parseCfg(methodTransformer, LoadSourceCode.load(SourceSubPathEnum.JAR_SOURCE_PATH, "Analyze.jar").toString(), "cn.li.easy.AST_1");
        }
        catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return methodTransformer;
    }

    public static void main(String[] args) {
       /* SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(StatementsInMethodTest.class).resolve("src/main/resource/SourceCode"));
        CompilationUnit cu = sourceRoot.parse("", "AST_1.java");
        // 初始化自身的数据结构-自定义ast数据结构
        AstNode root = new AstNode();
        AstNodeInit astNodeInit = new AstNodeInit(true, root);
        astNodeInit.Init(cu);
        // 获取cfg的中所有边
        HashMap<Integer, CfgNode<String>> allEdgesFromcfg = loadCfg();
        // 自定义打印dot
        DotPrinterAst dotPrinterAst = new DotPrinterAst(true, true, allEdgesFromcfg);
        String output = dotPrinterAst.output(root);
        try (FileWriter fileWriter = new FileWriter("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\one\\AST\\AST_3.dot"); PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.print(output);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("dot文件生成完毕!");*/
    }
}