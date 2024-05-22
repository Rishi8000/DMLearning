package com.example.myapplication;

public class Courses {

    // variables for storing our data.
    private String coursename, description, courseimg;

    public Courses() {
        // empty constructor
        // required for Firebase.
    }

    // Constructor for all variables.
    public Courses(String coursename, String description, String courseimg) {
        this.coursename = coursename;
        this.description = description;
        this.courseimg = courseimg;
    }

    // getter methods for all variables.
    public String getCourseName() {
        return coursename;
    }

    public void setCourseName(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseDescription() {
        return description;
    }

    // setter method for all variables.
    public void setCourseDescription(String description) {
        this.description = description;
    }

    public String getCourseImg() {
        return courseimg;
    }

    public void setCourseImg(String courseimg) {
        this.courseimg = courseimg;
    }
}
