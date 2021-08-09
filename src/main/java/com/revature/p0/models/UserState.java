package com.revature.p0.models;

/**
 * The UserState class provides a singleton instance of the logged-in user.
 */
public class UserState {
    private static UserState userState = null;
    private User activeUser = null;

    private UserState() { }

    public User getUser() {
        return activeUser;
    }

    public void setUser(User newUser) {
        activeUser = newUser;
    }

    public static UserState getInstance() {
        if(userState == null) {
           userState = new UserState();
        }
        return userState;
    }
}
