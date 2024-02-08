import java.util.PriorityQueue;

class Node {
    int key;
    double diff;
    Node left, right;

    public Node(int key) {
        this.key = key;
        left = right = null;
    }
}

public class BinarySearchTree {

    static void closest(Node root, double k, int x) {
        if (root == null) {
            return ;
        }

        closest(root.right, k, x);
        if (closestValues.size() == x) {
            if (closestValues.peek().diff > Math.abs(k - root.key)) {
                closestValues.poll();
                closestValues.add(root);
            }
        }

        else {
            closestValues.add(root);
        }

        root.diff = Math.abs(k - root.key);

        closest(root.left, k, x);
    }

    static PriorityQueue<Node> closestValues = new PriorityQueue<>((a, b) -> {
        return (int) (a.diff - b.diff);
    });

    public static void main(String[] args) {
        Node root = new Node(4);
        root.left = new Node(2);
        root.right = new Node(5);
        root.left.left = new Node(1);
        root.left.right = new Node(3);

        double k = 3.8;
        int x = 2;

        closest(root, k, x);

        for (int i = 0; i < x; i++) {
            System.out.print(closestValues.poll().key + " ");
        }
    }
}