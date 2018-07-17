package cn.javaparser.test;

import static com.github.javaparser.utils.Utils.assertNotNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.metamodel.NodeMetaModel;
import com.github.javaparser.metamodel.PropertyMetaModel;

public class DotPrintAstTest {
	private int nodeCount;
	private final boolean outputNodeType;

	public DotPrintAstTest(boolean outputNodeType) {
		this.outputNodeType = outputNodeType;
	}

	public String output(Node node) {
		nodeCount = 0;
		StringBuilder output = new StringBuilder();
		output.append("digraph {");
		output(node, null, "root", output);
		output.append(System.lineSeparator() + "}");
		return output.toString();
	}

	public void output(Node node, String parentNodeName, String name, StringBuilder builder) {
		assertNotNull(node);
		NodeMetaModel metaModel = node.getMetaModel();

		System.out.println(node.getBegin() + "\t" + metaModel.getTypeName());
		List<PropertyMetaModel> allPropertyMetaModels = metaModel.getAllPropertyMetaModels();
		List<PropertyMetaModel> attributes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isAttribute)
							.filter(PropertyMetaModel::isSingular).collect(toList());
		List<PropertyMetaModel> subNodes = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNode).filter(PropertyMetaModel::isSingular)
							.collect(toList());
		List<PropertyMetaModel> subLists = allPropertyMetaModels.stream().filter(PropertyMetaModel::isNodeList).collect(toList());

		String ndName = nextNodeName();
		if (outputNodeType)
			builder.append(System.lineSeparator() + ndName + " [label=\"" + name + " (" + metaModel.getTypeName() + ")\"];");
		else
			builder.append(System.lineSeparator() + ndName + " [label=\"" + name + "\"];");

		if (parentNodeName != null)
			builder.append(System.lineSeparator() + parentNodeName + " -> " + ndName + ";");

		for (PropertyMetaModel a : attributes) {
			String attrName = nextNodeName();
			builder.append(System.lineSeparator() + attrName + " [label=\"" + a.getName() + "='" + a.getValue(node).toString() + "'\"];");
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
				builder.append(System.lineSeparator() + ndLstName + " [label=\"" + sl.getName() + "\"];");
				builder.append(System.lineSeparator() + ndName + " -> " + ndLstName + ";");
				String slName = sl.getName().substring(0, sl.getName().length() - 1);
				for (Node nd : nl)
					output(nd, ndLstName, slName, builder);
			}
		}
	}

	private String nextNodeName() {
		return "n" + (nodeCount++);
	}
}
