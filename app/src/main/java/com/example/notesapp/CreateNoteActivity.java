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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNoteActivity extends AppCompatActivity {
    EditText etNoteTitle, etNoteDescription;
    Button createNote;
    CreateNoteActivity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteDescription = findViewById(R.id.etNoteDescription);
        createNote = findViewById(R.id.createNote);

        createNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putNoteInFirebase();
            }
        });

        context = CreateNoteActivity.this;
    }

    private void putNoteInFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference();

        DatabaseReference notesReference = rootReference.child("Users").child(user.getUid()).child("Notes");  //root reference

        DatabaseReference newNoteReference = notesReference.push();         //root/users/current user//notes//some random id generated by firebase

        Note newNote = new Note(etNoteTitle.getText().toString(), etNoteDescription.getText().toString());
        newNoteReference.setValue(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateNoteActivity.this, "Note created in Firebase", Toast.LENGTH_SHORT).show();
                    context.finish();
                }
                else {
                    Toast.makeText(CreateNoteActivity.this, "Soome error occured"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}