package cn.fan.cfg.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.FlowSet;
import cn.fan.model.DataFlowNode;
import cn.fan.model.Edge;
import cn.fan.model.UnitToDataSet;

/**
 * 对于class中方法 怎么处理
 * 
 * @author fan
 *
 */
public class MethodTransformer extends BodyTransformer {

    private List<String> methodNames;

    // 方法的类型 [[方法名字,方法的参数个数,行号,类型]],这个作用就是生成ast_cfg_pdg的dot文件名字能和callgraph对应上
    private List<ArrayList<String>> method_ParaTypes;
    /**
     * 所有的cfg边 {方法中：cfg边} 有个问题就是方法名字不唯一，不能
     */
    public HashMap<String, Set<Edge>> methodToCfgEdges;

    /*
     * 所有的DataFlow边 {方法中：dataflow边}
     */
    public HashMap<String, Set<Edge>> methodToallDataFlowEdges;

    public MethodTransformer(List<String> methodNames) {
        // TODO Auto-generated constructor stub
        this.methodNames = methodNames;
        methodToCfgEdges = new HashMap<String, Set<Edge>>();
        methodToallDataFlowEdges = new HashMap<String, Set<Edge>>();
        method_ParaTypes = new ArrayList<ArrayList<String>>();
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        // TODO Auto-generated method stub
        for (String methodName : methodNames) {
            if (b.getMethod().getName().toString().equals(methodName)) {
                BriefUnitGraph directGraph = new BriefUnitGraph(b);
                // 一个方法就算有重载，所有的相同方法 名的边都会加到一个边的集合中
                if (methodToCfgEdges.get(methodName) == null) {
                    methodToCfgEdges.put(methodName, getCfgEdges(directGraph));
                }
                else {
                    methodToCfgEdges.get(methodName).addAll(getCfgEdges(directGraph));
                }
                if (methodToallDataFlowEdges.get(methodName) == null) {
                    methodToallDataFlowEdges.put(methodName, getDataFlowEdges(directGraph));
                }
                else {
                    methodToallDataFlowEdges.get(methodName).addAll(getDataFlowEdges(directGraph));
                }
                // 加入行号信息
                String methodname = b.getMethod().getName();
                String classname = b.getMethod().getDeclaringClass().getName().replaceAll("\\$", "\\.");
                String fileName = classname + "." + methodname + b.getMethod().getParameterTypes().toString().replace('[', '(').replace(']', ')');
                ArrayList<String> sigMethod = new ArrayList<String>();
                sigMethod.add(methodName);
                sigMethod.add(String.valueOf(b.getMethod().getParameterCount()));
                sigMethod.add(String.valueOf(b.getMethod().getParameterTypes()));
                sigMethod.add(String.valueOf(b.getMethod().getJavaSourceStartLineNumber()));
                sigMethod.add(fileName);
                method_ParaTypes.add(sigMethod);
            }
        }
    }

    /**
     * 
     * @Title: getDataFlowEdge
     * @Description: TODO 根据cfg图进行数据流分析 获取数据流的边
     * @author LiRongFan
     * @param b方法体的jimple表达
     * @return 不重复而且头尾都是不同的行号的边
     * @throws
     */
    private Set<Edge> getDataFlowEdges(BriefUnitGraph directGraph) {
        Set<Edge> dataFlowEdges = new HashSet<Edge>();
        DataFlowAnalysisDefined dataFlowAnalysis = new DataFlowAnalysisDefined(directGraph);
        HashMap<Unit, UnitToDataSet> unitToDataSet = dataFlowAnalysis.getUnitToDataSet();
        Iterator<Unit> iterator = directGraph.iterator();
        while (iterator.hasNext()) {
            Unit next = iterator.next();
            UnitToDataSet edgeInfo = unitToDataSet.get(next);
            // 后继节点
            String succ = edgeInfo.getUnitLine();
            // 前驱节点
            if (!succ.equals("NAN")) {// 首先后继节点有行号
                FlowSet<DataFlowNode> inFlowSet = edgeInfo.getInFlowSet();
                for (DataFlowNode node : inFlowSet) {
                    // 同一行不处理而且没有行号的也不是边
                    if (!node.getLineNum().equals(succ) && !node.getLineNum().equals("NAN")) {
                        dataFlowEdges.add(new Edge(node.getLineNum(), succ));
                    }
                }
            }
        }
        return dataFlowEdges;
    }

    /**
     * @Title: initInfo
     * @Description: 将行号和每个unit对应起来 unitToLineMap的作用！
     *               allUnitsOfLine作用就是抽取相同行的所有unit {10：{r1,r2,r3}}
     * @author Dangzhang
     * @param b
     * @throws
     */
    private Set<Edge> getCfgEdges(BriefUnitGraph directGraph) {
        HashMap<Unit, String> unitToLineMap = new HashMap<Unit, String>(16);
        Set<Edge> cfgEdges = new HashSet<Edge>();
        Iterator<Unit> iterator = directGraph.iterator();
        while (iterator.hasNext()) {
            Unit unit = iterator.next();
            unitToLineMap.put(unit, canAddLineNum(unit));
        }
        for (Iterator<Unit> nodesIt = directGraph.iterator(); nodesIt.hasNext();) {
            Unit currentUnit = nodesIt.next();
            // 前驱节点有行号才有意义
            String predLine = unitToLineMap.get(currentUnit);
            if (!predLine.equals("NAN")) {
                for (Iterator<Unit> succes = directGraph.getSuccsOf(currentUnit).iterator(); succes.hasNext();) {
                    // 后继节点
                    Unit succ = succes.next();
                    String succLine = unitToLineMap.get(succ);
                    if (!succLine.equals("NAN") && !succLine.equals(predLine)) {
                        cfgEdges.add(new Edge(predLine, succLine));
                    }
                }
            }
        }
        return cfgEdges;
    }

    /**
     * 
     * @Title: canAddLineNum
     * @Description: 用于判断该unit是否有行号
     * @author LiRongFan
     * @param unit
     * @return 布尔值
     * @throws
     */
    private String canAddLineNum(Unit unit) {
        String flag = "NAN";
        LineNumberTag tagU = (LineNumberTag) unit.getTag("LineNumberTag");
        if (tagU != null && tagU.getLineNumber() > 0) {
            flag = String.valueOf(tagU.getLineNumber());
        }
        return flag;
    }

    public List<String> getMethodNames() {
        return methodNames;
    }

    public void setMethodNames(List<String> methodNames) {
        this.methodNames = methodNames;
    }

    public List<ArrayList<String>> getMethod_ParaTypes() {
        return method_ParaTypes;
    }

    public HashMap<String, Set<Edge>> getMethodToCfgEdges() {
        return methodToCfgEdges;
    }

    public void setMethodToCfgEdges(HashMap<String, Set<Edge>> methodToCfgEdges) {
        this.methodToCfgEdges = methodToCfgEdges;
    }

    public HashMap<String, Set<Edge>> getMethodToallDataFlowEdges() {
        return methodToallDataFlowEdges;
    }

    public void setMethodToallDataFlowEdges(HashMap<String, Set<Edge>> methodToallDataFlowEdges) {
        this.methodToallDataFlowEdges = methodToallDataFlowEdges;
    }

}
