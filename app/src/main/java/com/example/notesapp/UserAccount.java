package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccount extends AppCompatActivity implements View.OnClickListener {
    TextView tvHello;
    Button logoutButton, updatePassword, updateEmail;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        tvHello = findViewById(R.id.tvHello);
        logoutButton = findViewById(R.id.logoutButton);

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        logoutButton.setOnClickListener(this);
        context = this;
        updatePassword = findViewById(R.id.updatePassword);
        updatePassword.setOnClickListener(this);

        updateEmail = findViewById(R.id.updateEmail);
        updateEmail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.logoutButton:
                //logout method implemented
                logOutBtn();
                //firebaseAuth.signOut(); //end the screen
                //this.finish(); // finish the current activity
                break;
            case R.id.updatePassword:
                showUpdatePasswordActivity();
                break;
            case R.id.updateEmail:
                showUpdateEmailActivity();
                break;
        }
    }

    private void showUpdateEmailActivity() {
        Intent intent = new Intent( UserAccount.this , UpdateEmail.class);
        startActivity(intent);
    }

    private void showUpdatePasswordActivity() {
        Intent intent = new Intent(UserAccount.this, ShowUpdatePassword.class);
        startActivity(intent);
    }

    private void logOutBtn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firebaseAuth.signOut();
                        ((UserAccount)context).finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}