package com.revature.p0.models;

import java.util.Objects;

public class CourseHeader {
    private String courseId;
    private int section;

    public CourseHeader() { super(); }

    public CourseHeader(String courseId, int section) {
        this.courseId = courseId;
        this.section = section;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "CourseHeader{" +
                "courseId='" + courseId + '\'' +
                ", section=" + section +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseHeader that = (CourseHeader) o;
        return section == that.section && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseId, section);
    }
}
