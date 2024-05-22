package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFragment extends Fragment {

    private TextView textViewWelcome, textViewFullname, textViewEmail, textViewDob, textViewGender, textViewMobile;
    private String fullName, user_email, doB, Gen, moB;
    private static final String TAG = "ProfileFragment";
    private FirebaseAuth authProfile;
    private FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewWelcome = rootView.findViewById(R.id.textview_show_welcome);
        textViewFullname = rootView.findViewById(R.id.textview_show_fullname);
        textViewEmail = rootView.findViewById(R.id.textview_show_email);
        textViewDob = rootView.findViewById(R.id.textview_show_Dob);
        textViewGender = rootView.findViewById(R.id.textview_show_gender);
        textViewMobile = rootView.findViewById(R.id.textview_show_mobile);
//        progressBar = rootView.findViewById(R.id.progressbar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        Button buttonLogout = rootView.findViewById(R.id.btn_logout);

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "Something went Wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            readData(firebaseUser.getEmail());
        }



        buttonLogout.setOnClickListener(v -> logOut());

        return rootView;
    }

    private void readData(String userEmail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersCollection = db.collection("users");

        usersCollection.whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);

                            fullName = documentSnapshot.getString("fullname");
                            user_email = documentSnapshot.getString("email");
                            doB = documentSnapshot.getString("dob");
                            Gen = documentSnapshot.getString("gender");
                            moB = documentSnapshot.getString("mobile");

                            updateView(fullName, user_email, doB, Gen, moB);
                        } else {
                            Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });





    }

    private void updateView(String fullName, String userEmail, String doB, String gen, String moB) {
        textViewWelcome.setText("Welcome!");
        textViewFullname.setText(fullName);
        textViewEmail.setText(userEmail);
        textViewDob.setText(doB);
        textViewGender.setText(gen);
        textViewMobile.setText(moB);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}