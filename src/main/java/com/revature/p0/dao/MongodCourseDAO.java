package com.revature.p0.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.revature.p0.models.Course;
import com.revature.p0.models.CourseHeader;
import com.revature.p0.util.MongoClientFactory;
import com.revature.p0.util.exceptions.DataSourceException;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongodCourseDAO implements CRUD<Course>{

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
            return newCourse;
        } catch(Exception e) { //TODO log and something...
            return null;
        }
    }

    @Override
    public Course read(int id) {
        return null;
    }

    public Course readByCourseIdSection(String courseId, int section) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");
            Document queryDoc = new Document("courseId", courseId)
                    .append("section", section);
            Document courseMatchDoc = coursesCollection.find(queryDoc).first();

            if (courseMatchDoc == null) {
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(courseMatchDoc.toJson(), Course.class);

        } catch (JsonMappingException e) {
            //e.printStackTrace(); // TODO log this to a file
            //throw new DataSourceException("An exception occurred while mapping the document.", e);
            System.out.println("\nSomething went wrong fetching that course.");
            return null;
        } catch (Exception e) {
//            e.printStackTrace(); // TODO log this to a file
//            throw new DataSourceException("An unexpected exception occurred.", e);
            System.out.println("\nSomething went wrong fetching that course.");
            return null;
        }
    }

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

            while(cursor.hasNext()) {
                Document doc = cursor.next();
                Course course = mapper.readValue(doc.toJson(), Course.class);
                courseList.add(course);
            }
            return courseList;
        } catch(Exception e) {
            System.out.println("\nSomething went wrong while fetching course list."); //TODO handle thissss better.
            return null;
        }

    }

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
            return courseList;
        } catch(Exception e) {
            System.out.println("\nSomething went wrong while fetching course list."); //TODO handle thissss better.
            return null;
        }
    }

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
            return courseList;
        } catch(Exception e) {
            System.out.println("\nSomething went wrong while fetching course list."); //TODO handle thissss better.
            return null;
        }
    }

    @Override
    public boolean update(Course updatedCourse) {
        return false;
    }

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
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    public boolean deleteCourseByCourseHeader(CourseHeader courseHeader) {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            Document deleteDoc = new Document("courseId", courseHeader.getCourseId())
                    .append("section", courseHeader.getSection());
            coursesCollection.deleteOne(deleteDoc);
            return true;
        } catch(Exception e) {
            System.out.println("\nFailed to remove course.");
            return false;
        }
    }
}