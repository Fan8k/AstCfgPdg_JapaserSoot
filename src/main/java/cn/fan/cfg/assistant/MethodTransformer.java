package cn.fan.cfg.assistant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.BriefUnitGraph;
import soot.util.cfgcmd.CFGGraphType;
import cn.fan.model.CfgNode;

/**
 * 对于class中方法 怎么处理
 * @author fan
 *
 */
public class MethodTransformer extends BodyTransformer {

	private String methodName;
	/**
	 * 一行所对应的所有jimple statement
	 */
	private HashMap<Integer, List<Unit>> allUnitsOfLine;
	private HashMap<Integer, CfgNode<String>> allEdges;
	/**
	 * 存储 {unit:行号}
	 */
	private HashMap<Unit, Integer> unitToLineMap;

	public MethodTransformer(String methodName) {
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
		this.allUnitsOfLine = new HashMap<Integer, List<Unit>>(16);
		unitToLineMap = new HashMap<Unit, Integer>(16);
		allEdges = new HashMap<Integer, CfgNode<String>>(16);
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		// TODO Auto-generated method stub
		//只转换目标方法
		if (b.getMethod().getName().equals(methodName)) {
			//System.out.println(b);
			PatchingChain<Unit> units = b.getUnits();
			for (Unit unit : units) {
				LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");
				if (tag != null) {
					int lineNumber = tag.getLineNumber();
					unitToLineMap.put(unit, lineNumber);
					if (allUnitsOfLine.get(lineNumber) == null || allUnitsOfLine.get(lineNumber).size() == 0) {
						List<Unit> listUnit = new ArrayList<Unit>();
						listUnit.add(unit);
						allUnitsOfLine.put(lineNumber, listUnit);
					} else {
						allUnitsOfLine.get(lineNumber).add(unit);
					}
				}
			}
			extractEdgeFromJimple(b);
		}
	}

	/**
	 * 从body形成的cfg中抽取边
	 * @param body
	 */
	private void extractEdgeFromJimple(Body body) {
		//BriefUnitGraph
		CFGGraphType cfgType = CFGGraphType.BRIEF_UNIT_GRAPH;
		BriefUnitGraph directGraph = (BriefUnitGraph) cfgType.buildGraph(body);
		//把相同行的前驱和后继节点合并一起
		for (Entry<Integer, List<Unit>> entry : allUnitsOfLine.entrySet()) {
			//获取某一行号所有的list
			List<Unit> values = entry.getValue();
			List<CfgNode<Unit>> Nodes = new ArrayList<CfgNode<Unit>>();
			for (Unit unit : values) {
				//一个unit对应的所有前驱和后继节点
				//先清洗一遍前驱和后继  保证前驱和后继都是来自外来行
				List<Unit> predsOf = directGraph.getPredsOf(unit);
				predsOf.removeAll(entry.getValue());
				List<Unit> succsOf = directGraph.getSuccsOf(unit);
				succsOf.remove(entry.getValue());
				Nodes.add(new CfgNode<Unit>(entry.getKey() + "", predsOf, succsOf));
			}
			//前驱节点的所有行号
			List<String> predsLines = new ArrayList<String>();
			List<String> succsLines = new ArrayList<String>();

			//从unit到行号的转变
			for (CfgNode<Unit> node : Nodes) {

				//所有前驱
				for (Unit unit : node.getPreds()) {
					predsLines.add(String.valueOf(unitToLineMap.get(unit)));
				}
				//所有后继
				for (Unit unit : node.getSuccs()) {
					succsLines.add(String.valueOf(unitToLineMap.get(unit)));
				}
			}
			// {10:{11,12,13},{9,8,15}} 一行所对应的所有前驱和后继
			allEdges.put(entry.getKey(), new CfgNode<String>(entry.getKey() + "", predsLines, succsLines));
		}
	}

	/**
	 * 获取soot中所有的边
	 * @return
	 */
	public HashMap<Integer, CfgNode<String>> getAllEdges() {
		return allEdges;
	}

}
