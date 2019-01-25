package cn.fan.service;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fan.tool.ExtractClassName;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;

/**
 * AST节点 序列化为SBT
 * 
 * @ClassName: PaserAstToSBT
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author LiRongFan
 * @date 2019年1月7日 下午2:22:17
 *
 */
public class PaserAstToSBT {
    private Pattern p;
    private Pattern dotP;
    private Pattern quotationP;

    public PaserAstToSBT() {
        // TODO Auto-generated constructor stub
        p = Pattern.compile("\r|\n|\r\n");
        dotP = Pattern.compile(",");
        quotationP = Pattern.compile("\"");
    }

    public String paserToSBT(MethodDeclaration root) {
        StringBuilder sbt = new StringBuilder();
        System.out.println("start");
        walk(root, "root", sbt);
        return sbt.toString();
    }

    // 遍历AST各个节点，所以需要了解各个节点的存储类型
    public String walk(Node node, String name, StringBuilder sbt) {
        assertNotNull(node);
        NodeMetaModel metaModel = node.getMetaModel();
        List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
        List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(toList());

        String info = name + "(" + metaModel.getTypeName() + ")";
        sbt.append("(" + filter(info));
        // 属性子节点 直接归属于node 而且都是最终的节点
        for (PropertyMetaModel a : attributes) {
            String temp = a.getName() + "='" + a.getValue(node).toString() + "'";
            // 直接加上终止符
            sbt.append("(" + filter(temp) + ")" + filter(temp));
        }

        // node子节点
        for (PropertyMetaModel sn : subNodes) {
            Node nd = (Node) sn.getValue(node);
            if (nd != null) {
                walk(nd, sn.getName(), sbt);
            }
        }

        // list子节点
        for (PropertyMetaModel sl : subLists) {
            @SuppressWarnings("unchecked")
            NodeList<? extends Node> nl = (NodeList<? extends Node>) sl.getValue(node);
            if (nl != null && nl.isNonEmpty()) {
                // 注意这个是子节点但不是终止节点 可以成根节点
                sbt.append("(" + filter(sl.getName()));
                String slName = sl.getName().substring(0, sl.getName().length() - 1);
                for (Node nd : nl)
                    // output(nd, slName);
                    walk(nd, slName, sbt);
            }
            sbt.append(")" + filter(sl.getName()));
        }
        sbt.append(")" + filter(info));
        return sbt.toString();
    }

    public String filter(String str) {
        Matcher matcher = p.matcher(str);
        Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
        Matcher matcher3 = quotationP.matcher(matcher2.replaceAll("."));
        return matcher3.replaceAll("'");
    }

    public static void main(String[] args) {
        // 测试AST到SBT
        PaserAstToSBT astToSbt = new PaserAstToSBT();

        List<String> canIn = new ArrayList<String>();
        canIn.add("src");
        canIn.add("lib");
        List<String> notIn = new ArrayList<String>();
        notIn.add("bin");
        notIn.add("test");

        ExtractClassName extractClassName = new ExtractClassName(canIn, notIn);
        // 一个项目的所有java文件路径 已经入口package.className
        HashMap<String, String> paths = new HashMap<String, String>();
        // 该项目所依赖的jar
        List<String> jarsPath = new ArrayList<String>();
        File projectFile = new File("C:\\Users\\fan\\Desktop\\AST+CFG+PDG\\java-algorithms-implementation");
        // 在项目下面创建一个AST_CFG_PDGInfo目录放dot文件
        File aimPathFile = new File(projectFile.getAbsolutePath() + File.separator + "AST_CFG_PDGdotInfo");
        if (!aimPathFile.exists()) {
            aimPathFile.mkdir();
        }
        extractClassName.findJavaFile(projectFile, paths, jarsPath);

        for (Entry<String, String> pathToClassName : paths.entrySet()) {
            String className = pathToClassName.getKey();
            // 去除package前缀
            String name = className.substring(className.lastIndexOf(".") + 1);
            System.out.println(name);

            PaserSourceCodeToAst paserSourceCodeToAst = new PaserSourceCodeToAst(pathToClassName.getValue(), name + ".java");
            // 获取整个根的目录
            CompilationUnit cu = paserSourceCodeToAst.getCompilationUnit();

            // 生成当个method文件
            List<MethodDeclaration> methodDeclarations = cu.findAll(MethodDeclaration.class);
            for (MethodDeclaration methodDeclaration : methodDeclarations) {
                // 每个method 作为一个node 传入其中
                String sbt = astToSbt.paserToSBT(methodDeclaration);
                System.out.println(sbt);

                // 执行一次就跳出去
                break;
            }
            // 执行一次就跳出去
            break;
        }

    }
}
