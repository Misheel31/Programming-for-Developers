package SocialMedia;

import java.util.*;

class User {
    String username;
    Set<User> following;
    List<String> posts;
    Map<String, Integer> postLikes;

    public User(String username) {
        this.username = username;
        this.following = new HashSet<>();
        this.posts = new ArrayList<>();
        this.postLikes = new HashMap<>();
    }

    public void addPost(String content) {
        posts.add(content);
        postLikes.put(content, 0);
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

    public Map<String, Integer> getPostLikes() {
        return postLikes;
    }

    // Recommendation algorithm with enhanced factors
    public List<String> recommendContent(SocialNetworkGraph socialGraph) {
        List<String> recommendations = new ArrayList<>();
        Set<User> visited = new HashSet<>();

        dfs(this, visited, recommendations, socialGraph);
        postProcessRecommendations(recommendations, socialGraph);

        return recommendations;
    }

    private void dfs(User user, Set<User> visited, List<String> recommendations, SocialNetworkGraph socialGraph) {
        visited.add(user);

        for (User followingUser : socialGraph.getConnections(user)) {
            if (!visited.contains(followingUser)) {
                recommendations.addAll(followingUser.getPosts());
                dfs(followingUser, visited, recommendations, socialGraph);
            }
        }
    }

    private void postProcessRecommendations(List<String> recommendations, SocialNetworkGraph socialGraph) {
        Collections.sort(recommendations, Comparator.comparingInt(socialGraph::getPostPopularity).reversed());
        Set<String> uniqueRecommendations = new LinkedHashSet<>(recommendations);
        recommendations.clear();
        recommendations.addAll(uniqueRecommendations);
    }

    private int getPostLikes(String post) {
        return postLikes.getOrDefault(post, 0);
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

    // Get the popularity of a post (number of total likes)
    public int getPostPopularity(String post) {
        int totalLikes = 0;
        for (User user : adjacencyList.keySet()) {
            totalLikes += user.getPostLikes().getOrDefault(post, 0);
        }
        return totalLikes;
    }

    // Get the number of followers for a user
    public int getUserFollowers(User user) {
        return user.getFollowing().size();
    }
}

public class SocialGraph {
    public static void main(String[] args) {
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        User user3 = new User("Charlie");
        User user4 = new User("David");

        SocialNetworkGraph socialGraph = new SocialNetworkGraph();

        socialGraph.addUser(user1);
        socialGraph.addUser(user2);
        socialGraph.addUser(user3);
        socialGraph.addUser(user4);

        socialGraph.addConnection(user1, user2);
        socialGraph.addConnection(user1, user3);
        socialGraph.addConnection(user2, user4);

        user1.addPost("Post by Alice 1");
        user2.addPost("Post by Bob 1");
        user3.addPost("Post by Charlie 1");
        user4.addPost("Post by David 1");

        user1.follow(user2);
        user2.follow(user3);
        user3.follow(user4);

        List<String> recommendations = user1.recommendContent(socialGraph);

        System.out.println("Enhanced Recommendations for " + user1.username + ":");
        for (String recommendation : recommendations) {
            System.out.println("- " + recommendation);
        }
    }

    public static void addPostToUser(User user, int postId) {
        throw new UnsupportedOperationException("Unimplemented method 'addPostToUser'");
    }

    public static int getPostId(String post) {
        throw new UnsupportedOperationException("Unimplemented method 'getPostId'");
    }
}
