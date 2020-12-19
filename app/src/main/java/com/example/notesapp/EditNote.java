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

public class EditNote extends AppCompatActivity {
    EditText etEditNoteTitle, etEditNoteDescription;
    Button etEditNote;

    String editNoteTitle, editNoteDescription, noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        etEditNoteTitle = findViewById(R.id.editNoteTitle);
        etEditNoteDescription = findViewById(R.id.editNoteDescription);

        if (getIntent().getExtras() != null) {
            editNoteTitle = getIntent().getStringExtra("noteTitle");
            editNoteDescription = getIntent().getStringExtra("noteDescription");
            noteId = getIntent().getStringExtra("noteId");

            etEditNoteTitle.setText(editNoteTitle);
            etEditNoteDescription.setText(editNoteDescription);
        }

        etEditNote = findViewById(R.id.editNote);
        etEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNoteInFirebase();
            }
        });
    }

    private void editNoteInFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

        DatabaseReference notesReference = reference.child("Users").child(user.getUid()).child("Notes");

        DatabaseReference particularNoteReference = notesReference.child(noteId);
        particularNoteReference.child("noteTitle").setValue(etEditNoteTitle.getText().toString());
        particularNoteReference.child("noteDescription").setValue(etEditNoteDescription.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditNote.this, "Note updated successfully in Firebase", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditNote.this, "Some error occurred " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}