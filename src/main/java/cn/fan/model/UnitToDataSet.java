package cn.fan.model;

import java.util.HashMap;
import java.util.List;

import soot.toolkits.scalar.FlowSet;

public class UnitToDataSet {
    private String unitLine;
    // 节点的输入变量
    private FlowSet<String> inFlowSet;
    // 每一个变量对应的行号
    private HashMap<String, List<String>> valueToLine;

    public UnitToDataSet(String unit, FlowSet<String> inFlowSet, HashMap<String, List<String>> valueToLine) {
        super();
        this.unitLine = unit;
        this.inFlowSet = inFlowSet;
        this.valueToLine = valueToLine;
    }

    public String getUnit() {
        return unitLine;
    }

    public void setUnit(String unit) {
        this.unitLine = unit;
    }

    public FlowSet<String> getInFlowSet() {
        return inFlowSet;
    }

    public void setInFlowSet(FlowSet<String> inFlowSet) {
        this.inFlowSet = inFlowSet;
    }

    public HashMap<String, List<String>> getValueToLine() {
        return valueToLine;
    }

    public void setValueToLine(HashMap<String, List<String>> valueToLine) {
        this.valueToLine = valueToLine;
    }

    @Override
    public String toString() {
        return "UnitToDataSet [unit=" + unitLine + ", inFlowSet=" + inFlowSet + ", valueToLine=" + valueToLine + "]";
    }

}
