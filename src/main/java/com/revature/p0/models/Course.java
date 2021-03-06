package com.revature.p0.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String courseId;
    private String courseName;
    private int section;
    private String field;
    private int level;
    private int space;
    private int capacity;

    public Course() { super(); }

    public Course(String courseID, String courseName, int section, String field, int level, int space, int capacity) {
        this.courseId = courseID;
        this.courseName = courseName;
        this.section = section;
        this.field = field;
        this.level = level;
        this.space = space;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Course {\n" +
                "\tcourseId='" + courseId + "', courseName='" + courseName + "',\n" +
                "\tsection=" + section + "', field='" + field + "', level=" + level + ",\n" +
                "\tspace=" + space + ", capacity=" + capacity + "\n" +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course aClass = (Course) o;
        return level == aClass.level && space == aClass.space && capacity == aClass.capacity && Objects.equals(courseId, aClass.courseId) && Objects.equals(courseName, aClass.courseName) && Objects.equals(field, aClass.field);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, courseName, field, level, space, capacity);
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
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
