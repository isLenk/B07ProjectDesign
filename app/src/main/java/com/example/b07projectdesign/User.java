package com.example.b07projectdesign;


import android.util.Log;

import com.google.firebase.database.Exclude;

// We shouldn't generate these.
public class User {
    private String email;

    @Exclude
    private String password;
    private boolean isAdmin;

    public User() {}

    public User(String email, String password) {
        this.email = email;
        setPassword(password);
    }

    public User(String email, String password, boolean isAdmin) {
        this(email, password);
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setPassword(String password) {
        Log.d("USER", "I WAS CALLED WITH " + password);
        this.password = DatabaseHandler.hashString(password);
    }

    public void setIsAdmin(boolean val) {
        this.isAdmin = val;
    }

}
