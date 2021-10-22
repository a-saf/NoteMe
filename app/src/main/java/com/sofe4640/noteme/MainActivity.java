package com.sofe4640.noteme;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

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
            Intent i = new Intent(getApplicationContext(),NewNoteActivity.class);
            startActivity(i);

        });
    }



}