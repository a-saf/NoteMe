package com.sofe4640.noteme.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sofe4640.noteme.db.DBHandler;
import com.sofe4640.noteme.R;

import java.util.ArrayList;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;

public class NewNoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteSubtitle, noteBody;
    DBHandler db;
    ConstraintLayout myLayout;
    int colorPicked;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.color_picker) {
            openColorPicker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openColorPicker(){
        final ColorPicker colorPicker = new ColorPicker(this);
        ArrayList<String> colors = new ArrayList<>();
        colors.add("#82B926");
        colors.add("#a276eb");
        colors.add("#6a3ab2");
        colors.add("#666666");
        colors.add("#FFFF00");
        colors.add("#3C8D2F");
        colors.add("#FA9F00");
        colors.add("#FF0000");
        colorPicker
                .setDefaultColorButton(Color.parseColor("#f84c44"))
                .setColors(colors)
                .setColumns(4)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        colorPicked = color;
                        //myLayout.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }


    private boolean saveNote() {
        boolean status = false;
        if (noteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Your note needs a title!", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = db.saveNote(noteTitle.getText().toString(),
                    noteSubtitle.getText().toString(), noteBody.getText().toString(), Integer.toString(colorPicked));
            if (success) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                status = true;
            }

        }
        return status;
    }
}