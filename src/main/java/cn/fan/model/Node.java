package cn.fan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 一种抽象的结构
 * @author fan
 *
 */
public class Node {
	private String value;
	/**前置节点集合*/
	private List<Node> preds;
	/**后置节点集合*/
	private List<Node> succs;

	//构造函数公共模块抽取出来
	{
		preds = new ArrayList<Node>();
		succs = new ArrayList<Node>();
	}

	public Node() {
		this.value = "";
	}

	public Node(String value) {
		this.value = value;
	}

	/**加入一个前置节点 这个设计的目的就是可以连续 add.add*/
	public List<Node> addPred(Node node) {
		preds.add(node);
		return preds;
	}

	/**加入一个后置节点 这个设计的目的就是可以连续 add.add*/
	public List<Node> addSucc(Node node) {
		succs.add(node);
		return succs;
	}

	/**删除一个前置节点 这个设计的目的就是可以连续 add.add*/
	public List<Node> removePred(Node node) {
		preds.remove(node);
		return preds;
	}

	/**删除一个后置节点 这个设计的目的就是可以连续 add.add*/
	public List<Node> removeSucc(Node node) {
		succs.remove(node);
		return succs;
	}

	public String getValue() {
		return value;
	}

	public List<Node> getPreds() {
		return preds;
	}

	public List<Node> getSuccs() {
		return succs;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
