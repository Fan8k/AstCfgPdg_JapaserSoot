package cn.fan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 一种抽象的结构
 * @author fan
 *
 */
public class CfgNode<T> {
	private String value;
	/**前置节点集合*/
	private List<T> preds;
	/**后置节点集合*/
	private List<T> succs;

	public CfgNode() {
		this.value = "";
		this.preds = new ArrayList<T>();
		this.succs = new ArrayList<T>();
	}

	public CfgNode(String value, List<T> preds, List<T> succs) {
		this.value = value;
		this.preds = preds;
		this.succs = succs;
	}

	/**加入一个前置节点 这个设计的目的就是可以连续 add.add*/
	public List<T> addPred(T node) {
		preds.add(node);
		return preds;
	}

	/**加入一个后置节点 这个设计的目的就是可以连续 add.add*/
	public List<T> addSucc(T node) {
		succs.add(node);
		return succs;
	}

	/**删除一个前置节点 这个设计的目的就是可以连续 add.add*/
	public List<T> removePred(T node) {
		preds.remove(node);
		return preds;
	}

	/**删除一个后置节点 这个设计的目的就是可以连续 add.add*/
	public List<T> removeSucc(T node) {
		succs.remove(node);
		return succs;
	}

	public String getValue() {
		return value;
	}

	public List<T> getPreds() {
		return preds;
	}

	public List<T> getSuccs() {
		return succs;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
