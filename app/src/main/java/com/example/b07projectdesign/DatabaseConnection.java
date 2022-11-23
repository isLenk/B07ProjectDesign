package com.example.b07projectdesign;

import java.util.List;

public interface DatabaseConnection {
    public void getUser(String email, String password, Listener callback);

    public List<Course> getCourses();
    public List<Course> getCoursesWhereCodeHas(String code);

    public boolean updateCourse();

}
