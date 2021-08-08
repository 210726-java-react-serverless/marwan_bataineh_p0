package com.revature.p0.dao;

public interface CRUD<T> {

    T create(T newResource);
    T read(int id);
    boolean update(T updatedResource);
    boolean delete(int id);

}
