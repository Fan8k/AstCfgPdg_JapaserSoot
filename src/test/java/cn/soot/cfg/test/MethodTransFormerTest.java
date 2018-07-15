package cn.soot.cfg.test;

import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.PatchingChain;
import soot.Unit;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.DirectedGraph;
import soot.util.cfgcmd.CFGGraphType;

public class MethodTransFormerTest extends BodyTransformer {

	private String methodName;

	public MethodTransFormerTest(String methodName) {
		// TODO Auto-generated constructor stub
		this.methodName = methodName;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
		// TODO Auto-generated method stub
		//只转换目标方法
		if (b.getMethod().getName().equals(methodName)) {
			//System.out.println(b);

			PatchingChain<Unit> units = b.getUnits();
			for (Unit unit : units) {
				System.out.print(unit.toString() + "\t");
				LineNumberTag tag = (LineNumberTag) unit.getTag("LineNumberTag");
				if (tag != null) {
					int lineNumber = tag.getLineNumber();
					System.out.print("linenumber is " + lineNumber);
				}
				System.out.println();
			}
			CFGGraphType cfgType = CFGGraphType.BRIEF_UNIT_GRAPH;
			DirectedGraph buildGraph = cfgType.buildGraph(b);
			System.out.println(buildGraph);
		}
	}
}