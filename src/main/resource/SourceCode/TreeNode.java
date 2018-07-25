package SourceCode;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private String content;

    private List<TreeNode> childNodes;

    public TreeNode(String content) {
        this.content = content;
        this.childNodes = new ArrayList<TreeNode>();
    }

    public TreeNode(String content, List<TreeNode> childNodes) {
        this.content = content;
        this.childNodes = childNodes;
    }

    public String getContent() {
        return content;
    }

    public List<TreeNode> getChildNodes() {
        return childNodes;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChildNodes(List<TreeNode> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return "TreeNode [content=" + content + ", childNodes=" + childNodes + "]";
    }
}
