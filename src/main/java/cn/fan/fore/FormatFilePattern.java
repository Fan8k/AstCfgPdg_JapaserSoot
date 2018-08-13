package cn.fan.fore;

import static com.github.javaparser.utils.Utils.assertNotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.fan.ast.visitor.PatternI;
import cn.fan.model.AstNode;
import cn.fan.model.Edge;

import com.github.javaparser.ast.Node;

public class FormatFilePattern {
    private int nodeCount;
    private final boolean outputNodeType;
    // 是否需要假如cfg的边
    private boolean addCfgLines;
    private HashMap<Integer, String> lineToDotLabel;
    // 所有cfg中需要的边 数据流和控制流的边
    private HashMap<String, Set<Edge>> allmethodToCfgEdges;
    private HashMap<String, Set<Edge>> allmethodToallDataFlowEdges;
    private PatternI patternI;
    private Pattern p;
    private Pattern dotP;

    /**
     * 
     * @param outputNodeType
     * @param lineToTypeName
     *            这个行和类型名字结合的map 就是用来生成行和label的 这样才能为dot增加边
     */
    public FormatFilePattern(boolean outputNodeType, boolean addCfgLines, HashMap<String, Set<Edge>> allmethodToCfgEdges, HashMap<String, Set<Edge>> allmethodToallDataFlowEdges,
            PatternI patternI) {
        this.outputNodeType = outputNodeType;
        this.lineToDotLabel = new HashMap<Integer, String>();
        this.addCfgLines = addCfgLines;
        this.allmethodToallDataFlowEdges = allmethodToallDataFlowEdges;
        this.allmethodToCfgEdges = allmethodToCfgEdges;
        this.patternI = patternI;
        p = Pattern.compile("\r|\n|\r\n");
        dotP = Pattern.compile(",");
    }

    public String output(AstNode node) {
        nodeCount = 0;
        output(node, null, "root", node.getRootPrimary());
        if (addCfgLines && (allmethodToallDataFlowEdges != null || allmethodToCfgEdges != null)) {
            addCfgAddDataFlowEdge();
        }
        return null;
    }

    private void output(AstNode node, String parentNodeName, String name, Node root) {
        assertNotNull(node);
        List<String> attributes = node.getAttributes();
        List<AstNode> subNodes = node.getSubNodes();
        List<String> subLists = node.getSubLists();
        List<List<AstNode>> subListNodes = node.getSubListNodes();
        List<String> subLists_name = node.getSubLists_name();
        List<Node> subNodesPrimary = node.getSubNodesPrimary();
        List<List<Node>> subListNodesPrimary = node.getSubListNodesPrimary();

        // 原来Ast的节点 因为需要用到各个节点的行号信息，所以必须一起遍历

        String ndName = nextNodeName();
        if (!root.toString().equals("")) {
            if (outputNodeType) {
                // 只有第一次才能
                if (lineToDotLabel.get(root.getBegin().get().line) == null) {
                    lineToDotLabel.put(root.getBegin().get().line, ndName);
                }
                Matcher matcher = p.matcher(node.getTypeName());
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                patternI.dealNode(ndName, matcher2.replaceAll("."));
            }
            else {
                if (lineToDotLabel.get(root.getBegin().get().line) == null) {
                    lineToDotLabel.put(root.getBegin().get().line, ndName);
                }
                Matcher matcher = p.matcher(node.getTypeName());
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                patternI.dealNode(ndName, matcher2.replaceAll("."));
            }

            if (parentNodeName != null)
                patternI.dealPrimaryEdge(parentNodeName, ndName, "[primary]");

            for (String a : attributes) {
                String attrName = nextNodeName();
                Matcher matcher = p.matcher(a);
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                patternI.dealNode(attrName, matcher2.replaceAll("."));
                patternI.dealPrimaryEdge(ndName, attrName, "[primary]");
            }

            for (int i = 0; i < subNodes.size(); i++) {
                output(subNodes.get(i), ndName, subNodes.get(i).getName(), subNodesPrimary.get(i));
            }

            for (int i = 0; i < subLists.size(); i++) {
                String ndLstName = nextNodeName();
                Matcher matcher = p.matcher(subLists.get(i));
                Matcher matcher2 = dotP.matcher(matcher.replaceAll(""));
                patternI.dealNode(ndLstName, matcher2.replaceAll("."));
                patternI.dealPrimaryEdge(ndName, ndLstName, "[primary]");
                for (int j = 0; j < subListNodes.get(i).size(); j++) {
                    output(subListNodes.get(i).get(j), ndLstName, subLists_name.get(i), subListNodesPrimary.get(i).get(j));
                }
            }
        }
    }

    private void addCfgAddDataFlowEdge() {
        // 假如cfg lines
        Collection<Set<Edge>> cfgEdges = allmethodToCfgEdges.values();
        // 一个方法所有的边
        for (Set<Edge> methodEdges : cfgEdges) {
            for (Edge edge : methodEdges) {
                if (lineToDotLabel.get(Integer.parseInt(edge.getHead())) != null && lineToDotLabel.get(Integer.parseInt(edge.getTail())) != null) {
                    if (!lineToDotLabel.get(Integer.parseInt(edge.getHead())).equals("null") && !lineToDotLabel.get(Integer.parseInt(edge.getTail())).equals("null")) {
                        patternI.dealCfgEdge(lineToDotLabel.get(Integer.parseInt(edge.getHead())), lineToDotLabel.get(Integer.parseInt(edge.getTail())), "[cfg]");
                    }
                }
            }
        }

        Collection<Set<Edge>> dataFlowEdges = allmethodToallDataFlowEdges.values();
        // 一个方法所有的边
        for (Set<Edge> dataflowEdges : dataFlowEdges) {
            for (Edge edge : dataflowEdges) {
                if (lineToDotLabel.get(Integer.parseInt(edge.getHead())) != null && lineToDotLabel.get(Integer.parseInt(edge.getTail())) != null) {
                    if (!lineToDotLabel.get(Integer.parseInt(edge.getHead())).equals("null") && !lineToDotLabel.get(Integer.parseInt(edge.getTail())).equals("null")) {
                        patternI.dealPdgEdge(lineToDotLabel.get(Integer.parseInt(edge.getHead())), lineToDotLabel.get(Integer.parseInt(edge.getTail())), "[pdg]");
                    }
                }
            }
        }
    }

    private String nextNodeName() {
        return (nodeCount++) + "";
    }
}
