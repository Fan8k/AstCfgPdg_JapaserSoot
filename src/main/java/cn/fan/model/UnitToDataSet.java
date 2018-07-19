package cn.fan.model;

import soot.toolkits.scalar.FlowSet;

public class UnitToDataSet {
    private String unitLine;
    // 节点的输入变量
    private FlowSet<DataFlowNode> inFlowSet;

    public UnitToDataSet(String unit, FlowSet<DataFlowNode> inFlowSet) {
        super();
        this.unitLine = unit;
        this.inFlowSet = inFlowSet;
    }

    public FlowSet<DataFlowNode> getInFlowSet() {
        return inFlowSet;
    }

    public void setInFlowSet(FlowSet<DataFlowNode> inFlowSet) {
        this.inFlowSet = inFlowSet;
    }

    public String getUnitLine() {
        return unitLine;
    }

    public void setUnitLine(String unitLine) {
        this.unitLine = unitLine;
    }

    @Override
    public String toString() {
        return "UnitToDataSet [unitLine=" + unitLine + ", inFlowSet=" + inFlowSet + "]";
    }

}
