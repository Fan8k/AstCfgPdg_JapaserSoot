package cn.fan.model;

import java.io.Serializable;

import soot.EquivTo;

/**
 * 
 * @ClassName: DataFlowNode
 * @Description: 数据流分析中，专门用来存放每个unit中的入口变量集合和出口变量集合
 * @author LiRongFan
 * @date 2018年7月18日 下午9:09:13
 *
 */
public class DataFlowNode implements Serializable, EquivTo {
    private static final long serialVersionUID = 1L;
    private String variableName;
    private String lineNum;

    public DataFlowNode() {
        // 表示该变量没有对应的行号
        this.lineNum = "NAN";
    }

    public DataFlowNode(String variableName, String lineNum) {
        super();
        this.variableName = variableName;
        this.lineNum = lineNum;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getLineNum() {
        return lineNum;
    }

    public void setLineNum(String lineNum) {
        this.lineNum = lineNum;
    }

    @Override
    public String toString() {
        return "DataFlowNode [variableName=" + variableName + ", lineNum=" + lineNum + "]";
    }

    @Override
    public boolean equivTo(Object obj) {
        // TODO Auto-generated method stub
        boolean flag = false;
        if (obj instanceof DataFlowNode) {
            DataFlowNode dataFlowNode = (DataFlowNode) obj;
            if (dataFlowNode.getVariableName().equals(variableName) && dataFlowNode.getLineNum().equals(lineNum)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return equivHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return equivTo(obj);
    }

    @Override
    public int equivHashCode() {
        // TODO Auto-generated method stub
        return variableName.hashCode() + lineNum.hashCode();
    }

}
