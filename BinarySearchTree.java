import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
        this.left = this.right = null;
    }
}

public class BinarySearchTree {

    public static List<Integer> closestValues(TreeNode root, double target, int x) {
        List<Integer> result = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;

        // In-order traversal to create a sorted list of values
        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            result.add(current.val);
            current = current.right;
        }

        // Find x closest values to the target in the sorted list
        int leftIndex = 0, rightIndex = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) <= target) {
                leftIndex = i;
            } else {
                rightIndex = i;
                break;
            }
        }

        while (x > 0) {
            if (leftIndex >= 0 && rightIndex < result.size()) {
                double leftDiff = Math.abs(result.get(leftIndex) - target);
                double rightDiff = Math.abs(result.get(rightIndex) - target);

                if (leftDiff <= rightDiff) {
                    result.remove(rightIndex);
                } else {
                    result.remove(leftIndex);
                    leftIndex--;
                }
            } else if (leftIndex >= 0) {
                result.remove(leftIndex);
                leftIndex--;
            } else if (rightIndex < result.size()) {
                result.remove(rightIndex);
            }

            x--;
        }

        return result;
    }

    public static void main(String[] args) {
    
        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int x = 2;

        List<Integer> closestValues = closestValues(root, target, x);
        System.out.println("Closest values to " + target + ": " + closestValues);
    }
}
