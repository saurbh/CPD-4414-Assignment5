/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author c0666985
 */
@ManagedBean
@ApplicationScoped
public class Posts {
    List<Post> posts;
    Post currentPost;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Post getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(Post currentPost) {
        this.currentPost = currentPost;
    }
    
    public void Posts(){
        currentPost  = new Post( -1, -1, "", null, "");
        getPostsfromDB();
    }
    
    public void getPostsfromDB() {
        try {
            posts = new ArrayList<>();
            Connection conn=DBUtils.getConnection();
            Statement st=conn.createStatement();
            ResultSet rs = st.executeQuery("select * from posts");
            while(rs.next()){
            Post p=new Post(rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("title"),
                        rs.getTimestamp("created_time"),
                        rs.getString("contents"));
            posts.add(p);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
            posts = new ArrayList<>();
        }
        
    }
    
    public Post getPostbyID(int id){
        for(Post p: posts){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }
    
    public Post getPostbyTitle(String title){
        for(Post p:posts){
            if(p.getTitle().equals(title)){
                return p;
            }            
        }
        return null;
    }
    
    public String viewPost(Post post) {
        currentPost = post;
        return "viewPost";
    }

    /**
     * Navigate to add a new post
     *
     * @return the navigation rule
     */
    public String addPost() {
        currentPost = new Post(-1, -1, "", null, "");
        return "editPost";
    }

    /**
     * Navigate to edit the current post
     *
     * @return the navigation rule
     */
    public String editPost() {
        return "editPost";
    }
    
    public String cancelPost() {
        // currentPost can be corrupted -- reset it based on the DB
        int id = currentPost.getId();
        getPostsfromDB();
        currentPost = getPostbyID(id);
        return "viewPost";
    }
    
    public String savePosts(User u){
        
        try (Connection conn = DBUtils.getConnection()) {
            // If there's a current post, update rather than insert
            if (currentPost.getId() >= 0) {
                String sql = "UPDATE posts SET title = ?, contents = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, currentPost.getTitle());
                pstmt.setString(2, currentPost.getContents());
                pstmt.setInt(3, currentPost.getId());
                pstmt.executeUpdate();
            } else {
                String sql = "INSERT INTO posts (user_id, title, created_time, contents) VALUES (?,?,NOW(),?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, u.getId());
                pstmt.setString(2, currentPost.getTitle());
                pstmt.setString(3, currentPost.getContents());
                pstmt.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPostsfromDB();
        // Update the currentPost so that its details appear after navigation
        currentPost = getPostbyTitle(currentPost.getTitle());
        return "viewPost";
    }

    
}
