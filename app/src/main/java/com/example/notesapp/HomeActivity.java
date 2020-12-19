package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button createNoteBtn;
    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    Context context;
    ArrayList<Note> noteArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvWelcome = findViewById(R.id.tvWelcome);
        createNoteBtn = findViewById(R.id.createNoteBtn);
        recyclerView = findViewById(R.id.recyclerView);
        context = this;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(); // root node
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference nameReference = reference.child("Users").child(user.getUid()).child("name");

        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvWelcome.setText("Hello, " + snapshot.getValue().toString() + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNoteActivity();
            }
        });

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // set the adapter


        readNotesFromFirebase();
    }

    private void readNotesFromFirebase() {
        // read notes in firebase database
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid()).child("Notes");
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noteArrayList.clear();
                Note note;
                // this data snapshot contain {noteID1 : {noteTitle : "Value"  , noteDescription : "Value"} , noteID2 : {noteTitle : "Value" , noteDescription : "Value"}}
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    note = noteSnapshot.getValue(Note.class); // the title in firebase will link to the title in note class

                    note.setNoteId(noteSnapshot.getKey());

                    Toast.makeText(HomeActivity.this, "Note : Title : " + note.getNoteTitle() + " Description : " + note.getNoteDescription() + "noteID : " + note.getNoteId(), Toast.LENGTH_SHORT).show();
                    Log.i("MyNote", "Note : Title : " + note.getNoteTitle() + " Description : " + note.getNoteDescription());

                    // add note to the arrayList of Notes
                    noteArrayList.add(note);
                }
                // setup layout
                noteAdapter = new NoteAdapter(noteArrayList, context);
                recyclerView.setAdapter(noteAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openCreateNoteActivity() {
        Intent createNoteIntent = new Intent(HomeActivity.this, CreateNoteActivity.class);
        startActivity(createNoteIntent);
    }

    public void deleteNoteInFirebase(String noteID) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(firebaseUser.getUid()).child("Notes");
        DatabaseReference particularNoteReference = notesReference.child(noteID);

        //delete the node in firebase
        particularNoteReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Note is deleted successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "Some error occurred ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manageAccount:
                Intent manageAccountIntent = new Intent(HomeActivity.this, UserAccount.class);
                startActivity(manageAccountIntent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                Intent loginIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(loginIntent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }
}