import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class User {
    String username;
    List<User> following;

    public User(String username) {
        this.username = username;
        this.following = new ArrayList<>();
    }
}

public class SocialMediaApp extends JFrame {
    private String loggedInUsername;
    private Map<String, User> users;
    private Map<String, List<String>> interactions; 
    public SocialMediaApp() {
        setTitle("Social Media Application");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        users = new HashMap<>();
        interactions = new HashMap<>();

        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JButton loginButton = new JButton("Login");
        JLabel createAccountLabel = new JLabel("Don't have an account?");
        JButton createAccountButton = new JButton("Create Account");

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel()); // Empty cell
        loginPanel.add(loginButton);
        loginPanel.add(createAccountLabel);
        loginPanel.add(createAccountButton);

        add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (username != null && !username.isEmpty()) {
                    loggedInUsername = username;
                    showHomePage();
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a username.");
                }
            }
        });

        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter username:");
                if (username != null && !username.isEmpty()) {
                    if (!users.containsKey(username)) {
                        users.put(username, new User(username));
                        JOptionPane.showMessageDialog(null, "Account created successfully for " + username);
                        loggedInUsername = username;
                        showHomePage();
                    } else {
                        JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different one.");
                    }
                }
            }
        });
    }

    private void showHomePage() {
        JFrame homePage = new JFrame("Home Page");
        homePage.setSize(600, 400);
        homePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel homePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to the Home Page, " + loggedInUsername);
        JButton logoutButton = new JButton("Logout");
        JButton exploreButton = new JButton("Explore");

        homePanel.add(welcomeLabel, BorderLayout.NORTH);
        homePanel.add(logoutButton, BorderLayout.SOUTH);
        homePanel.add(exploreButton, BorderLayout.CENTER);

        homePage.add(homePanel);
        homePage.setVisible(true);

        setVisible(false);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(true);
                homePage.dispose();
            }
        });

        exploreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExplorePage();
            }
        });
    }

    private void showExplorePage() {
        JFrame explorePage = new JFrame("Explore Page");
        explorePage.setSize(600, 400);
        explorePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel explorePanel = new JPanel(new BorderLayout());
        JLabel exploreLabel = new JLabel("Explore content based on recommendations");

        JButton recommendButton = new JButton("Get Recommendations");
        JTextArea recommendationsTextArea = new JTextArea();

        explorePanel.add(exploreLabel, BorderLayout.NORTH);
        explorePanel.add(recommendButton, BorderLayout.SOUTH);
        explorePanel.add(recommendationsTextArea, BorderLayout.CENTER);

        explorePage.add(explorePanel);
        explorePage.setVisible(true);

        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<String> recommendedContent = recommendContent(loggedInUsername);
                recommendationsTextArea.setText("Recommended Content:\n" + String.join("\n", recommendedContent));
            }
        });
    }

    private List<String> recommendContent(String username) {
        // Placeholder recommendation logic
        List<String> recommendations = new ArrayList<>();
        recommendations.add("Video 1");
        recommendations.add("Article 2");
        recommendations.add("Image 3");
        return recommendations;
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
