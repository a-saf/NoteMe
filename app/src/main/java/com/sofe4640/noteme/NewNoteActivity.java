package com.sofe4640.noteme;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteSubtitle, noteBody;
    NoteDB db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        //Create AppBar and set Back button
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        db  = new NoteDB(getApplicationContext());

        noteTitle = findViewById(R.id.note_title);
        noteSubtitle = findViewById(R.id.note_subtitle);
        noteBody = findViewById(R.id.note_body);

        //Save button navigate back to home screen
        FloatingActionButton saveButton = findViewById(R.id.new_note_btn);
        saveButton.setOnClickListener(view -> {
            boolean saved = saveNote();
            if (saved) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean saveNote() {
        boolean status = false;
        if (noteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Your note needs a title!", Toast.LENGTH_SHORT).show();
        } else if (noteBody.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "What's in your note?", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = db.saveNote(noteTitle.getText().toString(),
                    noteSubtitle.getText().toString(), noteBody.getText().toString());
            if (success) Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
            status = true;
        }
        return status;
    }
}