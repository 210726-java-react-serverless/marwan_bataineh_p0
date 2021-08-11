package com.revature.p0.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.util.MongoClientFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * The MongodCourseDAO provides a data access object for Course specific operations on the courses collection in the
 * p0 database. //TODO This is bad... Consolidate DAO's and abstract them from specific db implementation.
 */
public class MongodCourseDAO implements CRUD<Course>{

    private final Logger logger = LogManager.getLogger(MongodCourseDAO.class);

    /**
     * Inserts a new Course into db.
     * @param newCourse - new Course.
     * @return - Copy of course if operation is successful; null if otherwise.
     */
    @Override
    public Course create(Course newCourse) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document insertDoc = new Document("courseId", newCourse.getCourseId())
                    .append("courseName", newCourse.getCourseName())
                    .append("section", newCourse.getSection())
                    .append("field", newCourse.getField())
                    .append("level", newCourse.getLevel())
                    .append("space", newCourse.getSpace())
                    .append("capacity", newCourse.getCapacity());

            coursesCollection.insertOne(insertDoc);

            logger.info("Course: " + newCourse.getCourseId() + ", Section: " + newCourse.getSection() + " successfully inserted.");
            return newCourse;
        } catch(Exception e) {
            logger.error(e.getMessage());
            logger.debug("Course: " + newCourse.getCourseId() + ", Section: " + newCourse.getSection() + " failed to insert.");
            return null;
        }
    }

    @Override
    public Course read(int id) {
        return null;
    }

    /**
     * Retrieve a specific course, identified by its courseId and section records.
     * @param courseId - courseId record.
     * @param section - section record.
     * @return - Course if found; null if otherwise.
     */
    public Course readByCourseIdSection(String courseId, int section) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");
            Document queryDoc = new Document("courseId", courseId)
                    .append("section", section);
            Document courseMatchDoc = coursesCollection.find(queryDoc).first();

            if (courseMatchDoc == null) {
                logger.info("Course: " + courseId + ", Section: " + section + " not found.");
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();

            logger.info("Course: " + courseId + ", Section: " + section + " found.");
            return mapper.readValue(courseMatchDoc.toJson(), Course.class);

        } catch (JsonMappingException e) {
            //e.printStackTrace();
            //throw new DataSourceException("An exception occurred while mapping the document.", e);
//            System.out.println("\nSomething went wrong fetching that course.");
            logger.error(e.getMessage());
            return null;
        } catch (Exception e) {
//            e.printStackTrace(); // TODO log this to a file
//            throw new DataSourceException("An unexpected exception occurred.", e);
//            System.out.println("\nSomething went wrong fetching that course.");
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * Retrieve a list of Courses by their 'field' record.
     * @param field - filter.
     * @return - list of Courses if they exist; null if otherwise.
     */
    public List<Course> readByField(String field) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document queryDoc = new Document("field", field);
            Document sortDoc = new Document("courseId", 1).append("section", 1);
            FindIterable<Document> iterDoc = coursesCollection.find(queryDoc).sort(sortDoc);

            MongoCursor<Document> cursor = iterDoc.iterator();
            List<Course> courseList = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();

            if(!cursor.hasNext()) {
                logger.info("No courses found for field: '" + field + "'.");
                return null;
            }

            while(cursor.hasNext()) {
                Document doc = cursor.next();
                Course course = mapper.readValue(doc.toJson(), Course.class);
                courseList.add(course);
            }

            logger.info("Courses successfully retrieved for field: '" + field + "'.");
            return courseList;
        } catch(Exception e) {
//            System.out.println("\nSomething went wrong while fetching course list.");
            logger.error(e.getMessage());
            return null;
        }

    }

    /**
     * Retrieve a list of Courses from db using a list of CourseHeaders.
     * @param courseHeaders
     * @return - list if exists; null if otherwise.
     */
    public List<Course> readByCourseHeaders(List<CourseHeader> courseHeaders) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            ObjectMapper mapper = new ObjectMapper();
            List<Course> courseList = new ArrayList<>();

            courseHeaders.forEach(courseHeader -> {
                Document queryDoc = new Document("courseId", courseHeader.getCourseId())
                        .append("section", courseHeader.getSection());
                Course course = null;
                try {
                    course = mapper.readValue(coursesCollection.find(queryDoc).first().toJson(), Course.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                courseList.add(course);
            });
            logger.info("Course list successfully retrieved.");
            return courseList;
        } catch(Exception e) {
//            System.out.println("\nSomething went wrong while fetching course list.");
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * Retrieve all Courses in db.
     * @return - list of all Courses.
     */
    public List<Course> readAll() {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document sortDoc = new Document("courseId", 1).append("section", 1);
            FindIterable<Document> iterDoc = coursesCollection.find().sort(sortDoc);
            MongoCursor<Document> cursor = iterDoc.iterator();
            List<Course> courseList = new ArrayList<Course>();
            ObjectMapper mapper = new ObjectMapper();

            while(cursor.hasNext()) {
                Document doc = cursor.next();
                Course course = mapper.readValue(doc.toJson(), Course.class);
                courseList.add(course);
            }

            logger.info("Course list successfully retrieved.");
            return courseList;
        } catch(Exception e) {
//            System.out.println("\nSomething went wrong while fetching course list.");
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(Course updatedCourse) {
        return false;
    }

    /**
     * Updates a single Course in the db with provided changes.
     * @param course - updated Course
     * @return - true if update was successful; false if otherwise
     */
    public boolean updateCourse(Course course) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document queryDoc = new Document("courseId", course.getCourseId())
                    .append("section", course.getSection());
            Document replaceDoc = new Document("courseId", course.getCourseId())
                    .append("courseName", course.getCourseName())
                    .append("section", course.getSection())
                    .append("field", course.getField())
                    .append("level", course.getLevel())
                    .append("space", course.getSpace())
                    .append("capacity", course.getCapacity());

            coursesCollection.replaceOne(queryDoc, replaceDoc);

            logger.info("Course: " + course.getCourseId() + ", Section: " + course.getSection() + " successfully updated.");
            return true;
        } catch(Exception e) {
//            e.printStackTrace();
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * Increments Course record: 'space'. Used for incrementing (or decrementing) course space available when student
     * adds (or drops) a course.
     * @param courseHeader - Contains unique course information.
     * @param changeSpaceValue - Increment value.
     * @return - true if operation was successful; false if otherwise.
     */
    public boolean updateCourseSpace(CourseHeader courseHeader, int changeSpaceValue) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            coursesCollection.findOneAndUpdate(new Document("courseId", courseHeader.getCourseId())
                            .append("section", courseHeader.getSection()),
                    new Document().append(
                            "$inc",
                            new Document("space", changeSpaceValue)
                    ));

            logger.info("Course:" + courseHeader.getCourseId() + ", Section: " + courseHeader.getSection() + " incremented by - " + changeSpaceValue);
            return true;
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    /**
     * Uses CourseHeader, containing unique identifier information, to delete a specific course.
     * @param courseHeader - contains unique identifier information for target Course.
     * @return - true if operation was successful; false if otherwise.
     */
    public boolean deleteCourseByCourseHeader(CourseHeader courseHeader) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document deleteDoc = new Document("courseId", courseHeader.getCourseId())
                    .append("section", courseHeader.getSection());
            coursesCollection.deleteOne(deleteDoc);

            logger.info("Course: " + courseHeader.getCourseId() + ", Section: " + courseHeader.getSection() + " successfully deleted.");
            return true;
        } catch(Exception e) {
//            System.out.println("\nFailed to remove course.");
            logger.error(e.getMessage());
            return false;
        }
    }
}