package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // creating variables for our edit text
    private EditText courseNameEdt, courseImgEdt, courseDescriptionEdt;

    // creating variable for button
    private Button submitCourseBtn;

    // creating a strings for storing
    // our values from edittext fields.
    private String coursename, courseimg, description;

    // creating a variable
    // for firebasefirestore.
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting our instance
        // from Firebase Firestore.
        db = FirebaseFirestore.getInstance();

        // initializing our edittext and buttons
        courseNameEdt = findViewById(R.id.idEdtCourseName);
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription);
        courseImgEdt = findViewById(R.id.idEdtCourseImg);
        submitCourseBtn = findViewById(R.id.idBtnSubmitCourse);

        // adding on click listener for button
        submitCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting data from edittext fields.
                coursename = courseNameEdt.getText().toString();
                description = courseDescriptionEdt.getText().toString();
                courseimg = courseImgEdt.getText().toString();

                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(coursename)) {
                    courseNameEdt.setError("Please enter Course Name");
                } else if (TextUtils.isEmpty(description)) {
                    courseDescriptionEdt.setError("Please enter Course Description");
                } else if (TextUtils.isEmpty(courseimg)) {
                    courseImgEdt.setError("Please enter Course Duration");
                } else {
                    // calling method to add data to Firebase Firestore.
                    addDataToFirestore(coursename, description, courseimg);
                }
            }
        });
    }

    private void addDataToFirestore(String coursename, String description, String courseimg) {

        // creating a collection reference
        // for our Firebase Firestore database.
        CollectionReference dbCourses = db.collection("courses");

        // adding our data to our courses object class.
        Courses courses = new Courses(coursename, description, courseimg);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(courses).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(MainActivity.this, "Your Course has been added to Firebase Firestore", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(MainActivity.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
