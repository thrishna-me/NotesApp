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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "login";
    TextView tvSignup;
    EditText etEmail, etPassword;
    Button loginButton;
    ValidateInput validateInput;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        validateInput = new ValidateInput(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.loginButton:
                handleLogin();
                break;
            case R.id.tvSignup:
                handleSignUp();
                break;
        }
    }

    private void handleSignUp() {
        Toast.makeText(this, "Signed Up", Toast.LENGTH_SHORT).show();
        Intent signupActivity = new Intent(MainActivity.this, SignUp.class);
        startActivity(signupActivity);
    }

    private void handleLogin() {
        showProgressBar();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (validateInput.CheckIfEmailIsValid(email) && validateInput.CheckIfPasswordIsStrong(password)) {
            Toast.makeText(this, "Valid inputs", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid Inputs", Toast.LENGTH_SHORT).show();


        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            hideProgressBar();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            //show another screen as login activity
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            hideProgressBar();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Some error occured" + task.getException(),
                                    Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
}