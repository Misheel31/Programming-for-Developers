import java.util.ArrayList;
import java.util.List;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

public class BinaryTree {

    private List<Integer> closestValues(TreeNode root, int k, int x) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(root, k, x, result);
        return result;
    }

    private void inorderTraversal(TreeNode node, int k, int x, List<Integer> result) {
        if (node != null) {
            inorderTraversal(node.left, k, x, result);
            if (result.size() < x) {
                result.add(node.val);
            } else {
                int diff = Math.abs(node.val - k);
                int lastDiff1 = Math.abs(result.get(result.size() - 1) - k);
                int lastDiff2 = Math.abs(result.get(result.size() - 2) - k);

                if (diff < lastDiff1 || (diff == lastDiff1 && node.val < result.get(result.size() - 1))) {
                    result.set(result.size() - 1, node.val);
                } else if (diff < lastDiff2 || (diff == lastDiff2 && node.val < result.get(result.size() - 2))) {
                    result.set(result.size() - 2, node.val);
                }
            }
            inorderTraversal(node.right, k, x, result);
        }
    }

    public static void main(String[] args) {

        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(9);
        root.right = new TreeNode(5);
        root.right.right = new TreeNode(8);
        root.right.right.left = new TreeNode(3);
        root.right.right.right = new TreeNode(10);

        BinaryTree binaryTree = new BinaryTree();
        List<Integer> result = binaryTree.closestValues(root, 60, 2);

        // Calculate and print the average of the two closest values
        double average = (result.get(0) + result.get(1)) / 2.0;
        System.out.println(average);  // Output: 4.5
    }
}
