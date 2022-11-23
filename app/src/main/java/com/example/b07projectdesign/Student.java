package com.example.b07projectdesign;

import java.util.HashMap;
import java.util.Map;

public class Student extends User {
    private Map<String, Boolean> coursesTaken;

    public Student() {coursesTaken = new HashMap<>();
    }

    public Student(String email, String password, Map<String, Boolean> coursesTaken) {
        super(email, password);
        this.coursesTaken = coursesTaken;

    }

    public Map<String, Boolean> getCoursesTaken() {
        return coursesTaken;
    }



}
