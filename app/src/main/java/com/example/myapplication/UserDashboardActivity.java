package com.example.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserDashboardActivity extends AppCompatActivity {
    LinearLayout courseListContainer;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
//        ScrollView scrollView = findViewById(R.id.scroll_view);
        courseListContainer = findViewById(R.id.course_list_container);
//        scrollView.addView(courseListContainer);
        firestore = FirebaseFirestore.getInstance();

        // Fetch data from Firestore for the "courses" collection
        firestore.collection("courses")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            courseListContainer.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String courseName = document.getString("courseName");
                                String courseDescription = document.getString("courseDescription");
                                // Assuming "courseimg" is a URL
                                String courseImageUrl = document.getString("courseImg");

                                CardView cardView = new CardView(UserDashboardActivity.this);

                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0, 0, 0, 16);
                                cardView.setLayoutParams(layoutParams);
                                cardView.setCardElevation(4);

                                LinearLayout cardContent = new LinearLayout(UserDashboardActivity.this);
                                cardContent.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                ));
                                cardContent.setOrientation(LinearLayout.VERTICAL);
                                cardContent.setPadding(16, 16, 16, 16);

                                TextView titleTextView = new TextView(UserDashboardActivity.this);
                                titleTextView.setText(courseName);
                                titleTextView.setTextSize(20);
                                titleTextView.setTypeface(null, Typeface.BOLD);
                                // Use Picasso to load the image dynamically

                                ImageView imageView = new ImageView(UserDashboardActivity.this);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT, 480
                                ));
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                imageView.setContentDescription(getString(R.string.course_image));

                                // Load the image using Picasso
//                                Picasso.get().load(courseImageUrl)
//                                        .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
//                                        .error(R.drawable.error_image) // Error image if the load fails
//                                        .into(imageView);
//                              Picasso.get().load(courseImageUrl).into(imageView);
                                Glide.with(UserDashboardActivity.this).load(courseImageUrl).into(imageView);


                                // Set up an OnClickListener for each card to navigate to a detailed view of the course
                                cardView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Toast.makeText(UserDashboardActivity.this, "Clicked on " + courseName, Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(UserDashboardActivity.this, CourseDetailActivity.class);
                                        intent.putExtra("courseName", courseName);
                                        intent.putExtra("courseDescription", courseDescription);
                                        intent.putExtra("courseImageUrl", courseImageUrl);
                                        // Add more data as needed
                                        startActivity(intent);
                                    }
                                });

                                cardContent.addView(imageView);
                                cardContent.addView(titleTextView);
//                                cardContent.addView(descriptionTextView);

                                cardView.addView(cardContent);
                                courseListContainer.addView(cardView);
                            }
                        } else {
                            // Handle errors
                            Toast.makeText(UserDashboardActivity.this, "Somthing Error Ouccred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//
//                case R.id.nav_home:
//                    // Handle Home selection or do nothing if already in Home
//                    return true;
//                case R.id.nav_profile:
//                    // Navigate to Profile activity
//                    Intent profileIntent = new Intent(UserDashboardActivity.this, ProfileFragment.class);
//                    startActivity(profileIntent);
//                    finish(); // Optionally finish the current activity
//                    return true;
//                // Add more cases for additional items
//                default:
//                    return false;
//            }
//        });


    }
}
