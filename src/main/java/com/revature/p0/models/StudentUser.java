package com.revature.p0.models;

/**
 * The StudentUser class provides a specific implementation of the User class for student users.
 */
public class StudentUser extends User{

    public StudentUser(String firstName, String lastName, String email, String username, String password) {
        super(firstName, lastName, email, username, password);
    }

    @Override
    public String toJSON() {
        return null;
    }

}
