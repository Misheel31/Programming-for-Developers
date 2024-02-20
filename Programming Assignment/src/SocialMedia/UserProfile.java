package SocialMedia;

import java.sql.*;
import java.util.*;
public class UserProfile {
    public static void main(String[] args) {
        // Creating users
        User user1 = new User("Alice");
        User user2 = new User("Bob");
        User user3 = new User("Charlie");
        User user4 = new User("David");

        // Creating social graph
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


    }

    public String getPhotoPath() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPhotoPath'");
    }
}
