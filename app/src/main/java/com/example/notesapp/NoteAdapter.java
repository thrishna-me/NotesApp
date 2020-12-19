package com.example.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private final ArrayList<Note> noteList;
    private final Context context;

    public NoteAdapter(ArrayList<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(noteView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {

        final Note note = noteList.get(position);

        // set the data here and position starts from zero
        holder.noteTitle.setText(noteList.get(position).getNoteTitle());
        holder.noteDescription.setText(noteList.get(position).getNoteDescription());
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // we will open edit note activity here
                Intent editIntent = new Intent(context, EditNote.class);
                editIntent.putExtra("noteTitle", note.getNoteTitle());
                editIntent.putExtra("noteDescription", note.getNoteDescription());
                editIntent.putExtra("noteID", note.getNoteId());
                context.startActivity(editIntent);
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog(note.getNoteId());
            }
        });
    }

    private void showDeleteDialog(final String noteID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete the note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //delete the note
                        ((HomeActivity)context).deleteNoteInFirebase(noteID);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return noteList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView noteTitle, noteDescription, tvEdit, tvDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.tvNoteTitle);
            noteDescription = itemView.findViewById(R.id.tvNoteDescription);
            tvEdit = itemView.findViewById(R.id.tvEdit);
            tvDelete = itemView.findViewById(R.id.tvDelete);

        }
    }
}
