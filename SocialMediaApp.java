import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SocialMediaApp {

    private static class User {
        String username;

        public User(String username) {
            this.username = username;
        }
    }

    private static class Post {
        String postId;
        String content;
        User user;

        public Post(String postId, String content, User user) {
            this.postId = postId;
            this.content = content;
            this.user = user;
        }
    }

    private static class Photo {
        String photoId;
        String description;
        User user;

        public Photo(String photoId, String description, User user) {
            this.photoId = photoId;
            this.description = description;
            this.user = user;
        }
    }

    private static class RegistrationGUI extends JFrame {
        private JTextField usernameField;
        private JButton registerButton;

        public RegistrationGUI() {
            initUI();
        }

        private void initUI() {
            setTitle("Registration");
            setSize(300, 150);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());

            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField();
            registerButton = new JButton("Register");
            registerButton.addActionListener(new RegisterButtonListener());

            mainPanel.add(usernameLabel, BorderLayout.NORTH);
            mainPanel.add(usernameField, BorderLayout.CENTER);
            mainPanel.add(registerButton, BorderLayout.SOUTH);

            setContentPane(mainPanel);
            setLocationRelativeTo(null);
        }

        private class RegisterButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                if (!username.isEmpty()) {
                    User newUser = registerUser(username);
                    openSocialMediaGUI(newUser);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a valid username.");
                }
            }
        }

        private User registerUser(String username) {
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User newUser = new User(username);
                session.save(newUser);

                session.getTransaction().commit();

                return newUser;
            } finally {
                sessionFactory.close();
            }
        }

        private void openSocialMediaGUI(User user) {
            SwingUtilities.invokeLater(() -> {
                SocialMediaGUI socialMediaGUI = new SocialMediaGUI();
                socialMediaGUI.setLoggedInUser(user);
                socialMediaGUI.setVisible(true);
                dispose();
            });
        }
    }

    private static class SocialMediaGUI extends JFrame {
        private JTextArea contentTextArea;
        private JTextArea postsTextArea;
        private JComboBox<String> usersComboBox;
        private DefaultComboBoxModel<String> usersComboBoxModel;

        private User loggedInUser;

        public SocialMediaGUI() {
            initUI();
        }

        private void initUI() {
            setTitle("Social Media App");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());

            contentTextArea = new JTextArea();
            JButton postButton = new JButton("Post");
            postButton.addActionListener(new PostButtonListener());

            JButton postPhotoButton = new JButton("Post Photo");
            postPhotoButton.addActionListener(new PostPhotoButtonListener());

            postsTextArea = new JTextArea();
            JScrollPane postsScrollPane = new JScrollPane(postsTextArea);

            usersComboBoxModel = new DefaultComboBoxModel<>();
            usersComboBox = new JComboBox<>(usersComboBoxModel);

            JButton followButton = new JButton("Follow");
            followButton.addActionListener(new FollowButtonListener());

            mainPanel.add(contentTextArea, BorderLayout.NORTH);
            mainPanel.add(postButton, BorderLayout.CENTER);
            mainPanel.add(postPhotoButton, BorderLayout.CENTER);
            mainPanel.add(postsScrollPane, BorderLayout.CENTER);
            mainPanel.add(usersComboBox, BorderLayout.CENTER);
            mainPanel.add(followButton, BorderLayout.CENTER);

            setContentPane(mainPanel);
            setLocationRelativeTo(null);
        }

        private class PostButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = contentTextArea.getText();
                if (!content.isEmpty()) {
                    postContent(content);
                    contentTextArea.setText("");
                    updatePostsTextArea();
                }
            }
        }

        private class PostPhotoButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String photoId = java.util.UUID.randomUUID().toString();
                String description = JOptionPane.showInputDialog(null, "Enter photo description:");
                postPhoto(description);
                updatePostsTextArea();
            }
        }

        private class FollowButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedUsername = (String) usersComboBox.getSelectedItem();
                if (selectedUsername != null) {
                    followUser(selectedUsername);
                }
            }
        }

        private void postContent(String content) {
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                Post post = new Post(java.util.UUID.randomUUID().toString(), content, loggedInUser);
                session.save(post);

                session.getTransaction().commit();
            } finally {
                sessionFactory.close();
            }
        }

        private void postPhoto(String description) {
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                Photo photo = new Photo(java.util.UUID.randomUUID().toString(), description, loggedInUser);
                session.save(photo);

                session.getTransaction().commit();
            } finally {
                sessionFactory.close();
            }
        }

        private void followUser(String usernameToFollow) {
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                User userToFollow = session.get(User.class, usernameToFollow);
                if (userToFollow != null) {
                    loggedInUser.following.add(userToFollow);
                    session.update(loggedInUser);

                    userToFollow.followers.add(loggedInUser);
                    session.update(userToFollow);
                }

                session.getTransaction().commit();
                updateUsersComboBox();
            } finally {
                sessionFactory.close();
            }
        }

        private void updatePostsTextArea() {
            StringBuilder postsText = new StringBuilder();
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                // Fetch posts
                java.util.List<Post> posts = session.createQuery("FROM Post WHERE user = :user", Post.class)
                        .setParameter("user", loggedInUser)
                        .list();

                for (Post post : posts) {
                    postsText.append("Post: ").append(post.content).append("\n");
                }

                // Fetch photos
                java.util.List<Photo> photos = session.createQuery("FROM Photo WHERE user = :user", Photo.class)
                        .setParameter("user", loggedInUser)
                        .list();

                for (Photo photo : photos) {
                    postsText.append("Photo: ").append(photo.description).append("\n");
                }

                session.getTransaction().commit();
            } finally {
                sessionFactory.close();
            }

            postsTextArea.setText(postsText.toString());
        }

        private void updateUsersComboBox() {
            usersComboBoxModel.removeAllElements();
            SessionFactory sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

            try (Session session = sessionFactory.openSession()) {
                session.beginTransaction();

                java.util.List<User> followingUsers = new java.util.ArrayList<>(loggedInUser.following);
                followingUsers.forEach(user -> usersComboBoxModel.addElement(user.username));

                session.getTransaction().commit();
            } finally {
                sessionFactory.close();
            }
        }

        public void setLoggedInUser(User user) {
            this.loggedInUser = user;
            updateUsersComboBox();
            updatePostsTextArea();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationGUI registrationGUI = new RegistrationGUI();
            registrationGUI.setVisible(true);
        });
    }
}
