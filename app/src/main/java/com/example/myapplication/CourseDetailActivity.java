package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class CourseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        // Retrieve data passed from UserDashboardActivity
        Intent intent = getIntent();
        String courseName = intent.getStringExtra("courseName");
        String courseDescription = intent.getStringExtra("courseDescription");
        String courseImageUrl = intent.getStringExtra("courseImageUrl");

        // Set up UI elements
        TextView titleView = findViewById(R.id.coursetitle);
        ImageView courseDetailImageView = findViewById(R.id.courseDetailImageView);
        TextView courseDetailNameTextView = findViewById(R.id.courseDetailNameTextView);
        TextView courseDetailDescriptionTextView = findViewById(R.id.courseDetailDescriptionTextView);

        // Load the course image using Picasso (or any other image loading library)
//        Picasso.get().load(courseImageUrl).placeholder(R.drawable.placeholder_image);

        // Set the course details in TextViews
        titleView.setText(courseName);
        courseDetailNameTextView.setText(courseName);
        courseDetailDescriptionTextView.setText(courseDescription);
        Glide.with(this).load(courseImageUrl).into(courseDetailImageView);

        // Add more UI elements and set their values as needed
    }
}

