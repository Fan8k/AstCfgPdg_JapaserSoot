package cn.fan.fore;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fan.model.Edge;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;

/**
 * 
 * @ClassName: DotMethodAst
 * @Description: 只是负责把ast中各个method的进行节点输出，加上改方法的控制流和数据流
 * @author LiRongFan
 * @date 2018年8月6日 下午2:55:35
 *
 */
public class DotMethodAst {
    private int nodeCount;
    private final boolean outputNodeType;
    // 是否需要假如cfg的边
    private boolean addCfgLines;
    private HashMap<Integer, String> lineToDotLabel;

    // 所有cfg中需要的边 数据流和控制流的边
    private HashMap<String, Set<Edge>> allmethodToCfgEdges;
    private HashMap<String, Set<Edge>> allmethodToallDataFlowEdges;
    private Pattern p;
    private Pattern dotP;
    private Pattern quotationP;
    private int startLine;
    private int endLine;

    public DotMethodAst(boolean outputNodeType, boolean addCfgLines, HashMap<String, Set<Edge>> allmethodToCfgEdges, HashMap<String, Set<Edge>> allmethodToallDataFlowEdges) {
        this.outputNodeType = outputNodeType;
        this.addCfgLines = addCfgLines;
        this.allmethodToallDataFlowEdges = allmethodToallDataFlowEdges;
        this.allmethodToCfgEdges = allmethodToCfgEdges;
        p = Pattern.compile("\r|\n|\r\n");
        dotP = Pattern.compile(",");
        quotationP = Pattern.compile("\"");
    }

    public String output(Node node) {
        nodeCount = 0;
        StringBuilder output = new StringBuilder();
        this.lineToDotLabel = new HashMap<Integer, String>();
        output.append("digraph {");
        NodeMetaModel metaModel = node.getMetaModel();
        startLine = node.getBegin().get().line;
        endLine = node.getEnd().get().line;
        output.append(System.lineSeparator() + "label=\"" + startLine + "----" + endLine + ",total" + (endLine - startLine) + "\"");
        output(node, null, "root", output);
        if (addCfgLines && (allmethodToallDataFlowEdges != null || allmethodToCfgEdges != null)) {
            addCfgAddDataFlowEdge(output);
        }
        output.append(System.lineSeparator() + "}");
        return output.toString();
    }

    public void output(Node node, String parentNodeName, String name, StringBuilder builder) {
        assertNotNull(node);
        NodeMetaModel metaModel = node.getMetaModel();
        List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
        List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute).filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular).collect(toList());
        List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(toList());

        String ndName = nextNodeName();
        if (!node.toString().equals("")) {
            if (outputNodeType) {
                // 只有第一次才能
                if (lineToDotLabel.get(node.getBegin().get().line) == null) {
                    lineToDotLabel.put(node.getBegin().get().line, ndName);
                }
                Matcher matcher = p.matcher(name + " (" + metaModel.getTypeName() + ")");
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                Matcher matcher3 = quotationP.matcher(matcher2.replaceAll("."));
                builder.append(System.lineSeparator() + ndName + " [label=\"" + matcher3.replaceAll("'") + ")\"];");
            }
            else {
                if (lineToDotLabel.get(node.getBegin().get().line) == null) {
                    lineToDotLabel.put(node.getBegin().get().line, ndName);
                }
                Matcher matcher = p.matcher(name);
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                Matcher matcher3 = quotationP.matcher(matcher2.replaceAll("."));
                builder.append(System.lineSeparator() + ndName + " [label=\"" + matcher3.replaceAll("'") + "\"];");
            }

            if (parentNodeName != null)
                builder.append(System.lineSeparator() + parentNodeName + " -> " + ndName + ";");

            for (PropertyMetaModel a : attributes) {
                String attrName = nextNodeName();
                Matcher matcher = p.matcher(a.getName() + "='" + a.getValue(node).toString() + "'");
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                Matcher matcher3 = quotationP.matcher(matcher2.replaceAll("."));
                builder.append(System.lineSeparator() + attrName + " [label=\"" + matcher3.replaceAll("'") + "'\"];");
                builder.append(System.lineSeparator() + ndName + " -> " + attrName + ";");

            }
            for (PropertyMetaModel sn : subNodes) {
                Node nd = (Node) sn.getValue(node);
                if (nd != null)
                    output(nd, ndName, sn.getName(), builder);
            }

            for (PropertyMetaModel sl : subLists) {
                NodeList<? extends Node> nl = (NodeList<? extends Node>) sl.getValue(node);
                if (nl != null && nl.isNonEmpty()) {
                    String ndLstName = nextNodeName();
                    Matcher matcher = p.matcher(sl.getName());
                    Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                    Matcher matcher3 = quotationP.matcher(matcher2.replaceAll("."));
                    builder.append(System.lineSeparator() + ndLstName + " [label=\"" + matcher3.replaceAll("'") + "\"];");
                    builder.append(System.lineSeparator() + ndName + " -> " + ndLstName + ";");
                    String slName = sl.getName().substring(0, sl.getName().length() - 1);
                    for (Node nd : nl)
                        output(nd, ndLstName, slName, builder);
                }
            }
        }
    }

    private void addCfgAddDataFlowEdge(StringBuilder outputstrBuilder) {
        // 假如cfg lines
        Collection<Set<Edge>> cfgEdges = allmethodToCfgEdges.values();
        // 一个方法所有的边
        for (Set<Edge> methodEdges : cfgEdges) {
            for (Edge edge : methodEdges) {
                if ((Integer.parseInt(edge.getHead()) >= startLine && Integer.parseInt(edge.getHead()) <= endLine)
                        && (Integer.parseInt(edge.getTail()) >= startLine && Integer.parseInt(edge.getTail()) <= endLine)) {
                    if (lineToDotLabel.get(Integer.parseInt(edge.getHead())) != null && lineToDotLabel.get(Integer.parseInt(edge.getTail())) != null) {
                        if (!lineToDotLabel.get(Integer.parseInt(edge.getHead())).equals("null") && !lineToDotLabel.get(Integer.parseInt(edge.getTail())).equals("null")) {
                            outputstrBuilder.append(System.lineSeparator() + lineToDotLabel.get(Integer.parseInt(edge.getHead())) + " -> "
                                    + lineToDotLabel.get(Integer.parseInt(edge.getTail())) + " [color=\"red\"];");
                        }
                    }
                }
            }
        }

        Collection<Set<Edge>> dataFlowEdges = allmethodToallDataFlowEdges.values();
        // 一个方法所有的边
        for (Set<Edge> dataflowEdges : dataFlowEdges) {
            for (Edge edge : dataflowEdges) {
                // 只要边的头实体和尾实体在method中才行
                if ((Integer.parseInt(edge.getHead()) >= startLine && Integer.parseInt(edge.getHead()) <= endLine)
                        && (Integer.parseInt(edge.getTail()) >= startLine && Integer.parseInt(edge.getTail()) <= endLine)) {
                    if (lineToDotLabel.get(Integer.parseInt(edge.getHead())) != null && lineToDotLabel.get(Integer.parseInt(edge.getTail())) != null) {
                        if (!lineToDotLabel.get(Integer.parseInt(edge.getHead())).equals("null") && !lineToDotLabel.get(Integer.parseInt(edge.getTail())).equals("null")) {
                            outputstrBuilder.append(System.lineSeparator() + lineToDotLabel.get(Integer.parseInt(edge.getHead())) + " -> "
                                    + lineToDotLabel.get(Integer.parseInt(edge.getTail())) + " [color=\"green\"];");
                        }
                    }
                }
            }
        }
    }

    private String nextNodeName() {
        return "n" + (nodeCount++);
    }
}
