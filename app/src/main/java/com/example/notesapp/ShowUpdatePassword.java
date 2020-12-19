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

public class ShowUpdatePassword extends AppCompatActivity {
    EditText etPassword, etPasswordAgain;
    Button updatePassword;
    ValidateInput validateInput;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_update_password);
        etPassword = findViewById(R.id.et_Password);
        etPasswordAgain = findViewById(R.id.et_PasswordAgain);
        updatePassword = findViewById(R.id.updateButton);
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePasswordInFirebase();
            }
        });
        validateInput = new ValidateInput(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void updatePasswordInFirebase() {
        final String password = etPassword.getText().toString();
        final String passwordAgain = etPasswordAgain.getText().toString();

        if (validateInput.CheckIfPasswordIsStrong(passwordAgain)) {
            if (password.equals(passwordAgain)) {
                // updating password in firebase
                firebaseUser.updatePassword(passwordAgain).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //password updated successfully
                            Toast.makeText(ShowUpdatePassword.this, "Password updated to : " + passwordAgain, Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ShowUpdatePassword.this, "Some error occured" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "Passwords didn't match", Toast.LENGTH_SHORT).show();
            }
        }
    }
}