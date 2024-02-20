package SocialMedia;

import javax.swing.*;
import java.sql.*;
import java.util.*;

class SocialNetworkGraph {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/socialMedia";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "misheel123";
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

    public int getPostPopularity(String post) {
        int totalLikes = 0;
        for (User user : adjacencyList.keySet()) {
            totalLikes += user.getPostLikes().getOrDefault(post, 0);
        }
        return totalLikes;
    }

    public void storePostInDatabase(String username, String content, String photoType) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (username, content, photo_path) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, content);
            statement.setString(3, photoType);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int postId = generatedKeys.getInt(1);
                User user = getUser(username);
                if (user != null) {
                    user.addPost(content);
                    user.getPostLikes().put(content, 0);
                    SocialNetworkGraph.addPostToUser(user, postId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addPostToUser(User user, int postId) {
        user.addPost("New Photo with ID: " + postId);
        user.getPostLikes().put("New Photo with ID: " + postId, 0);

    }

    private User getUser(String username) {
        return null;
    }

    public List<String> recommendContent(User loggedInUser) {
        throw new UnsupportedOperationException("Unimplemented method 'recommendContent'");

    }

    public List<String> retrievePhotos(String username) {
        List<String> photos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT photo_path FROM posts");
            while (resultSet.next()) {
                String photoPath = resultSet.getString("photo_path");
                if (photoPath != null) {
                    photos.add(photoPath);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving photos. See console for details.");
        }
        return photos;
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


        List<String> recommendations = user1.recommendContent(socialGraph);

    }

    public static void addPostToUser(User user, int postId) {
        throw new UnsupportedOperationException("Unimplemented method 'addPostToUser'");
    }

    public static int getPostId(String post) {
        throw new UnsupportedOperationException("Unimplemented method 'getPostId'");
    }
}
