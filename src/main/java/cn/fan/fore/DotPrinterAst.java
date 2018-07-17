package cn.fan.fore;

import static com.github.javaparser.utils.Utils.assertNotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import cn.fan.model.AstNode;
import cn.fan.model.CfgNode;
import cn.fan.model.Edge;

import com.github.javaparser.ast.Node;

public class DotPrinterAst {
	private int nodeCount;
	private final boolean outputNodeType;
	//是否需要假如cfg的边
	private boolean addCfgLines;
	private HashMap<Integer, String> lineToDotLabel;
	private HashMap<Integer, CfgNode<String>> allEdgesFromcfg;

	/**
	 * 
	 * @param outputNodeType
	 * @param lineToTypeName 这个行和类型名字结合的map 就是用来生成行和label的 这样才能为dot增加边
	 */
	public DotPrinterAst(boolean outputNodeType, boolean addCfgLines, HashMap<Integer, CfgNode<String>> allEdgesFromcfg) {
		this.outputNodeType = outputNodeType;
		this.lineToDotLabel = new HashMap<Integer, String>();
		this.addCfgLines = addCfgLines;
		this.allEdgesFromcfg = allEdgesFromcfg;
	}

	public String output(AstNode node) {
		nodeCount = 0;
		StringBuilder outputstrBuilder = new StringBuilder();
		outputstrBuilder.append("digraph {");
		output(node, null, "root", outputstrBuilder, node.getRootPrimary());
		//假如cfg lines
		if (addCfgLines && allEdgesFromcfg != null) {
			Set<Edge> washEdges = washEdges();
			for (Edge edge : washEdges) {
				outputstrBuilder.append(System.lineSeparator() + lineToDotLabel.get(Integer.parseInt(edge.getHead())) + " -> "
									+ lineToDotLabel.get(Integer.parseInt(edge.getTail())) + " [color=\"red\"];");
			}
		}
		outputstrBuilder.append(System.lineSeparator() + "}");
		return outputstrBuilder.toString();
	}

	private void output(AstNode node, String parentNodeName, String name, StringBuilder builder, Node root) {
		assertNotNull(node);
		List<String> attributes = node.getAttributes();
		List<AstNode> subNodes = node.getSubNodes();
		List<String> subLists = node.getSubLists();
		List<List<AstNode>> subListNodes = node.getSubListNodes();
		List<String> subLists_name = node.getSubLists_name();
		List<Node> subNodesPrimary = node.getSubNodesPrimary();
		List<List<Node>> subListNodesPrimary = node.getSubListNodesPrimary();

		//原来Ast的节点  因为需要用到各个节点的行号信息，所以必须一起遍历

		String ndName = nextNodeName();
		if (outputNodeType) {
			//只有第一次才能
			if (lineToDotLabel.get(root.getBegin().get().line) == null) {
				lineToDotLabel.put(root.getBegin().get().line, ndName);
			}
			builder.append(System.lineSeparator() + ndName + " [label=\"" + node.getTypeName() + ")\"];");
		} else {
			if (lineToDotLabel.get(root.getBegin().get().line) == null) {
				lineToDotLabel.put(root.getBegin().get().line, ndName);
			}
			builder.append(System.lineSeparator() + ndName + " [label=\"" + node.getTypeName() + "\"];");
		}

		if (parentNodeName != null)
			builder.append(System.lineSeparator() + parentNodeName + " -> " + ndName + ";");

		for (String a : attributes) {
			String attrName = nextNodeName();
			builder.append(System.lineSeparator() + attrName + " [label=\"" + a + "'\"];");
			builder.append(System.lineSeparator() + ndName + " -> " + attrName + ";");
		}

		for (int i = 0; i < subNodes.size(); i++) {
			output(subNodes.get(i), ndName, subNodes.get(i).getName(), builder, subNodesPrimary.get(i));
		}

		for (int i = 0; i < subLists.size(); i++) {
			String ndLstName = nextNodeName();
			builder.append(System.lineSeparator() + ndLstName + " [label=\"" + subLists.get(i) + "\"];");
			builder.append(System.lineSeparator() + ndName + " -> " + ndLstName + ";");
			for (int j = 0; j < subListNodes.get(i).size(); j++) {
				output(subListNodes.get(i).get(j), ndLstName, subLists_name.get(i), builder, subListNodesPrimary.get(i).get(j));
			}
		}
	}

	/**
	 * cfg 中拿到的边有可能前驱重复很多和后继和重复很多，所以清洗一下前驱和后继都只要一条
	 */
	private Set<Edge> washEdges() {
		Set<Edge> allEdges = new HashSet<Edge>();
		for (Entry<Integer, CfgNode<String>> entry : allEdgesFromcfg.entrySet()) {
			//所有前驱节点
			for (String preds : entry.getValue().getPreds()) {
				if (!preds.equals("null")) {
					allEdges.add(new Edge(preds, entry.getKey() + ""));
				}
			}
			//所有后继节点
			for (String succs : entry.getValue().getPreds()) {
				if (!succs.equals("null")) {
					allEdges.add(new Edge(entry.getKey() + "", succs));
				}
			}
		}
		return allEdges;
	}

	private String nextNodeName() {
		return "n" + (nodeCount++);
	}
}
