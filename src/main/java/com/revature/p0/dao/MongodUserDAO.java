package com.revature.p0.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.models.User;
import com.revature.p0.util.MongoClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The MongodUserDAO provides the database connectivity layer for the application. Facilitates CRUD operations on the
 * users collection. //TODO need to bring over Course DAO and abstract mongo connectivity out of individual methods.
 */
public class MongodUserDAO implements CRUD<User>{

    private final Logger logger = LogManager.getLogger(MongodUserDAO.class);

    /**
     * Inserts a new entry into the users collection in p0.
     * @param newUser
     * @return - User is insertion is successful; null if otherwise.
     */
    @Override
    public User create(User newUser) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase bookstoreDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");

            Document newUserDoc = new Document("permissions", newUser.getPermissions())
                    .append("firstName", newUser.getFirstName())
                    .append("lastName", newUser.getLastName())
                    .append("email", newUser.getEmail())
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getPassword());

            usersCollection.insertOne(newUserDoc);
            Bson bson = newUserDoc.toBsonDocument();
            newUser.setId(newUserDoc.get("_id").toString());

            logger.info("User: " + newUser.getUsername() + "successfully registered!");

            return newUser;

        } catch (Exception e) {
            //e.printStackTrace();
            //throw new DataSourceException("An unexpected exception occurred.", e);
            logger.error(e.getMessage());
            logger.debug("User not registered!");
            return null;
        }
    }

    @Override
    public User read(int id) {
        return null;
    }

    public User readByID(int id) {
        return null;
    }

    public User readByUsername(String username) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");
            Document queryDoc = new Document("username", username);
            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                logger.info("Username: " + username + " does not already exist.");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            logger.info("Username: " + username + " already exists.");
            return authUser;

        } catch (JsonMappingException e) {
//            e.printStackTrace();
//            throw new DataSourceException("An exception occurred while mapping the document.", e);
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
//            e.printStackTrace();
//            throw new DataSourceException("An unexpected exception occurred.", e);
            logger.error(e.getMessage());
            return null;
        }
    }

    public User readByUsernamePassword(String username, String password) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");
            Document queryDoc = new Document("username", username);
            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                logger.info("User: " + username + " not found.");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            // Create courses list (CourseHeaders) from doc and insert into user
            List<Document> coursesList = authUserDoc.getList("courses", Document.class);
            if(coursesList != null) {
                Iterator<Document> it = coursesList.iterator();
                List<CourseHeader> courses = new ArrayList<CourseHeader>();

                while (it.hasNext()) {
                    Document doc = it.next();
                    CourseHeader course = mapper.readValue(doc.toJson(), CourseHeader.class);
                    courses.add(course);
                }
                authUser.setCourses(courses);
            } else authUser.setCourses(null);

            if(!authUser.getPassword().equals(password)) {
                logger.info("User: " + username + " not authorized.");
                return null;
            }

            logger.info("User: " + authUser.getUsername() + " authorized.");
            return authUser;

        } catch (JsonMappingException e) {
//            e.printStackTrace();
//            throw new DataSourceException("An exception occurred while mapping the document.", e);
            logger.error(e.getMessage());
            logger.debug("Failed to authenticate user.");
            return null;
        } catch (Exception e) {
//            e.printStackTrace();
//            throw new DataSourceException("An unexpected exception occurred.", e);
            logger.error(e.getMessage());
            logger.debug("Failed to authenticate user.");
            return null;
        }
    }

    public User readByEmail(String email) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");
            Document queryDoc = new Document("email", email);
            Document authUserDoc = usersCollection.find(queryDoc).first();

            if (authUserDoc == null) {
                logger.info("Email: " + email + " does not already exist.");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            logger.info("Email: " + email + " already exists.");
            return authUser;
        } catch(JsonProcessingException e) {
//            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        } catch(Exception e) {
//            e.printStackTrace();
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(User updatedResource) {
        return false;
    }

    public boolean updateAddUserCourseList(String username, Course course) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");

            usersCollection.findOneAndUpdate(Filters.eq("username", username),
                    new Document().append(
                            "$push",
                            new Document("courses", new Document("courseId", course.getCourseId())
                                    .append("section", course.getSection()))
                    ));
            logger.info("Course: " + course.getCourseId() + ", Section: " + course.getSection() + " added to user " + username);
            return true;
        } catch(Exception e) {
//            System.out.println("\nThere was a problem updating your course list.");
            logger.error(e.getMessage());
            logger.debug("Failed to add course to user.");
            return false;
        }
    }

    public boolean updateDeleteUserCourseList(String username, Course course) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");

            usersCollection.findOneAndUpdate(Filters.eq("username", username),
                    new Document().append(
                            "$pull",
                            new Document("courses", new Document("courseId", course.getCourseId())
                                    .append("section", course.getSection()))
                    ));

            logger.info("Course: " + course.getCourseId() + ", Section: " + course.getSection() + " removed from user " + username);
            return true;
        } catch(Exception e) {
//            System.out.println("\nThere was a problem removing your course.");
            logger.error(e.getMessage());
            logger.debug("Failed to remove course from user.");
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    public boolean deleteAllUserCoursesByCourseHeader(CourseHeader courseHeader) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = p0Database.getCollection("users");

            Document queryDoc = new Document("courses", new Document("courseId", courseHeader.getCourseId())
                    .append("section", courseHeader.getSection()));
            usersCollection.updateMany(queryDoc,
                    new Document().append(
                            "$pull",
                            queryDoc
                    ));

            logger.info("Course: " + courseHeader.getCourseId() + ", Section: " + courseHeader.getSection() + " successfully removed from users' lists");
            return true;
        } catch(Exception e) {
            logger.error(e.getMessage());
            logger.debug("Course: " + courseHeader.getCourseId() + ", Section: " + courseHeader.getSection() + " failed to be fully removed " +
                    "from users' lists. Possible corruption/incomplete operation");
            return false;
        }
    }

}
