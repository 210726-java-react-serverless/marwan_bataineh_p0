package com.revature.p0.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.revature.p0.models.User;
import com.revature.p0.util.MongoClientFactory;
import com.revature.p0.util.PasswordUtils;
import com.revature.p0.util.exceptions.DataSourceException;
import org.bson.Document;

/**
 * The MongodUserDAO provides the database connectivity layer for the application. Facilitates CRUD operations on the
 * users collection.
 */
public class MongodUserDAO implements CRUD<User>{

    @Override
    public User create(User newUser) {
        try {
            /* Encrypt newUser's plaintext password */
            String salt = PasswordUtils.getSalt(30);
            String encryptedPassword = PasswordUtils.generateSecurePassword(newUser.getPassword(), salt);

            MongoClient mongoClient = MongoClientFactory.getInstance()
                    .getConnection();


            MongoDatabase bookstoreDb = mongoClient.getDatabase("p0");
            MongoCollection<Document> usersCollection = bookstoreDb.getCollection("users");
            Document newUserDoc = new Document("firstName", newUser.getFirstName())
                    .append("lastName", newUser.getLastName())
                    .append("email", newUser.getEmail())
                    .append("username", newUser.getUsername())
                    .append("password", newUser.getPassword()); // TODO: enter encrypted password



            usersCollection.insertOne(newUserDoc);
            newUser.setId(newUserDoc.get("_id").toString());

            return newUser;

        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
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
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            return authUser;

        } catch (JsonMappingException e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An exception occurred while mapping the document.", e);
        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
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
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            if(!authUser.getPassword().equals(password)) {
                return null;
            }

            return authUser;

        } catch (JsonMappingException e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An exception occurred while mapping the document.", e);
        } catch (Exception e) {
            e.printStackTrace(); // TODO log this to a file
            throw new DataSourceException("An unexpected exception occurred.", e);
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
                return null;
            }

            ObjectMapper mapper = new ObjectMapper();
            User authUser = mapper.readValue(authUserDoc.toJson(), User.class);
            authUser.setId(authUserDoc.get("_id").toString());

            return authUser;
        } catch(JsonProcessingException e) {
            e.printStackTrace(); //TODO: log and provide handling
        } catch(Exception e) {
            e.printStackTrace(); //TODO: log and handle
        }

        return null;
    }

    @Override
    public boolean update(User updatedResource) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

}
