package com.revature.p0.models;

/**
 * The FacultyUser class provides a specific implementation of the User class for faculty users.
 */
public class FacultyUser extends User{

    public FacultyUser(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password);
    }

    @Override
    public String toJSON() {
        return null;
    }

}
