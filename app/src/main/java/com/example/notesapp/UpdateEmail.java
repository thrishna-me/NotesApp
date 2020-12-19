package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {
    EditText etEmail, etEmailAgain;
    Button updateEmail;
    ValidateInput validateInput;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        etEmail = findViewById(R.id.etEmail);
        etEmailAgain = findViewById(R.id.etEmailAgain);
        updateEmail = findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmailInFirebase();
            }
        });
        validateInput = new ValidateInput(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void updateEmailInFirebase() {
        String email = etEmail.getText().toString();
        String emailAgain = etEmailAgain.getText().toString();

        if (validateInput.CheckIfEmailIsValid(emailAgain)) {
            if (email.equals(emailAgain)) {
                Toast.makeText(this, "Email can't be same as te old one.", Toast.LENGTH_SHORT).show();
            }
            else {
                firebaseUser.updateEmail(emailAgain).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateEmail.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(UpdateEmail.this, "Some error occured" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}