package com.example.notesapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

class ValidateInput {
    Context context;

    public ValidateInput(Context context) {
        this.context = context;
    }

    boolean CheckIfEmailIsValid(String email) {
        if(email.length() == 0)
        {
            Toast.makeText(context, "Please enter email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        else return true;
    }

    boolean CheckIfPasswordIsStrong(String password) {
        if(password.length() == 0)
        {
            Toast.makeText(context, "Please enter a password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.length() <= 8)
        {
            Toast.makeText(context, "Please enter a strong password", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

}
