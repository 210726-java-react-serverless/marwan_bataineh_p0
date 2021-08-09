package com.revature.p0.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

/**
 * The User class is a pojo which provides the template for various user objects.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String id;
    private int permissions;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    public User() { super(); }

    public User(String firstName, String lastName,
                String email,
                String username, String password) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;

    }

    /**
     * This method generates the JSON format for user input into the mongoDB.
     * @return - JSON object containing user entry fields and values.
     */
    public String toJSON() { return ""; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public int getPermissions() { return permissions; }

    public void setPermissions(int permissions) { this.permissions = permissions; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User appUser = (User) o;
        return id == appUser.id
                && Objects.equals(firstName, appUser.firstName)
                && Objects.equals(lastName, appUser.lastName)
                && Objects.equals(email, appUser.email)
                && Objects.equals(username, appUser.username)
                && Objects.equals(password, appUser.password);
    }

}
