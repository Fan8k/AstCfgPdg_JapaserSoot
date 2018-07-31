package cn.fan.service;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.fan.ast.visitor.MethodVisitor;
import cn.fan.ast.visitor.PackageVisitor;
import cn.fan.model.SourceSubPathEnum;
import cn.fan.tool.LoadSourceCode;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.SourceRoot;

/**
 * 利用javaPaser 解析语法树
 * 
 * @author fan
 *
 */
public class PaserSourceCodeToAst {
    private Logger logger = Logger.getLogger(PaserSourceCodeToAst.class);
    /**
     * Soot AST树的树根
     */
    private CompilationUnit compilationUnit;
    /**
     * java sourceCode的原路径
     */
    private Path path;

    /**
     * @param filename
     *            XXX.java
     */
    public PaserSourceCodeToAst(String filename) {
        try {
            this.path = LoadSourceCode.load(SourceSubPathEnum.SOURCE_CODE_PATH, "");
        }
        catch (URISyntaxException e) {
            logger.error(e.getStackTrace());
            e.printStackTrace();
        }
        compilationUnit = new SourceRoot(path).parse("", filename);
    }

    public PaserSourceCodeToAst(String absPath, String filename) {
        compilationUnit = new SourceRoot(Paths.get(absPath)).parse("", filename);
    }

    /**
     * 根据方法的名字解析某一个方法 获取方法的开始到结束的行号
     * 
     * @param methodName
     *            方法的名字
     */
    public List<String> getMethodNames() {
        Set<String> set = new HashSet<String>();
        MethodVisitor methodVisitor = new MethodVisitor();
        methodVisitor.visit(compilationUnit, set);
        return new ArrayList<String>(set);
    }

    public String getPackageName() {
        String name = "";
        PackageVisitor packageVisitor = new PackageVisitor();
        packageVisitor.visit(compilationUnit, name);
        return name;
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

}
