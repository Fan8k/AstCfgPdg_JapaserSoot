package SourceCode;

public class BinarySearchTree<T extends Comparable<T>> {
    // 节点
    private static class Node<T> {
        private T data;
        private Node<T> left;
        private Node<T> right;

        public Node(T data) {
            this(data, null, null);
        }

        public Node(T data, Node<T> left, Node<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    // 私有变量 根root
    private Node<T> root;

    public BinarySearchTree() {
        root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(T value) {
        return contains(value, root);
    }

    private boolean contains(T Value, Node<T> t) {
        boolean flag = false;
        if (t == null) { // 空树
            return false;
        }
        int result = Value.compareTo(t.data);
        if (result == 0) {
            flag = true;
        }
        else if (result < 0) { // 小于该节点数据
            flag = contains(Value, t.left);
        }
        else if (result > 0) {
            flag = contains(Value, t.right);
        }
        return flag;
    }

    public T findMin() {
        return findMin(root).data;
    }

    private Node<T> findMin(Node<T> t) {
        if (t == null) {
            return null;
        }
        else if (t.left == null) {
            return t;
        }
        return findMin(t.left);
    }

    public T findMax() {
        return findMax(root).data;
    }

    private Node<T> findMax(Node<T> t) {
        if (t != null) {
            while (t.right != null) {
                t = t.right;
            }
        }
        return t;
    }

    public void insert(T value) {
        root = insert(value, root);
    }

    private Node<T> insert(T value, Node<T> t) {
        if (t == null) {
            return new Node(value, null, null); // 新建一个节点
        }
        int result = value.compareTo(t.data);
        if (result < 0) {
            t.left = insert(value, t.left);
        }
        else if (result > 0) {
            t.right = insert(value, t.right);
        }
        return t;
    }

    public void remove(T value) {
        root = remove(value, root);
    }

    private Node<T> remove(T value, Node<T> t) {
        if (t == null) {
            return t;
        }
        // 先看是否相等
        int result = value.compareTo(t.data);
        if (result < 0) {

        }
        else if (result > 0) {

        }
        else if (t.left != null && t.right != null) {
            t.data = findMin(t.right).data; // 替换值
            t.right = remove(t.data, t.right);
        }
        else {
            t = (t.left != null) ? t.left : t.right;
        }
        return t;
    }

    public void printTree() {

    }

    private void printTree(Node<T> root) {
        if (root != null) {
            printTree(root.left);
            System.out.println(root.data);
            printTree(root.right);
        }
    }

    public int height() {
        return 1;
    }

    private int height(Node<T> root) {
        return 0;
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
        bst.insert(5);
        bst.insert(7);
        bst.insert(3);
        bst.insert(1);
        bst.insert(9);
        bst.insert(6);
        bst.insert(4);
        System.out.println("最小值:" + bst.findMin());
        System.out.println("最大值:" + bst.findMax());
        System.out.println("查找元素9是否存在:" + bst.contains(9));
        System.out.println("查找元素8是否存在:" + bst.contains(8));
        System.out.println("遍历二叉树");
        bst.printTree();

    }
}
