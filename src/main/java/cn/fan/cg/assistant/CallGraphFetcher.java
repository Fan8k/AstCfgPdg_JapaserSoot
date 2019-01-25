package cn.fan.cg.assistant;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;

public class CallGraphFetcher extends SceneTransformer {

    private String className;

    public CallGraphFetcher(String className) {
        // TODO Auto-generated constructor stub
        this.className = className;
    }

    @Override
    protected void internalTransform(String phaseName, Map<String, String> options) {
        // TODO Auto-generated method stub
        CHATransformer.v().transform();
        SootClass c = Scene.v().getSootClass(this.className);
        CallGraph cg = Scene.v().getCallGraph();
        // 一个class 多个方法，一个方法到底调用了哪些方法
        List<SootMethod> methods = c.getMethods();
        for (SootMethod method : methods) {
            // 主动方法
            SootMethod src = method;
            Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(src));
            while (targets.hasNext()) {
                SootMethod target = (SootMethod) targets.next();
                System.out.println(src + "might call" + target);
            }
        }
    }
}
