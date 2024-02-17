import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class TreeNode {
    int val;
    TreeNode left, right;

    public TreeNode(int val) {
        this.val = val;
        this.left = this.right = null;
    }
}

public class BinarySearchTree {
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> result = new ArrayList<>();
        PriorityQueue<Double> minHeap = new PriorityQueue<>((a, b) -> Double.compare(Math.abs(b - target), Math.abs(a - target)));

        inorderTraversal(root, target, k, minHeap);

        while (k-- > 0 && !minHeap.isEmpty()) {
            result.add(minHeap.poll().intValue());
        }

        return result;
    }

    private void inorderTraversal(TreeNode node, double target, int k, PriorityQueue<Double> minHeap) {
        if (node == null) {
            return;
        }

        inorderTraversal(node.left, target, k, minHeap);

        minHeap.offer((double) node.val);

        if (minHeap.size() > k) {
            minHeap.poll();
        }

        inorderTraversal(node.right, target, k, minHeap);
    }

    public static void main(String[] args) {
        BinarySearchTree solution = new BinarySearchTree();

        TreeNode root = new TreeNode(4);
        root.left = new TreeNode(2);
        root.right = new TreeNode(5);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(3);

        double target = 3.8;
        int k = 2;

        List<Integer> result = solution.closestKValues(root, target, k);

        System.out.println("Closest values to " + target + ": " + result);
    }
}
