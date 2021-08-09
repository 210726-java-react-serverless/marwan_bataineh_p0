package com.revature.p0.dao;

public interface CRUD<T> {

    T create(T newResource);
    T readByID(int id);
    T readByUsername(String username);
    T readByUsernamePassword(String username, String password);
    T readByEmail(String email);
    boolean update(T updatedResource);
    boolean delete(int id);

}
