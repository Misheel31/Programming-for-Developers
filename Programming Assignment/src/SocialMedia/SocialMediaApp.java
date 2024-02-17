package SocialMedia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.List;

class User {
    String username;
    Set<User> following;
    Map<String, Integer> postLikes;
    private ArrayList<String> posts;

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

    public void likePost(String post) {
        postLikes.put(post, postLikes.get(post) + 1);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public ArrayList<String> getPosts() {
        return posts;
    }

    public Map<String, Integer> getPostLikes() {
        return postLikes;
    }

    public void addInteraction(String post, String type) {
    }

    public void commentOnPost(String post, String comment) {
    }
}

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

    public int getUserFollowers(User user) {
        return user.getFollowing().size();
    }

    public void storePostInDatabase(String username, String content, String photoPath) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO posts (username, content, photo_path) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, content);
            statement.setString(3, photoPath);
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
        throw new UnsupportedOperationException("Unimplemented method 'addPostToUser'");
    }

    private User getUser(String username) {
        return null;
    }

    public void storeInteractionInDatabase(String username, String post, String type) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO interactions (username, post_id, interaction_type) VALUES (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setInt(2, getPostId(post));
            statement.setString(3, type);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPostId(String post) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT id FROM posts WHERE content = ?")) {
            statement.setString(1, post);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<String> retrieveOtherUsersPosts(String loggedInUsername) {

        return new ArrayList<>();
    }

    public List<String> retrievePhotos() {
        List<String> photos = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT photo_path FROM posts");
            while (resultSet.next()) {
                photos.add(resultSet.getString("photo_path"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public List<String> recommendContent(User loggedInUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recommendContent'");
    }
}

public class SocialMediaApp extends JFrame {
    private String loggedInUsername;
    private Map<String, User> users;
    private SocialNetworkGraph socialGraph;

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/socialMedia";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "misheel123";

    public SocialMediaApp() {
        setTitle("Social Media Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        users = new HashMap<>();
        socialGraph = new SocialNetworkGraph();

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordJLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField();

        JButton loginButton = new JButton("Register");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);

        loginPanel.add(passwordJLabel);
        loginPanel.add(passwordField);

        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                    registerUser(username, password);

                    loggedInUsername = username;
                    showHomePage();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a username and password.");
                }
            }
        });
    }

    private void registerUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO register (Username, Password) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            User newUser = new User(username);
            users.put(username, newUser);
            socialGraph.addUser(newUser);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showHomePage() {
        JFrame homePage = new JFrame("Home Page");
        homePage.setSize(800, 600);
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        JPanel homePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome," + loggedInUsername);
        JButton logoutButton = new JButton("Logout");
    
        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(logoutButton, BorderLayout.SOUTH);
    
        JButton uploadButton = new JButton("Upload Photo");
        JButton viewPhotosButton = new JButton("View Photos");
    
        // Add JLabel to display uploaded photo
        JLabel uploadedPhotoLabel = new JLabel();
        uploadedPhotoLabel.setPreferredSize(new Dimension(200, 200)); // Set your preferred width and height
        homePanel.add(uploadedPhotoLabel, BorderLayout.CENTER);

        JLabel recommendationsLabel = new JLabel("Recommendations:");
        JTextArea recommendationsTextArea = new JTextArea();
        recommendationsTextArea.setEditable(false); 
        JScrollPane recommendationsScrollPane = new JScrollPane(recommendationsTextArea);

        homePanel.add(recommendationsLabel, BorderLayout.WEST);
        homePanel.add(recommendationsScrollPane, BorderLayout.EAST);


        uploadButton.addActionListener(new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showOpenDialog(null);
            if (option == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String photoPath = selectedFile.getAbsolutePath();
                socialGraph.storePostInDatabase(loggedInUsername, "New Photo", photoPath);
                // Update the displayed photo
                ImageIcon imageIcon = new ImageIcon(photoPath);
                // Resize the image to preferred size
                Image scaledImage = imageIcon.getImage().getScaledInstance(uploadedPhotoLabel.getWidth(), uploadedPhotoLabel.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);
                uploadedPhotoLabel.setIcon(scaledIcon);
                JOptionPane.showMessageDialog(null, "Photo uploaded successfully!");

                User loggedInUser = users.get(loggedInUsername);
                List<String> recommendations = socialGraph.recommendContent(loggedInUser);

                // Display recommendations in JTextArea
                StringBuilder recommendationsText = new StringBuilder();
                for (String recommendation : recommendations) {
                    recommendationsText.append("- ").append(recommendation).append("\n");
                }
                recommendationsTextArea.setText(recommendationsText.toString());
            }
        }
    });
        
        viewPhotosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> photos = socialGraph.retrievePhotos();
                StringBuilder message = new StringBuilder("Available Photos:\n");
                for (String photo : photos) {
                    message.append(photo).append("\n");
                }
                JOptionPane.showMessageDialog(null, message.toString());
            }
        });
    
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loggedInUsername = null;
                showLoginPage();
                homePage.dispose();
            }
        });
    
        homePanel.add(uploadButton, BorderLayout.WEST);
        homePanel.add(viewPhotosButton, BorderLayout.EAST);
    
        homePage.add(homePanel);
        homePage.setVisible(true);
    }

    private void showLoginPage() {
        getContentPane().removeAll();
        getContentPane().revalidate();
        getContentPane().repaint();

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passwordJLabel = new JLabel("Password:");
        JTextField passwordField = new JTextField();

        JButton loginButton = new JButton("Login");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);

        loginPanel.add(passwordJLabel);
        loginPanel.add(passwordField);

        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                if (validateUser(username, password)) {
                    loggedInUsername = username;
                    showHomePage();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password. Please try again.");
                }
            }
        });
    }

    private boolean validateUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM register WHERE Username = ? AND Password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SocialMediaApp().setVisible(true);
            }
        });
    }
}
