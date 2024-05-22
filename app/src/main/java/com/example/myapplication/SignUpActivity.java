package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    // Declare any other necessary variables.
    private FirebaseAuth auth;
    private EditText signupName, signupDoB, signupMobile, signupEmail, signupPassword, signupConfirmPwd;
    private Button signupButton;
    private RadioGroup gender;
    private TextView loginRedirectText;
    private RadioButton radioButtonRegisterGenderSelected;
    private  DatePickerDialog picker;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Initialize the FirebaseAuth instance in the onCreate()
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        signupName = findViewById(R.id.fullname);
        signupDoB = findViewById(R.id.edittext_register_dob);
        signupMobile = findViewById(R.id.edittext_register_mobile);
        signupConfirmPwd = findViewById(R.id.edittext_register_confirem_password);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        gender = findViewById(R.id.radiogroup_register_gender);
        gender.clearCheck();

        signupDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                //Date picker Dialog
                picker = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        signupDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedGenderId=gender.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected=findViewById(selectedGenderId);

                String textfullname;
                textfullname= signupName.getText().toString().trim();
                String textdob;
                textdob= signupDoB.getText().toString().trim();
                String textMobile;
                textMobile= signupMobile.getText().toString().trim();
                String textConfirmPwd;
                textConfirmPwd= signupConfirmPwd.getText().toString().trim();
                String textGender;
                String user;
                user = signupEmail.getText().toString().trim();
                String pass;
                pass = signupPassword.getText().toString().trim();


                String mobileRegx = "[6-9][0-9]{9}";     // First no. can be {6,8,9} and rest 9 nos. can be any no.
                Matcher mobileMatcher;
                Pattern mobilePattern =Pattern.compile(mobileRegx);
                mobileMatcher =mobilePattern.matcher(textMobile);

                //if-else ststement
                if (TextUtils.isEmpty(textfullname)){
                    Toast.makeText(SignUpActivity.this,"Please enter your full name",Toast.LENGTH_LONG).show();
                    signupName.setError("Full name is required");
                    signupName.requestFocus();
                } else if (TextUtils.isEmpty(user)) {
                    Toast.makeText(SignUpActivity.this,"Please enter your Email",Toast.LENGTH_LONG).show();
                    signupEmail.setError("Email is required");
                    signupEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
                    Toast.makeText(SignUpActivity.this,"Please re-enter your Email",Toast.LENGTH_LONG).show();
                    signupEmail.setError("Valid Email is required");
                    signupEmail.requestFocus();
                } else if (TextUtils.isEmpty(textdob)) {
                    Toast.makeText(SignUpActivity.this,"Please enter your date of birth",Toast.LENGTH_LONG).show();
                    signupDoB.setError("date of birth is required");
                    signupDoB.requestFocus();
                } else if (gender.getCheckedRadioButtonId() == -1){
                    Toast.makeText(SignUpActivity.this,"Please select your gender",Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required");
                    radioButtonRegisterGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(SignUpActivity.this,"Please enter your mobile no.",Toast.LENGTH_LONG).show();
                    signupMobile.setError("Mobile no. is required");
                    signupMobile.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText(SignUpActivity.this,"Please re-enter your mobile no.",Toast.LENGTH_LONG).show();
                    signupMobile.setError("mobile no. should be 10 digits");
                    signupMobile.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(SignUpActivity.this,"Please re-enter your mobile no.",Toast.LENGTH_LONG).show();
                    signupMobile.setError("mobile no. is not valid");
                    signupMobile.requestFocus();
                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(SignUpActivity.this,"Please enter your password",Toast.LENGTH_LONG).show();
                    signupPassword.setError("Password is required");
                    signupPassword.requestFocus();
                } else if (pass.length() < 6) {
                    Toast.makeText(SignUpActivity.this,"Password should be at least 6 digits",Toast.LENGTH_LONG).show();
                    signupPassword.setError("Password is too weak");
                    signupPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(SignUpActivity.this,"Please confirm your password",Toast.LENGTH_LONG).show();
                    signupConfirmPwd.setError("Password confirmation is required");
                    signupConfirmPwd.requestFocus();
                } else if (!pass.equals(textConfirmPwd)) {
                    Toast.makeText(SignUpActivity.this,"Please Enter same password",Toast.LENGTH_LONG).show();
                    signupConfirmPwd.setError("Password confirmation is required");
                    signupConfirmPwd.requestFocus();
                    //cleared the entered password
                    signupPassword.clearComposingText();
                    signupConfirmPwd.clearComposingText();
                } else{
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    System.out.println(textGender);
                    auth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){


                                addDataToFirestore(textfullname, user, textdob, textGender, textMobile);

//                                Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            } else{
                                Toast.makeText(SignUpActivity.this, "Signup Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
    private void addDataToFirestore(String fullname, String email, String dob,String gender, String mobile) {

        // creating a collection reference
        // for our Firebase Firestore database.
        CollectionReference dbCourses = db.collection("users");

        // adding our data to our courses object class.
        ReadWriteuserDetails details = new ReadWriteuserDetails(fullname, email, dob, gender, mobile);

        // below method is use to add data to Firebase Firestore.
        dbCourses.add(details).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                Toast.makeText(SignUpActivity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                Toast.makeText(SignUpActivity.this, "Fail to add course \n" + e, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
