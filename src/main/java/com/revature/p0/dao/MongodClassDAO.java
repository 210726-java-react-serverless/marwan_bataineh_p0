package com.revature.p0.dao;

import com.revature.p0.models.Course;

public class MongodClassDAO implements CRUD<Course>{

    @Override
    public Course create(Course newClass) {
        return null;
    }

    @Override
    public Course read(int id) {
        return null;
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