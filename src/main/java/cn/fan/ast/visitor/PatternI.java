package cn.fan.ast.visitor;

/**
 * @ClassName: Pattern
 * @Description: 我们把ast树怎么处理 需要继承改方法
 * @author LiRongFan
 * @date 2018年7月31日 下午1:53:05
 *
 */
public interface PatternI {
    public boolean dealNode(String nodelabel, String nodeName);

    public boolean dealPrimaryEdge(String startNode, String endNode, String remark);

    public boolean dealCfgEdge(String startNode, String endNode, String remark);

    public boolean dealPdgEdge(String startNode, String endNode, String remark);
}
