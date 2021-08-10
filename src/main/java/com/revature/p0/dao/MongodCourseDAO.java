package com.revature.p0.dao;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.revature.p0.models.Course;
import com.revature.p0.util.MongoClientFactory;
import com.revature.p0.util.exceptions.DataSourceException;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MongodCourseDAO implements CRUD<Course>{

    @Override
    public Course create(Course newClass) {
        return null;
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

    public List<Course> readAll() {
        try {
            MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
            MongoDatabase p0Database = mongoClient.getDatabase("p0");
            MongoCollection<Document> coursesCollection = p0Database.getCollection("courses");

            FindIterable<Document> iterDoc = coursesCollection.find();
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
    public boolean update(Course updatedClass) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}