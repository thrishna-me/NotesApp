package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "Signup";
    EditText et_Email, et_Password, et_PasswordAgain, etName;
    TextView tvLogin;
    Button signupButton;
    private String email, password, passwordAgain, name;
    ValidateInput validateInput;
    private FirebaseAuth mAuth;
    ProgressBar progressSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        etName = findViewById(R.id.etName);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_PasswordAgain = findViewById(R.id.et_PasswordAgain);
        tvLogin = findViewById(R.id.tvLogin);
        signupButton = findViewById(R.id.signupbutton);
        progressSignup = findViewById(R.id.progressSignup);

        validateInput = new ValidateInput(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUpBtn();
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginText();
            }
        });
    }

    private void handleLoginText() {
        Intent intent = new Intent(SignUp.this, MainActivity.class);
        startActivity(intent);
    }

    private void hideProgressBar() {
        progressSignup.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressSignup.setVisibility(View.VISIBLE);
    }

    private void handleSignUpBtn() {
        showProgressBar();
        email = et_Email.getText().toString();
        password = et_Password.getText().toString();
        passwordAgain = et_PasswordAgain.getText().toString();
        name = etName.getText().toString();

        if (!name.isEmpty()) {
            if (validateInput.CheckIfEmailIsValid(email) && validateInput.CheckIfPasswordIsStrong(password)) {
                if (password.equals(passwordAgain)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        hideProgressBar();
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        //user is used to show user info and give current user
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(SignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                        
                                        saveNameInFirebaseRealtimeDatabase(user);
                                    } else {
                                        Toast.makeText(SignUp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                } else {
                    hideProgressBar();
                    Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            hideProgressBar();
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveNameInFirebaseRealtimeDatabase(FirebaseUser user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //reference to the root
        DatabaseReference rootReference = firebaseDatabase.getReference();
        //reference to the name
        DatabaseReference nameReference = rootReference.child("Users").child(user.getUid()).child("name");

        nameReference.setValue(name);
        


    }
}