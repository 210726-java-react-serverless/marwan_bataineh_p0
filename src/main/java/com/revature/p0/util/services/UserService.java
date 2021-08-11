package com.revature.p0.util.services;

import com.revature.p0.dao.MongodCourseDAO;
import com.revature.p0.dao.MongodUserDAO;
import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.models.User;
import com.revature.p0.util.PageNavUtil;
import com.revature.p0.util.PasswordUtils;
import com.revature.p0.util.UserState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The UserService class provides business logic implementations and abstraction between DAO layer and user/client layer.
 *
 * //TODO cleanup method geographical features, pull as much validation into this layer from the DAO and client layers as possible.
 */
public class UserService {

    private final MongodUserDAO userDAO;
    private final MongodCourseDAO courseDAO;
    private final Logger logger = LogManager.getLogger(UserService.class);

    public UserService() {
        this.userDAO = new MongodUserDAO();
        this.courseDAO = new MongodCourseDAO();
    }

    public UserService(MongodUserDAO userDAO, MongodCourseDAO courseDAO) {
        this.userDAO = userDAO;
        this.courseDAO = courseDAO;
    }

    /**
     * The login method passes a username and password (supplied by the user) to the data access object. If the login
     * was successful, a User object with the information from the database is returned.
     * @param username - username entered.
     * @param password - password entered.
     * @return - authorized User instance.
     */
    public User login(String username, String password) {

        Properties appProperties = new Properties();

        if(username == null || username.trim().equals("") || password == null || password.trim().equals("")) {
            //throw new InvalidRequestException("Invalid user credentials provided!");
            return null;
        }

        try {
            appProperties.load(new FileReader("src/main/resources/application.properties"));
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        String securePassword = PasswordUtils.generateSecurePassword(password, appProperties.getProperty("salt"));

        User authUser = userDAO.readByUsernamePassword(username, securePassword);

        if(authUser == null) {
            //throw new AuthenticationException("Invalid credentials provided!");
            return null;
        }

        return authUser; // TODO we need to store this value within app memory to use elsewhere

    }

    /**
     * Clears the UserState and sets destination page to "Landing Page" (or Home).+
     */
    public void logout() {
        UserState.getInstance().logout();
        PageNavUtil.getInstance().portalHome();
    }

    /**
     * The register method accepts a User object created with information entered by the user. If the information entered
     * is valid, the User instance is passed to the data access object to be entered into the database.
     * @param newUser - new User instance with information supplied by the user in the registration interface.
     * @return - new User instance upon successful registration (otherwise null).
     */
    public User register(User newUser) {

        Properties appProperties = new Properties();

        User returnUser = new User();

        if(!isNameValid(newUser.getFirstName())) {
            //throw new InvalidRequestException("Invalid user data provided!");
            System.out.println("\nFirst name is invalid!");
            returnUser = null;
        }
        if(!isNameValid(newUser.getLastName())) {
            System.out.println("\nLast name is invalid!");
            returnUser = null;
        }
        if(!isEmailValid(newUser.getEmail())) {
            System.out.println("\nEmail is invalid!");
            returnUser = null;
        }
        if(!isUsernameValid(newUser.getUsername())) {
            System.out.println("\nUsername is invalid!");
            returnUser = null;
        }
        if(!isPasswordValid(newUser.getPassword())) {
            System.out.println("\nPassword is invalid!");
            returnUser = null;
        }
        if(userDAO.readByUsername(newUser.getUsername()) != null) {
            //throw new ResourcePersistenceException("Provided username is already taken!");
            System.out.println("\nUsername taken!");
            returnUser = null;
        }
        if(userDAO.readByEmail(newUser.getEmail()) != null) {
            //throw new ResourcePersistenceException("Provided email is already taken!");
            System.out.println("\nEmail taken!");
            returnUser = null;
        }

        try {
            appProperties.load(new FileReader("src/main/resources/application.properties"));
        } catch(Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        if(returnUser != null) {
            String securePassword = PasswordUtils.generateSecurePassword(newUser.getPassword(), appProperties.getProperty("salt"));
            newUser.setPassword(securePassword);
            return userDAO.create(newUser);
        } else {
            return null;
        }

    }

    /**
     * Leverages various input validation methods into one method for checking if a User is valid in all its entries.
     * @param user - check this User.
     * @return - true if the User passes all validation; false if it fails any.
     */
    public boolean isUserValid(User user) {
        if (!isNameValid(user.getFirstName()) || !isNameValid(user.getLastName())) return false;
        if (!isEmailValid(user.getEmail())) return false;
        if (!isUsernameValid(user.getUsername())) return false;
        if (!isPasswordValid(user.getPassword())) return false;
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
     * Calls the DAO to search for given username and respond with whether it exists already.
     * @param username - check this username.
     * @return - true if the username exists in the db; false if otherwise.
     */
    public boolean isUsernameTaken(String username) {
        return userDAO.readByUsername(username) != null;
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
        return userDAO.readByEmail(email) != null;
    }

    /**
     * Ensure username has no spaces nor special characters (aside from underscores).
     * @param username - check this username.
     * @return - true if username passed validation; false if otherwise.
     */
    public boolean isUsernameValid(String username) {
        if(username == null || username.trim().equals("")) return false;
        Pattern illegalCharacters = Pattern.compile("[^a-zA-Z0-9_]");
        Matcher matcher = illegalCharacters.matcher(username);
        return !username.trim().equals("") &&
                !username.matches("\\s+") &&
                !matcher.find() &&
                username.length() >= 3;

    }

    /**
     * Check if password is valid or not.
     * @return - true if valid; false if otherwise.
     */
    public boolean isPasswordValid(String password) {
        return password != null && !password.trim().equals("") &&
                !password.matches("[\\s+]") && password.length() >= 6;
    }

    /**
     * Ensure the field being entered passes requirements.
     * @param field - check this field.
     * @return - true if field is valid; false if otherwise.
     */
    public boolean isFieldValid(String field) {
        if(field == null || field.trim().equals("")) return false; // Is empty
        if(field.matches("\\s+")) return false; // Contains whitespace
        if(field.matches("[0-9]")) return false; // Contains number(s)
        if(field.length() < 2) return false; // Must be at least 2 characters long
        return field.equalsIgnoreCase("cs") || // Computer Science
                field.equalsIgnoreCase("biol") || // Biology
                field.equalsIgnoreCase("phil") || // Philosophy
                field.equalsIgnoreCase("chem") || // Chemistry
                field.equalsIgnoreCase("soc") || // Sociology
                field.equalsIgnoreCase("engl") || // English
                field.equalsIgnoreCase("math"); // Mathematics
    }

    /**
     * Pass Course search to DAO.
     * @param courseId - courseId record.
     * @param section - section record.
     * @return - copy of Course if it exists; null if otherwise.
     */
    public Course getCourseByIdSection(String courseId, int section) {
        return courseDAO.readByCourseIdSection(courseId, section);
    }

    /**
     * Pass information to DAO necessary for updated a User's Course list.
     * @param username - User to update.
     * @param courseHeader - Course to add.
     * @return - true if DAO returns a copy; false if it returns null.
     */
    public boolean addCourseToStudent(String username, CourseHeader courseHeader) {
        Course course = getCourseByIdSection(courseHeader.getCourseId(), courseHeader.getSection());
        if(course == null) {
            System.out.println("\nCouldn't find course!");
            return false;
        }

        if(!updateAddUserCourse(username, course)) {
            System.out.println("\nFailed to add course!");
            return false;
        }
        System.out.println("\nCourse added!");
        return true;
    }

    /**
     * Pass DAO new Course to be added.
     * @param newCourse - new Course.
     * @return - copy of new Course if operation was successful; null if otherwise.
     */
    public Course addCourse(Course newCourse) {
        Course returnedCourse = courseDAO.create(newCourse);
        if(returnedCourse == null) {
            System.out.println("\nFailed to add course!");
            return null;
        }
        System.out.println("\nCourse added!");
        return returnedCourse;
    }

    /**
     * [FIXED!] Send a list of CourseHeaders and get a list of Courses back.
     * @param courseHeaders - list of course headers (containing id and section vars)
     * @return - list of courses...
     */
    public List<Course> getAllCoursesFromList(List<CourseHeader> courseHeaders) {
        if(courseHeaders.size() <= 0 || courseHeaders == null) return new ArrayList<>();
        return courseDAO.readByCourseHeaders(courseHeaders);
    }

    /**
     * Pass DAO field record to query for a list of Courses matching that field.
     * @param field - field filter.
     * @return - list of Courses.
     */
    public List<Course> getCoursesByField(String field) {
        return courseDAO.readByField(field);
    }

    /**
     * Request ALL Courses from db from DAO.
     * @return - list of all Courses.
     */
    public List<Course> getAllCourses() {
        return courseDAO.readAll();
    }

    /**
     * Add a course (courseId and section) to the user's "courses" array in the db and update space in that course.
     * @param username
     * @param course
     * @return - success/fail.
     */
    public boolean updateAddUserCourse(String username, Course course) { //TODO cleanup that nonsense
        if(course.getSpace() <= 0) {
            System.out.println("\nThat course is full!");
            return false;
        }

        List<CourseHeader> userCourseHeaders = UserState.getInstance().getUser().getCourses();
        for(int i = 0; i < userCourseHeaders.size(); i++) {
            if (userCourseHeaders.get(i).getCourseId().equals(course.getCourseId())) {
                System.out.println("\nYou're already registered for that course!");
                return false;
            }
        }

        if(!userDAO.updateAddUserCourseList(username, course)) {
            System.out.println("\nThere was an issue adding course.");
            return false;
        }

        UserState.getInstance().getUser().getCourses().add(new CourseHeader(course.getCourseId(), course.getSection()));
        courseDAO.updateCourseSpace(new CourseHeader(course.getCourseId(), course.getSection()), -1);
        return true;
    }

    /**
     * Removes a Course from a User's Course list.
     * @param username - User.
     * @param course - Course to remove.
     * @return - true if operation succeeded; false if otherwise.
     */
    public boolean updateDeleteUserCourse(String username, Course course) {
        if(!userDAO.updateDeleteUserCourseList(username, course)) {
            System.out.println("\nThere was an issue dropping course.");
            return false;
        }
        UserState.getInstance().getUser().getCourses().remove(new CourseHeader(course.getCourseId(), course.getSection()));
        courseDAO.updateCourseSpace(new CourseHeader(course.getCourseId(), course.getSection()), 1);
        return true;
    }

    /**
     * Update the records of a single Course (request op from DAO).
     * @param course - Course.
     * @return - true if operation was successful; false if otherwise.
     */
    public boolean updateCourse(Course course) {
        if(course == null) {
            System.out.println("\nCourse is null.");
            return false;
        }
        if(!courseDAO.updateCourse(course)) {
            System.out.println("\nFailed to update course.");
            return false;
        }
        System.out.println("\nCourse updated!");
        return true;
    }

    /**
     * Removes a Course from the db and updates any Users who had that Course in their list (request ops from DAO).
     * @param courseHeader - Course Identification for Course to be deleted.
     * @return - true if operation was successful; false if otherwise.
     */
    public boolean removeCourse(CourseHeader courseHeader) {
        if(!courseDAO.deleteCourseByCourseHeader(courseHeader)) {
            System.out.println("\nFailed to remove course.");
            return false;
        }
        if(!userDAO.deleteAllUserCoursesByCourseHeader(courseHeader)) {
            System.out.println("\nFailed to remove courses from users. Possible corruption...");
            return false;
        }
        return true;
    }

}
