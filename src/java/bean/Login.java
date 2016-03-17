/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author c0666985
 */


@ManagedBean
@SessionScoped
public class Login implements Serializable {

    private String username;
    private String password;
    private boolean loggedIn;
    private User currentUser;

    /**
     * No-arg constructor -- establishes as not logged in
     */
    public Login() {
        username = null;
        password = null;
        loggedIn = false;
        currentUser = null;
    }

    /**
     * Retrieves the User's username
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes the User's username
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the User's password
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Stores the User's password
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the User's logged-in status
     *
     * @return the logged-in status
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * Retrieves the actual User object
     *
     * @return the User object
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks the user's credentials against the known credentials in the DB
     *
     * @return the next page to navigate to
     */
    public String login() {
        // Hash the current password to compare against DB
        String passhash = DBUtils.hash(password);

        // Check for a Match against the Users List
        for (User u : Users.getInstance().getUsers()) {
            if (username.equals(u.getUsername())
                    && passhash.equals(u.getPasshash())) {
                loggedIn = true;
                currentUser = u;
                return "index";
            }
        }
        // If the Loop Ends -- No User Exists
        currentUser = null;
        loggedIn = false;
        return "index";
    }
}