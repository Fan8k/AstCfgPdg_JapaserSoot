package cn.fan.cfg.assistant;

import java.util.HashMap;
import java.util.Iterator;

import soot.Unit;
import soot.ValueBox;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
import cn.fan.model.DataFlowNode;
import cn.fan.model.UnitToDataSet;

/**
 * 
 * 
 * @ClassName: DataFlowAnalysisDefined
 * @Description: 数据流分析 只是记录数据是否用过 ！
 *               更新就是如果当前没有定义和以前一样的变量就直接加入输出，否则就是就去掉之前定义的变量，加入当前定义的变量
 * @author fan
 * @date 2018年7月18日 下午4:40:48
 *
 */
public class DataFlowAnalysisDefined extends ForwardFlowAnalysis<Unit, FlowSet<DataFlowNode>> {

    private HashMap<Unit, UnitToDataSet> unitToDataSet;

    private FlowSet<DataFlowNode> emptySet;

    public DataFlowAnalysisDefined(DirectedGraph<Unit> graph) {
        super(graph);
        emptySet = new ArraySparseSet<DataFlowNode>();
        unitToDataSet = new HashMap<Unit, UnitToDataSet>(16);
        doAnalysis();
    }

    @Override
    protected void flowThrough(FlowSet<DataFlowNode> in, Unit d, FlowSet<DataFlowNode> out) {
        // TODO Auto-generated method stub
        filter(in, d, out);
    }

    private void filter(FlowSet<DataFlowNode> inSet, Unit u, FlowSet<DataFlowNode> outSet) {
        // TODO Auto-generated method stub
        // 所有使用的变量,专门判断哪些变量当前unit使用过，说明有使用过的inset都是前驱
        Iterator<ValueBox> useVariablesIterator = u.getUseBoxes().iterator();
        FlowSet<DataFlowNode> inFlowSet = emptySet.clone();
        while (useVariablesIterator.hasNext()) {
            ValueBox usedVar = useVariablesIterator.next();
            // 看看使用的变量是否出现在之前的定义中
            for (DataFlowNode dataNode : inSet) {
                if (dataNode.getVariableName().equals(usedVar.getValue().toString())) {
                    inFlowSet.add(dataNode);
                }
            }
        }
        // 让一个unit记住当前的时刻
        String lineNum = canAddLineNum(u);
        unitToDataSet.put(u, new UnitToDataSet(lineNum, inFlowSet));
        // 处理输出集 遇到定义就更新，更新的方法就是删除之前所有的名字相同的变量
        FlowSet<DataFlowNode> currentDefineVariableSet = emptySet.clone();// Unit的kills
        FlowSet<DataFlowNode> middleSet = emptySet.clone();
        Iterator<ValueBox> defIterator = u.getDefBoxes().iterator();
        String lineNum1 = null;
        while (defIterator.hasNext()) {
            lineNum1 = canAddLineNum(u);
            currentDefineVariableSet.add(new DataFlowNode(defIterator.next().getValue().toString(), lineNum1));
        }
        // 判断相交只能用变量名字，名相同就要更新
        for (DataFlowNode inNode : inSet) {
            for (DataFlowNode cNode : currentDefineVariableSet) {
                if (inNode.getVariableName().equals(cNode.getVariableName())) {
                    middleSet.add(inNode);
                    break;
                }
            }
        }
        if (middleSet.isEmpty()) { // 没有重复的
            inSet.union(currentDefineVariableSet, outSet);
        }
        else {
            inSet.difference(middleSet);
            inSet.union(currentDefineVariableSet, outSet);
        }
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

    @Override
    protected FlowSet<DataFlowNode> newInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected FlowSet<DataFlowNode> entryInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected void merge(FlowSet<DataFlowNode> in1, FlowSet<DataFlowNode> in2, FlowSet<DataFlowNode> out) {
        // TODO Auto-generated method stub
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<DataFlowNode> source, FlowSet<DataFlowNode> dest) {
        // TODO Auto-generated method stub
        source.copy(dest);
    }

    public HashMap<Unit, UnitToDataSet> getUnitToDataSet() {
        return unitToDataSet;
    }

    public void setUnitToDataSet(HashMap<Unit, UnitToDataSet> unitToDataSet) {
        this.unitToDataSet = unitToDataSet;
    }
}
