package com.sofe4640.noteme.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
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


        FloatingActionButton addNote = findViewById(R.id.new_note_btn);
        addNote.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent i = new Intent(getApplicationContext(), NewNoteActivity.class);
            startActivity(i);

        });

        SearchView search = (SearchView) findViewById(R.id.search_box);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                displayNotes(newText);
                return false;
            }
        });

        db = new DBHandler(this);

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        displayNotes("");
    }

    private void displayView() {
        noteAdapter = new RVAdapter(noteList);
        notesRecyclerView.setAdapter(noteAdapter);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
    }

    private void displayNotes(String query) {
        populateNoteList(query);

        displayView();
    }

    private void populateNoteList(String query) {
        Cursor cursor = null;
        if(query.equals("")){
            cursor = db.getNotes();
        } else {
            cursor = db.searchNote(query);
        }

        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Note note = new Note();
                note.title = cursor.getString(1);
                note.subtitle = cursor.getString(2);
                note.body = cursor.getString(3);
                note.noteColor = cursor.getString(4);

                noteList.add(note);
            }
        }
    }
}