package com.example.b07projectdesign;

import com.google.firebase.database.DataSnapshot;

// Source: stackoverflow.com user: Tirupati Singh
// Callback Interface
public interface Listener {
    void onSuccess(String data);
    void onFailure(String data);
    void onComplete(String data);
}
