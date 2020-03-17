/*
 * Problem: https://www.hackerrank.com/challenges/tree-postorder-traversal/problem
 * Difficulty: Easy
 * Themes: Tree Traversal
 */


package trees;

import org.junit.Assert;
import org.junit.Test;

public class TreePostOrderTraversal {

    @Test
    public void test(){
        Node root = new Node(5);
        insert(root, 1);
        insert(root, 3);
        insert(root, 9);
        insert(root, 15);
        insert(root, 13);
        Assert.assertEquals("3 1 13 15 9 5", postOrder(root));
    }

    public static String postOrder(Node root) {
        StringBuilder traversal = new StringBuilder();
        recursiveTraversal(root, traversal);
        return traversal.toString();
    }

    public static void recursiveTraversal(Node root, StringBuilder traversal) {
        if(root.left != null){
            recursiveTraversal(root.left, traversal);
        }
        if(root.right != null){
            recursiveTraversal(root.right, traversal);
        }
        if(traversal.length() > 0 ){
            traversal.append(' ');
        }
        traversal.append(root.data);
    }

    public static Node insert(Node root, int data) {
        if(root == null) {
            return new Node(data);
        } else {
            Node cur;
            if(data <= root.data) {
                cur = insert(root.left, data);
                root.left = cur;
            } else {
                cur = insert(root.right, data);
                root.right = cur;
            }
            return root;
        }
    }

}

class Node {
    Node left;
    Node right;
    int data;

    Node(int data) {
        this.data = data;
        left = null;
        right = null;
    }
}
