package SocialMedia;

import java.util.*;

class User {
    String username;
    Set<User> following;
    List<String> posts;

    public User(String username) {
        this.username = username;
        this.following = new HashSet<>();
        this.posts = new ArrayList<>();
    }

    public void addPost(String content) {
        posts.add(content);
    }

    public void follow(User user) {
        following.add(user);
    }

    public List<String> getPosts() {
        return posts;
    }

    public Set<User> getFollowing() {
        return following;
    }
}

class SocialNetworkGraph {
    private Map<User, Set<User>> adjacencyList;

    public SocialNetworkGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addUser(User user) {
        adjacencyList.putIfAbsent(user, new HashSet<>());
    }

    public void addConnection(User user1, User user2) {
        adjacencyList.get(user1).add(user2);
        adjacencyList.get(user2).add(user1);
    }

    public Set<User> getConnections(User user) {
        return adjacencyList.get(user);
    }

    // Recommendation algorithm based on DFS
    public List<String> recommendContent(User user) {
        List<String> recommendations = new ArrayList<>();
        Set<User> visited = new HashSet<>();

        dfs(user, visited, recommendations);

        return recommendations;
    }

    private void dfs(User user, Set<User> visited, List<String> recommendations) {
        visited.add(user);

        for (User followingUser : adjacencyList.get(user)) {
            if (!visited.contains(followingUser)) {
                recommendations.addAll(followingUser.getPosts());
                dfs(followingUser, visited, recommendations);
            }
        }
    }
}

public class Recomendation {
    public static void main(String[] args) {
        // Creating users
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        User user3 = new User("Charlie");
        User user4 = new User("David");

        SocialNetworkGraph socialGraph = new SocialNetworkGraph();

        // Adding users to the graph
        socialGraph.addUser(user1);
        socialGraph.addUser(user2);
        socialGraph.addUser(user3);
        socialGraph.addUser(user4);

        // Creating connections between users
        socialGraph.addConnection(user1, user2);
        socialGraph.addConnection(user1, user3);
        socialGraph.addConnection(user2, user4);

        // Creating posts
        user1.addPost("Post by Alice 1");
        user2.addPost("Post by Bob 1");
        user3.addPost("Post by Charlie 1");
        user4.addPost("Post by David 1");

        // User interactions
        user1.follow(user2);
        user2.follow(user3);
        user3.follow(user4);

        // Recommendation for user1
        List<String> recommendations = socialGraph.recommendContent(user1);

        System.out.println("Recommendations for " + user1.username + ":");
        for (String recommendation : recommendations) {
            System.out.println("- " + recommendation);
        }
    }
}
