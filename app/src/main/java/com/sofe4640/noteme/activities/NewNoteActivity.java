package com.sofe4640.noteme.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sofe4640.noteme.db.DBHandler;
import com.sofe4640.noteme.R;

import java.util.Objects;

public class NewNoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteSubtitle, noteBody;
    DBHandler db;

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

        db  = new DBHandler(this);

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


    private boolean saveNote() {
        boolean status = false;
        if (noteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Your note needs a title!", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = db.saveNote(noteTitle.getText().toString(),
                    noteSubtitle.getText().toString(), noteBody.getText().toString(), "test");
            if (success) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                status = true;
            }

        }
        return status;
    }
}