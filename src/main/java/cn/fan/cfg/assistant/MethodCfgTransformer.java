package cn.fan.cfg.assistant;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Unit;
import soot.options.Options;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;
import soot.util.cfgcmd.CFGToDotGraph;
import soot.util.dot.DotGraph;

/**
 * 只是生成纯的cfg的dot图
 * 
 * @ClassName: MethodCfgTransformer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author LiRongFan
 * @date 2019年1月16日 下午4:05:32
 *
 */
public class MethodCfgTransformer extends BodyTransformer {

    private String filePath;
    private int index;

    public MethodCfgTransformer(String filePath) {
        // TODO Auto-generated constructor stub
        this.filePath = filePath;
        this.index = 0;
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {

        // 生成graph

        CFGGraphType graphType = CFGGraphType.getGraphType("BriefUnitGraph");
        @SuppressWarnings("unchecked")
        DirectedGraph<Unit> buildGraph = graphType.buildGraph(b);

        // 生成dot文件
        CFGToDotGraph dotGraph = new CFGToDotGraph(); //
        // dotGraph.setBriefLabels(true);
        dotGraph.setOnePage(true); // 是否多页表示
        dotGraph.setUnexceptionalControlFlowAttr("color", "black");
        dotGraph.setExceptionalControlFlowAttr("color", "red");
        dotGraph.setExceptionEdgeAttr("color", "lightgray");
        dotGraph.setShowExceptions(Options.v().show_exception_dests());

        DotGraph canvas = graphType.drawGraph(dotGraph, buildGraph, b);
        String methodname = b.getMethod().getName();
        System.out.println(methodname);
        String classname = b.getMethod().getDeclaringClass().getName().replaceAll("\\$", "\\.");

        String fileName = classname + "." + methodname + b.getMethod().getParameterTypes().toString().replace('[', '(').replace(']', ')');
        this.write(this.filePath, fileName, String.valueOf(index));
        G.v().out.println("Generate dot file in " + index + "_CFG.dot");
        canvas.plot(this.filePath + (index++) + "_CFG.dot");
    }

    public void write(String path, String fileName, String key) {
        try {
            FileWriter fileWriter = new FileWriter(new File(path + "cfg_map.txt"), true);
            fileWriter.write(key + ":" + fileName + "\r\n");
            fileWriter.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
