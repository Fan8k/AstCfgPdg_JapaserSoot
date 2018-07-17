package cn.fan.model;

import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ast.Node;

public class AstNode {
	private String name; //这个name 是用来生成dot 中的name用的
	private String typeName;
	private int lineBegin;
	private Node rootPrimary;

	/**
	 * 叶子节点  所以遍历的时候无需再继续递归了
	 */
	private List<String> attributes;

	/**
	 * 中间节点 不是一种集合类别
	 */
	private List<AstNode> subNodes;

	/**
	 * javaPaser 中自己的Node节点
	 */
	private List<Node> subNodesPrimary;
	/**
	 * 一种集合类别的节点 比如imports
	 */
	private List<String> subLists;

	/**
	 * 只是dot遍历时候需要用,用在集合类别节点那一快
	 */
	private List<String> subLists_name;

	/**
	 * 集合中所有的Nodes
	 */
	private List<List<AstNode>> subListNodes;
	/**
	 * 集合中所有的原来JavaPaser的Nodes
	 */
	private List<List<Node>> subListNodesPrimary;

	public AstNode() {
		this.attributes = new ArrayList<String>();
		this.subNodes = new ArrayList<AstNode>();
		this.subLists = new ArrayList<String>();
		this.subListNodes = new ArrayList<List<AstNode>>();
		this.subLists_name = new ArrayList<String>();
		this.subListNodesPrimary = new ArrayList<List<Node>>();
		this.subNodesPrimary = new ArrayList<Node>();
	}

	public String getTypeName() {
		return typeName;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<AstNode> getSubNodes() {
		return subNodes;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setSubNodes(List<AstNode> subNodes) {
		this.subNodes = subNodes;
	}

	public List<String> getSubLists() {
		return subLists;
	}

	public List<List<AstNode>> getSubListNodes() {
		return subListNodes;
	}

	public void setSubLists(List<String> subLists) {
		this.subLists = subLists;
	}

	public void setSubListNodes(List<List<AstNode>> subListNodes) {
		this.subListNodes = subListNodes;
	}

	public String getName() {
		return name;
	}

	public List<String> getSubLists_name() {
		return subLists_name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubLists_name(List<String> subLists_name) {
		this.subLists_name = subLists_name;
	}

	public int getLineBegin() {
		return lineBegin;
	}

	public void setLineBegin(int lineBegin) {
		this.lineBegin = lineBegin;
	}

	public List<Node> getSubNodesPrimary() {
		return subNodesPrimary;
	}

	public List<List<Node>> getSubListNodesPrimary() {
		return subListNodesPrimary;
	}

	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}

	public void setSubNodesPrimary(List<Node> subNodesPrimary) {
		this.subNodesPrimary = subNodesPrimary;
	}

	public void setSubListNodesPrimary(List<List<Node>> subListNodesPrimary) {
		this.subListNodesPrimary = subListNodesPrimary;
	}

	public Node getRootPrimary() {
		return rootPrimary;
	}

	public void setRootPrimary(Node rootPrimary) {
		this.rootPrimary = rootPrimary;
	}

}
