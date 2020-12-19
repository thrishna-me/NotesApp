package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentUser != null) {
                    Intent homeIntent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
                else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, 1500);


    }
}