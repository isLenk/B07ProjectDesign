package com.example.b07projectdesign;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.hash.Hashing;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class DatabaseHandler {
    private static final boolean USE_EMULATOR = false;
    //private static FirebaseFirestore db;
    private static FirebaseDatabase db;
    private static DatabaseReference mDatabase;

    private static ChildEventListener listener;
    private static ChildEventListener onCourseAdded;

    private static final String TAG = "DATABASE HANDLER";
    private static ArrayList<Course> courses;

    public List<Course> getCourses() { return courses; }

    public DatabaseReference getCoursesWithFieldEqualTo(String fieldName, String value) {
        return mDatabase.child("course").orderByChild(fieldName).equalTo(value).getRef();
    }

    /**
     * Returns the DatabaseReference pointing to the entire database
     * @return DatabaseReference
     */
    public DatabaseReference getReference() {
        return mDatabase;
    }

    /**
     * Converts plaintext string into hashed string
     * @param plain the input string
     * @return sha256 hashed input string
     */
    public static String hashString(String plain) {
        return Hashing.sha256().hashBytes(plain.getBytes(StandardCharsets.UTF_8)).toString();
    }

    // Query the database for any matching hash pass.
    // In case there are recurring passwords, check email as well
    public static void getUser(String email, String rawPassword, Listener callback) {
        String password = hashString(rawPassword);
        mDatabase.child("users").orderByChild("password").equalTo(password).getRef()
                .orderByChild("email").equalTo(email).get()
                .addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        callback.onSuccess(dataSnapshot.toString());
                    }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e.toString());
                    }
                });
    }

//    private static void attachListener(DatabaseReference ref) {
//        if (listener == null) {
//            listener = new ChildEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Log.d("CHANGE", dataSnapshot.toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    Log.w("warning", "loadPost:onCancelled",
//                            databaseError.toException());
//                }
//
//            };
//        }
//        ref.addValueEventListener(listener);
//    }

    public static void addCourse(Course course) {
        DatabaseReference id = mDatabase.child("courses").push(); // .getKey()
        id.setValue(course)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {}
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Failure: " + e.toString());
                }
            });
    }

    public static void addUser(User user) {
        mDatabase.child("users").push().setValue(user);
    }

    // Test
    private static void createSampleCourses() {
        // public Course(String code, String name, Map<String, boolean> sessions, List<String> prerequisites) {
        // Larger size requires Map.ofEntries( entry(k,v), ... )
        ArrayList<Course> courses = new ArrayList<>();

        courses.add(new Course(
                "CSCC24",
                "Principles of Programming Languages",
                Map.of(0, true, 1, true, 2, false),
                List.of("CSCB07", "CSCB09"))
        );

        courses.add(new Course(
                "CSCB07",
                "Software Design",
                Map.of(0, false, 1, true, 2, true),
                List.of("CSCA48"))
        );

        courses.add(new Course(
                "CSCB09",
                "Software Tools and Systems Programming",
                Map.of(0, true, 1, true, 2, false),
                List.of("CSCA48"))
        );

        courses.add(new Course(
                "CSCA48",
                "Introduction to Compute Science II",
                Map.of(0, true,1, true, 2, false),
                List.of("CSCA08"))
        );

        courses.add(new Course(
                "CSCA08",
                "Introduction to Compute Science I",
                Map.of(0, true, 1, false, 2, true),
                List.of())
        );

        courses.add(new Course(
                "CSCC63",
                "Computability and Computational Complexity",
                Map.of(0, true, 1, false, 2, true),
                List.of("CSCB63", "CSCB36"))
        );

        courses.add(new Course(
                "CSCB63",
                "Design and Analysis of Data Structures",
                Map.of(0, true, 1, true, 2, false),
                List.of("CSCB36"))
        );

        courses.add(new Course(
                "CSCB36",
                "Introduction to the Theory of Computation",
                Map.of(0, false, 1, true, 2, true),
                List.of("CSCA48", "CSCA67"))
        );

        courses.add(new Course( // Problem course as there exists MATA67
                "CSCA67",
                "Discrete Mathematics",
                Map.of(0, false, 1, true, 2, true),
                List.of())
        );

        for (Course course : courses) addCourse(course);

        // Test : Repopulate courses on clear
        courses.clear();

    }

    // Test
    private static void createSampleUsers() {
        ArrayList<User> users = new ArrayList<>();

        users.add(new Student(
                "nobeans@mail.utoronto.ca",
                "0beans",
                Map.of("MATA41", true, "CSCB36", true, "CSCA08", true)
        ));

        users.add(new User(
            "admin@mail.utoronto.ca",
            "imAProfessor",
            true
        ));

        for (User user : users) addUser(user);
    }

    private static void listenForNewCourses(DatabaseReference coursesRef) {
        if (onCourseAdded == null) onCourseAdded = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Added Course: " + snapshot.toString());

                // TODO: Safety Checks
                courses.add(snapshot.getValue(Course.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d(TAG, "Child Changed: " + snapshot.toString());
                // Log.d("PREVIOUS CHANGE", previousChildName);
                //Log.e(TAG, "what... somebody just changed the ID to a course");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Removed Course: " + snapshot.toString());
                // TODO: Safety Checks
                courses.remove(snapshot.getValue(Course.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Log.d(TAG, "Child Moved: " + snapshot.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Cancelled? " + error.toString());
            }
        };

        coursesRef.addChildEventListener(onCourseAdded);
    }

    public static void Initialise() {
        courses = new ArrayList<>();

        FirebaseDatabase database;
        if (USE_EMULATOR) {
            database = FirebaseDatabase.getInstance("https://utsc-b07-projcourses.firebaseio.com");
            database.useEmulator("10.0.2.2", 9000);
            Log.d(TAG, "Emulator Connected");
        } else {
            database = FirebaseDatabase.getInstance("https://utsc-b07-projcourses-default-rtdb.firebaseio.com");
        }
        mDatabase = database.getReference();

        // NOTE: Clear Database for Testing
        mDatabase.setValue(null);

        createSampleCourses();
        createSampleUsers();

        listenForNewCourses(mDatabase.child("courses").getRef());

        // Test : onDataChange
//        new java.util.Timer().schedule(
//                new java.util.TimerTask() {
//                    @Override
//                    public void run() {
//                        update();
//                    }
//                },
//                5000
//        );

//        attachListener( mDatabase.child("courses").getRef());
//        mDatabase.child("courses").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            Log.e("demo", "Error getting data", task.getException());
//                        }
//                        else {
//                            Log.i(TAG, "Attaching Listeners");
//                            for(DataSnapshot child:task.getResult().getChildren()) {
//                                attachListener(child.getRef());
//                                Log.i("->" + TAG, "Attached Listener");
//                            }
//                        }
//                    }
//                });

        // READ
//        db.collection("users")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("NAH", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("YES", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
    }

}
//
//sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = sharedPreferences.edit();
//        if (sharedPreferences.getBoolean(Constants.FIRST_RUN, true)) {
//        addInitialDataToFirebase();;
//        editor.putBoolean(Constants.FIRST_RUN, false).commit();
//        }
