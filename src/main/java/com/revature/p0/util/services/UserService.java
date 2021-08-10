package com.revature.p0.util.services;

import com.revature.p0.dao.MongodCourseDAO;
import com.revature.p0.dao.MongodUserDAO;
import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserService {

    private final MongodUserDAO userDAO;
    private final MongodCourseDAO courseDAO;

    public UserService() {
        this.userDAO = new MongodUserDAO();
        this.courseDAO = new MongodCourseDAO();
    }

    /**
     * The login method passes a username and password (supplied by the user) to the data access object. If the login
     * was successful, a User object with the information from the database is returned.
     * @param username - username entered.
     * @param password - password entered.
     * @return - authorized User instance.
     */
    public User login(String username, String password) {

        if(username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            //throw new InvalidRequestException("Invalid user credentials provided!");
            return null;
        }

        User authUser = userDAO.readByUsernamePassword(username, password);

        if(authUser == null) {
            //throw new AuthenticationException("Invalid credentials provided!");
            return null;
        }

        return authUser; // TODO we need to store this value within app memory to use elsewhere

    }

    /**
     * The register method accepts a User object created with information entered by the user. If the information entered
     * is valid, the User instance is passed to the data access object to be entered into the database.
     * @param newUser - new User instance with information supplied by the user in the registration interface.
     * @return - new User instance upon successful registration (otherwise null).
     */
    public User register(User newUser) {

        if(!isUserValid(newUser)) {
            //throw new InvalidRequestException("Invalid user data provided!");
            System.out.println("\nUsername is invalid!");
            return null;
        }
        if(userDAO.readByUsername(newUser.getUsername()) != null) {
            //throw new ResourcePersistenceException("Provided username is already taken!");
            System.out.println("\nUsername taken!");
            return null;
        }
        if(userDAO.readByEmail(newUser.getEmail()) != null) {
            //throw new ResourcePersistenceException("Provided email is already taken!");
            System.out.println("\nEmail taken!");
            return null;
        }

        return userDAO.create(newUser);

    }

    public boolean isUserValid(User user) {
        if (user == null) return false;
        if (user.getFirstName() == null || user.getFirstName().trim().equals("")) return false;
        if (user.getLastName() == null || user.getLastName().trim().equals("")) return false;
        if (user.getEmail() == null || user.getEmail().trim().equals("")) return false;
        if (user.getUsername() == null || user.getUsername().trim().equals("")) return false;
        return user.getPassword() != null && !user.getPassword().trim().equals("");
    }

    /**
     * The isNameValid method ensures that an entered name is not null/empty, or otherwise invalid.
     * @param name - name.
     * @return - true if name is, in fact, a name.
     */
    public boolean isNameValid(String name) {
        return name != null && !name.trim().equals("")
                && !name.matches(".*\\d.*");
    }

    /**
     * Check for proper format (username@domainName.domain). Alphanumeric characters, dots and dashes ONLY. No spaces!!!
     * @param email - email given, to be checked for correctness.
     * @return - true if conditions are met.
     */
    public boolean isEmailValid(String email) {
        String username = "";
        String domainFull = "";

        try {
            username = email.split("@")[0];
            domainFull = email.split("@")[1];
        } catch(Exception e) {
            return false;
        }

        if(username.length() < 3) return false;
        if(username.trim().equals("")) return false;
        if(username.matches("\\s+") || username.matches("[^A-Za-z0-9\\.\\-]")) return false;
        if(domainFull.trim().equals("")) return false;

        String domainName = "";
        String domain = "";

        try{
            domainName = domainFull.split("\\.")[0];
            domain = domainFull.split("\\.")[1];
        } catch(Exception e) {
            return false;
        }

        if(domainName.equals("") || domainName.trim().equals("") || domainName.matches("\\s+")) return false;
        if(domain.equals("") || domain.trim().equals("") || domain.matches("\\s+")) return false;

        return true;
    }

    public boolean isEmailTaken(String email) {
        return userDAO.readByEmail(email) == null;
    }

    /**
     * Ensure username has no spaces nor special characters (aside from underscores).
     * @param username
     * @return
     */
    public boolean isUsernameValid(String username) {
        Pattern illegalCharacters = Pattern.compile("[^a-zA-Z0-9_]");
        Matcher matcher = illegalCharacters.matcher(username);
        return !username.trim().equals("") &&
                !username.matches("\\s+") &&
                !matcher.find() &&
                username.length() >= 3;

    }

    /**
     * Under construction.
     * @return
     */
    public boolean isPasswordValid(String password) {
        return true;
    }

    public Course getCourseByIdSection(String courseId, int section) {
        return courseDAO.readByCourseIdSection(courseId, section);
    }

    /**
     * This is a big no-no...
     * @param courseHeaders - list of course headers (containing id and section vars)
     * @return - reduced list of courses from full list of courses *gasp*...
     */
    public List<Course> getAllCoursesFromList(List<CourseHeader> courseHeaders) {
        List<Course> fullList = courseDAO.readAll();
        List<Course> reducedList = new ArrayList<>();
        fullList.forEach(course -> {
            if(courseHeaders.contains(new CourseHeader(course.getCourseId(), course.getSection()))) {
                reducedList.add(course);
            }
        });
        return reducedList;
    }

    public List<Course> getAllCourses() {
        return courseDAO.readAll();
    }

}
