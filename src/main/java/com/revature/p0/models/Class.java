package com.revature.p0.models;

import java.util.Objects;

public class Class {
    private String classId;
    private String className;
    private String field;
    private int level;
    private int space;
    private int capacity;

    public Class(String classId, String className, String field, int level, int space, int capacity) {
        this.classId = classId;
        this.className = className;
        this.field = field;
        this.level = level;
        this.space = space;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Class{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                ", field='" + field + '\'' +
                ", level=" + level +
                ", space=" + space +
                ", capacity=" + capacity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class aClass = (Class) o;
        return level == aClass.level && space == aClass.space && capacity == aClass.capacity && Objects.equals(classId, aClass.classId) && Objects.equals(className, aClass.className) && Objects.equals(field, aClass.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classId, className, field, level, space, capacity);
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
