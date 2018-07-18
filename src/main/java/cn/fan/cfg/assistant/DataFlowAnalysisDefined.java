package cn.fan.cfg.assistant;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;
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
public class DataFlowAnalysisDefined extends ForwardFlowAnalysis<Unit, FlowSet<String>> {

    private HashMap<Unit, UnitToDataSet> unitToDataSet;

    private FlowSet<String> emptySet;

    /**
     * 用来记录所有定义变量的位置 包括改变变量的位置 list 就是记录所有定义过变量的位置 {a,{1,2,3}}
     */
    private HashMap<String, List<String>> variableToLines = new HashMap<String, List<String>>();

    public DataFlowAnalysisDefined(DirectedGraph<Unit> graph) {
        super(graph);
        emptySet = new ArraySparseSet<String>();
        unitToDataSet = new HashMap<Unit, UnitToDataSet>(16);
        doAnalysis();
    }

    @Override
    protected void flowThrough(FlowSet<String> in, Unit d, FlowSet<String> out) {
        // TODO Auto-generated method stub
        filter(in, d, out);
    }

    private void filter(FlowSet<String> inSet, Unit u, FlowSet<String> outSet) {
        // TODO Auto-generated method stub
        FlowSet<String> currentDefineVariableSet = emptySet.clone();// Unit的kills
        FlowSet<String> middleSet = emptySet.clone();// Unit的kills
        Iterator<ValueBox> useVariablesIterator = u.getUseBoxes().iterator(); // 所有使用的变量
        FlowSet<String> inFlowSet = new ArraySparseSet<String>();
        while (useVariablesIterator.hasNext()) {
            ValueBox next = useVariablesIterator.next();
            // 看看使用的变量是否出现在之前的定义中
            for (String value : inSet) {
                if (value.equals(next.getValue().toString())) {
                    inFlowSet.add(value);
                }
            }
        }
        HashMap<String, List<String>> currentState = new HashMap<String, List<String>>();
        for (String s : inFlowSet) {
            if (variableToLines.get(s) != null) {
                currentState.put(s, variableToLines.get(s));
            }
        }
        // 让一个unit记住当前的时刻
        LineNumberTag tagU = (LineNumberTag) u.getTag("LineNumberTag");
        if (tagU != null) {
            unitToDataSet.put(u, new UnitToDataSet(tagU.getLineNumber() + "", inFlowSet, deepClone(currentState)));
        }
        // 当前定义的变量如果和之前定义的变量value一样就要被杀死
        HashMap<String, Integer> currentDefinedVariableToLine = new HashMap<String, Integer>();
        Iterator<ValueBox> defIterator = u.getDefBoxes().iterator();
        while (defIterator.hasNext()) {
            Value value = defIterator.next().getValue();
            currentDefineVariableSet.add(value.toString());
            // 记录value的行号
            LineNumberTag tag = (LineNumberTag) u.getTag("LineNumberTag");
            if (tag != null) {
                currentDefinedVariableToLine.put(value.toString(), tag.getLineNumber());
            }
        }
        inSet.intersection(currentDefineVariableSet, middleSet);
        if (middleSet.isEmpty()) { // 如果中间定义为空 那就是该unit 定义都是新的变量
            inSet.union(currentDefineVariableSet, outSet);
            for (java.util.Map.Entry<String, Integer> entry : currentDefinedVariableToLine.entrySet()) {
                ArrayList<String> arrayList = new ArrayList<String>();
                arrayList.add(String.valueOf(entry.getValue()));
                variableToLines.put(entry.getKey(), arrayList);
            }
        }
        else {
            // 这一步可以不要
            inSet.difference(middleSet); // 去掉以前定义的
            inSet.union(currentDefineVariableSet, outSet); // 加入新定义的
            // 记录下从新定义的变量的新位置
            Iterator<String> iterator = middleSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next().toString();
                Integer integer = currentDefinedVariableToLine.get(next);
                variableToLines.get(next).add(String.valueOf(integer));
            }
        }

    }

    @Override
    protected FlowSet<String> newInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected FlowSet<String> entryInitialFlow() {
        // TODO Auto-generated method stub
        return emptySet.emptySet();
    }

    @Override
    protected void merge(FlowSet<String> in1, FlowSet<String> in2, FlowSet<String> out) {
        // TODO Auto-generated method stub
        in1.union(in2, out);
    }

    @Override
    protected void copy(FlowSet<String> source, FlowSet<String> dest) {
        // TODO Auto-generated method stub
        source.copy(dest);
    }

    public HashMap<Unit, UnitToDataSet> getUnitToDataSet() {
        return unitToDataSet;
    }

    public void setUnitToDataSet(HashMap<Unit, UnitToDataSet> unitToDataSet) {
        this.unitToDataSet = unitToDataSet;
    }

    /**
     * 深度克隆 利用序列化 因为每一个unit需要记住当时输入节点的状态，而非全局状态
     */
    @SuppressWarnings("unchecked")
    private HashMap<String, List<String>> deepClone(HashMap<String, List<String>> varToLines) {
        HashMap<String, List<String>> result = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(varToLines);
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            result = (HashMap<String, List<String>>) ois.readObject();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                bos.close();
                oos.close();
                bis.close();
                ois.close();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
}
