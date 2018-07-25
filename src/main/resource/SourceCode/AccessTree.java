package SourceCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AccessTree {

    private static HashMap<Integer, String> results = new HashMap<Integer, String>();

    static {
        results.put(1, "one");
        results.put(2, "two");
        results.put(3, "three");
        results.put(4, "four");
    }

    public static void main(String[] args) {
        TreeNode rootNode = new TreeNode("根节点");
        getTree(rootNode, 1);
        postvisitor(rootNode);
        convertToBinarytree(rootNode);
        System.out.println(rootNode);
    }

    public static void getTree(TreeNode node, int n) {
        if (n == 3) {
            return;
        }
        else {
            List<TreeNode> oneList = new ArrayList<TreeNode>();
            for (int i = 1; i < 4; i++) {
                oneList.add(new TreeNode(results.get(n) + "_" + i));
            }
            node.setChildNodes(oneList);
            for (int i = 0; i < 3; i++) {
                getTree(oneList.get(i), n + 1);
            }
        }
    }

    // 先根访问 就是先序遍历二叉树一样
    public static void previsitor(TreeNode root) {
        if (root != null) {
            System.out.println(root.getContent());
            for (TreeNode child : root.getChildNodes()) {
                if (child == null) {
                    break;
                }
                else {
                    previsitor(child);
                }
            }
        }
    }

    // 先根访问 就是先序遍历二叉树一样
    public static void postvisitor(TreeNode root) {
        if (root != null) {
            for (TreeNode child : root.getChildNodes()) {
                postvisitor(child);
                System.out.println(child.getContent());
            }
        }
    }

    /**
     * 非二叉树转二叉树 1.从左到右连接兄弟节点，2.删除除了最左边的兄弟和父节点的连线
     * 
     * @param root
     */
    public static void convertToBinarytree(TreeNode root) {
        if (root != null) {
            List<TreeNode> childNodes = root.getChildNodes();// 获取所有的子节点
            if (childNodes.size() > 0) { // 说明不是最后一节点
                for (int i = childNodes.size() - 1; i > 1; i--) {
                    TreeNode post_Node = childNodes.get(i);
                    TreeNode pre_Node = childNodes.get(i - 1);
                    List<TreeNode> childNodes2 = pre_Node.getChildNodes();
                    childNodes2.add(post_Node);
                    pre_Node.setChildNodes(childNodes2);
                }
                List<TreeNode> newChilds = new ArrayList<TreeNode>();
                newChilds.add(childNodes.get(0));
                root.setChildNodes(newChilds);
                for (TreeNode node : childNodes) {
                    convertToBinarytree(node);
                }
            }
        }
    }
}
