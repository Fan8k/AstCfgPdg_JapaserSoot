package SourceCode;

public class RBTree<T extends Comparable<T>> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private RBTNode<T> root;

    // 红黑树节点
    private class RBTNode<T extends Comparable<T>> {
        boolean color = RED;
        T key; // 存储的值
        RBTNode<T> left;
        RBTNode<T> right;
        RBTNode<T> parent;

        public RBTNode(T key, RBTNode<T> left, RBTNode<T> right, RBTNode<T> parent) {
            this.key = key;
            this.left = left;
            this.right = right;
            this.parent = parent;
        }

        public boolean isColor() {
            return color;
        }

        public T getKey() {
            return key;
        }

        public RBTNode<T> getLeft() {
            return left;
        }

        public RBTNode<T> getRight() {
            return right;
        }

        public RBTNode<T> getParent() {
            return parent;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public void setKey(T key) {
            this.key = key;
        }

        public void setLeft(RBTNode<T> left) {
            this.left = left;
        }

        public void setRight(RBTNode<T> right) {
            this.right = right;
        }

        public void setParent(RBTNode<T> parent) {
            this.parent = parent;
        }

    }

    private RBTNode<T> parentOf(RBTNode<T> node) {
        return node != null ? node.getParent() : null;
    }

    private boolean colorOf(RBTNode<T> node) {
        return node != null ? node.isColor() : BLACK;
    }

    private boolean isRed(RBTNode<T> node) {
        return ((node != null) && (node.color == RED)) ? true : false;
    }

    // 左旋 以需要旋转的节点为输入
    /**
     * 左旋 是将旋转节点的右孩子的左孩子作为该节点的右孩子，而右孩子的左孩子就是旋转节点的左节点，旋转节点的父节点变成右孩子的父节点
     * 
     * @param x
     */
    private void leftRotate(RBTNode<T> x) {
        RBTNode<T> y = x.right;

        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        }
        else {
            if (x.parent.left == x) {
                x.parent.left = y;
            }
            else {
                x.parent.right = y;
            }
        }
        y.left = x;
        x.parent = y;
    }

    // 右旋
    /**
     * 右旋： 旋转节点X的左孩子的右节点变成旋转节点的左孩子！旋转节点的父节点就是自己的左孩子节点！
     */
    private void rightRotate(RBTNode<T> x) {
        RBTNode<T> y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            this.root = y;
        }
        else {
            if (x.parent.left == x) {
                x.parent.left = y;
            }
            else {
                x.parent.right = y;
            }
        }
        y.right = x;
        x.parent = y;
    }

    /**
     * 将节点插入到红黑树中
     */
    private void insert(RBTNode<T> node) {
        int cmp;
        RBTNode<T> y = null;
        RBTNode<T> x = this.root;
        while (x != null) { // 利用循坏代替递归实现插入 和二叉排序树一样的插入
            y = x;
            cmp = node.key.compareTo(x.key);
            if (cmp < 0) {
                x = x.left;
            }
            else {
                x = x.right;
            }
        }
        node.parent = y;
        if (y != null) {
            cmp = node.key.compareTo(y.key);
            if (cmp < 0) {
                y.left = node;
            }
            else {
                y.right = node;
            }

        }
        else {
            this.root = node;
        }
    }

    public void insert(T key) {
        RBTNode<T> node = new RBTNode(key, null, null, null);
        insert(node);
    }

    /**
     * 删除节点 child 其实就是替代节点，不管删除节点有几个孩子 两个孩子删除的不在是node，而是replace，所以是replace的child
     * 一个孩子或者没有孩子，则child就是替代节点
     */
    private void remove(RBTNode<T> node) {
        RBTNode<T> child, parent;
        boolean color;
        // 删除节点左右孩子都不为空
        if ((node.left != null) && (node.right != null)) {
            RBTNode<T> replace = node;
            // 获取后继节点 也就是节点右边的最小节点
            replace = node.right;
            while (replace.left != null) {
                replace = replace.left;
            }
            // 交换node和replace的值
            node.key = replace.key;
            // 删除replace点
            child = replace.right;
            parent = replace.getParent();
            color = replace.isColor();
            // 替代点的父节点就是要删除的节点 说明他有可能有left
            if (parent == node) {
                parent.right = child;
            }
            else {
                parent.left = child;
            }
            // 删除了replace节点 如果为黑色需要调整树
            if (color == BLACK) {
                // 调整树
            }
            node = null;
            return;
        }
        // 说明没有右孩子
        if (node.left != null) {
            child = node.left;
        }
        else {
            // node 没有左孩子或者没有孩子
            child = node.right;
        }
        parent = node.getParent();
        color = node.isColor();
        if (child != null) {
            child.parent = parent;
        }
        if (parent != null) {
            if (parent.left == node) {
                parent.left = child;
            }
            else {
                parent.right = child;
            }
        }
        else {
            // 情况1 就是删除的是根节点
            this.root = child;
        }
        if (color == BLACK) {

        }
    }

    /**
     * 只需要管删除节点的替代节点
     */
    private void removeFixUp(RBTNode<T> node, RBTNode<T> parent) {
        RBTNode<T> brother;
        // node == null || node.color == BLACK 表示节点是黑色 因为node=null 就是叶子节点
        while ((node == null || node.color == BLACK) && (node != this.root)) {
            // 删除节点在左边的情况
            if (parent.left == node) {
                brother = parent.right;
                // 第一种情况兄弟节点为红色 因为brbrother 其他bl br p pl 都是黑色
                if (isRed(brother)) {
                    brother.setColor(BLACK);
                    parent.setColor(RED);
                    leftRotate(parent);
                    brother = parent.right;
                }
                if ((brother.left == null || brother.left.color == BLACK) && (brother.right == null || brother.right.color == BLACK)) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    brother.setColor(RED);
                    node = parent;
                    parent = parentOf(node);
                }
                else {
                    if (brother.right == null || brother.right.color == BLACK) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        brother.left.color = BLACK;
                        brother.color = RED;
                        rightRotate(brother);
                        brother = parent.right;
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    brother.color = parent.color;
                    parent.color = BLACK;
                    brother.right.color = BLACK;
                    leftRotate(parent);
                    node = this.root;
                    break;
                }
            }
            else {
                brother = parent.left;
                // 第一种情况兄弟节点为红色 因为brbrother 其他bl br p pl 都是黑色
                if (isRed(brother)) {
                    brother.setColor(BLACK);
                    parent.setColor(RED);
                    rightRotate(parent);
                    brother = parent.left;
                }
                if ((brother.left == null || brother.left.color == BLACK) && (brother.right == null || brother.right.color == BLACK)) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    brother.setColor(RED);
                    node = parent;
                    parent = parentOf(node);
                }
                else {
                    if (brother.left == null || brother.left.color == BLACK) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        brother.right.color = BLACK;
                        brother.color = RED;
                        leftRotate(brother);
                        brother = parent.left;
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
                    brother.color = parent.color;
                    parent.color = BLACK;
                    brother.left.color = BLACK;
                    rightRotate(parent);
                    node = this.root;
                    break;
                }
            }
        }
        // 如果孩子是红色的只需要改黑即可 黑色个体数不变！
        if (node != null) {
            node.setColor(BLACK);
        }
    }

    /**
     * 红黑树插入节点之后修正函数
     */
    private void insertFixUP(RBTNode<T> node) {
        RBTNode<T> parent = node.getParent();
        RBTNode<T> gparent;
        // 插入的时候只会触碰到情况4 如果父类为黑则不用调整
        while ((parent != null) && isRed(parent)) {
            gparent = parentOf(parent);
            // 先考虑左边的情况 和父节点是红色
            if (gparent.left == parent) {
                // case 1 叔叔节点是红色
                RBTNode<T> uncle = gparent.right;
                if (uncle != null && isRed(uncle)) {
                    uncle.setColor(BLACK);
                    parent.setColor(BLACK);
                    gparent.setColor(RED);
                    node = gparent;
                    parent = parentOf(node); // 递归向上
                    continue; // 这次循环下面的不用执行
                }
                // case2 叔叔是黑色 且红色节点是右孩子
                if (parent.right == node) {
                    RBTNode<T> tmp;
                    leftRotate(parent); // 左旋 变成情况三了
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // case3 条件 uncle 为黑色 且当前节点为左孩子 或者uncle为空也行
                parent.setColor(BLACK);
                gparent.setColor(RED);
                rightRotate(gparent);
            }
            else {
                // case 1 叔叔节点是红色
                RBTNode<T> uncle = gparent.left;
                if (uncle != null && isRed(uncle)) {
                    uncle.setColor(BLACK);
                    parent.setColor(BLACK);
                    gparent.setColor(RED);
                    node = gparent;
                    parent = parentOf(node); // 递归向上
                    continue; // 这次循环下面的不用执行
                }
                // case2 叔叔是黑色 且红色节点是右孩子
                if (parent.left == node) {
                    RBTNode<T> tmp;
                    rightRotate(parent); // 左旋 变成情况三了
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }
                // case3 条件 uncle 为黑色 且当前节点为左孩子 或者uncle为空也行
                parent.setColor(BLACK);
                gparent.setColor(RED);
                leftRotate(gparent);
            }
        }
        root.setColor(BLACK);
    }

}
