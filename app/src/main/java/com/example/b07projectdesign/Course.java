package com.example.b07projectdesign;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course  { // implements Serializable
    @Exclude
    private static Map<String, Course> courses;

    private String code;
    private String name;
    private Map<Integer, Boolean> sessionOfferings;
    private List<String> prerequisites;

    // Attach a listener
    public Course() {
        if (courses == null) courses = new HashMap<>();
    }

    public Course(String code, String name, Map<Integer, Boolean> sessions, List<String> prerequisites) {
        this();
        this.code = code;
        this.name = name;
        this.sessionOfferings = sessions;
        this.prerequisites = prerequisites;
    }

    public String getCode() { return code; }

    public String getName() { return name; }

    public void setCode(String code) {
        this.code = code;
        courses.put(code, this);
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }

//    public List<Course> getPrerequisites() {
//        List<Course> result = new ArrayList<>();
//        if (prerequisites == null) return List.of();
//        for (String course : prerequisites.keySet()) {
//            Course c = courses.get(course);
//            if (c != null) result.add(c);
//        }
//        return result;
//    }

    //        Map<String, Boolean> result = new HashMap<>();
//        if (prerequisites == null) return Map.of();
//        for (String course : prerequisites.keySet()) {
//            Course c = courses.get(course);
//            if (c != null) result.put(c.code, true);
//        }
//        return result;
   public Map<String, String> getPrerequisites() {
        Map<String, String> result = new HashMap<>();
        for (int i = 0; i < prerequisites.size(); i++)
            result.put(Integer.toString(i), prerequisites.get(i));
        return result;
    }

    public Map<String, Boolean> getSessionOfferings() {
        Map<String, Boolean> result = new HashMap<>();
        for (Integer c : sessionOfferings.keySet())
            result.put(c.toString(), sessionOfferings.get(c));
        return result;
    }

    public void setSessionOfferings(ArrayList<Boolean> session) {// Map<Integer, Boolean> session) {
        Map<Integer, Boolean> result = new HashMap<>();
        for (int i = 0 ; i < session.size(); i++)
            result.put(i, session.get(i));
        sessionOfferings = result;
    }

    @NonNull
    public String toString() {
        return code + ": " + name;
    }
}
