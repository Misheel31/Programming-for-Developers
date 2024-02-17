package SocialMedia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private List<User> following;
    private List<String> posts;
    private Map<String, Integer> postLikes;
    private Map<String, List<String>> postComments;

    public User(String username) {
        this.username = username;
        this.following = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.postLikes = new HashMap<>();
        this.postComments = new HashMap<>();
    }

    public void addPost(String content) {
        posts.add(content);
    }

    public void addInteraction(String post, String type) {
    }

    public List<String> recommendContent(SocialNetworkGraph socialGraph) {
        return Collections.emptyList();
    }

    public List<String> getPosts() {
        return posts;
    }

    public Map<String, Integer> getPostLikes() {
        return postLikes;
    }

    public Map<String, List<String>> getPostComments() {
        return postComments;
    }

}