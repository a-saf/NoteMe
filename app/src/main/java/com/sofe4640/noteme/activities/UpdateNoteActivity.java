package com.sofe4640.noteme.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.sofe4640.noteme.R;
import com.sofe4640.noteme.db.DBHandler;

import java.util.ArrayList;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;

public class UpdateNoteActivity extends AppCompatActivity {

    int colorPicked;
    String title;
    String subtitle;
    String body;
    int id;

    EditText noteTitle;
    EditText noteSubtitle;
    EditText noteBody;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        Toolbar myToolbar = findViewById(R.id.toolbar_update);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);

        this.noteTitle = findViewById(R.id.note_title_edit);
        this.noteSubtitle = findViewById(R.id.note_subtitle_edit);
        this.noteBody = findViewById(R.id.note_body_edit);
        db = new DBHandler(this);

        Intent intent = getIntent();
        this.colorPicked = Integer.parseInt(intent.getStringExtra("color"));
        this.title = intent.getStringExtra("title");
        this.subtitle = intent.getStringExtra("subtitle");
        this.body = intent.getStringExtra("body");
        this.id = db.getNoteId(title, subtitle, body);

        System.out.println("Title:" +title);
        System.out.println("subtitle" + subtitle);
        System.out.println("body "+ body);
        noteTitle.setText(title);
        noteSubtitle.setText(subtitle);
        noteBody.setText(body);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.color_picker:
                openColorPicker();
                break;
            case R.id.add_image:
                break;
            case R.id.delete_note:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Title");
                builder.setMessage("Message");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteNote();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
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
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }


    public void updateNote(View view) {
        boolean status = false;
        if (noteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Your note needs a title!", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = db.updateNote(Integer.toString(id), noteTitle.getText().toString(), noteSubtitle.getText().toString(), noteBody.getText().toString(), Integer.toString(colorPicked));
            if (success) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                status = true;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    public void deleteNote(){
        db.deleteNote(Integer.toString(id));
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}