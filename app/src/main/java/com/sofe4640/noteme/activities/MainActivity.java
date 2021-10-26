package com.sofe4640.noteme.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sofe4640.noteme.adapters.RVAdapter;
import com.sofe4640.noteme.db.DBHandler;
import com.sofe4640.noteme.models.Note;
import com.sofe4640.noteme.R;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Note> noteList = new ArrayList<>();
    private DBHandler db;
    private RVAdapter noteAdapter;

    private RecyclerView notesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        db = new DBHandler(this);

        FloatingActionButton addNote = findViewById(R.id.new_note_btn);
        addNote.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent i = new Intent(getApplicationContext(), NewNoteActivity.class);
            startActivity(i);

        });

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );

        notesRecyclerView.setAdapter(noteAdapter);
        noteAdapter = new RVAdapter(noteList);

        db.populateNoteList(noteList);
    }



}