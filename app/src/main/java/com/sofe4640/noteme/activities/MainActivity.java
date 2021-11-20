package com.sofe4640.noteme.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sofe4640.noteme.adapters.RVAdapter;
import com.sofe4640.noteme.db.DBHandler;
import com.sofe4640.noteme.models.Note;
import com.sofe4640.noteme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RVAdapter.NoteViewHolder.OnNoteListener {

    private ArrayList<Note> noteList = new ArrayList<>();
    private DBHandler db;

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
            Intent i = new Intent(getApplicationContext(), NewNoteActivity.class);
            startActivity(i);

        });

        SearchView search = findViewById(R.id.search_box);
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
        RVAdapter noteAdapter = new RVAdapter(noteList, this);
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
        noteList = new ArrayList<>();
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
                note.date = cursor.getString(5);
                note.image = cursor.getString(6);
                noteList.add(note);
            }
        }
    }

    @Override
    public void onNoteClick(int position) {
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra("title", noteList.get(position).getTitle());
        intent.putExtra("subtitle", noteList.get(position).getSubtitle());
        intent.putExtra("body", noteList.get(position).getBody());
        intent.putExtra("color", noteList.get(position).getNoteColor());
        intent.putExtra("image", noteList.get(position).getImage());
        startActivity(intent);
    }

    public void exportToJson(View view) throws JSONException {
        FileWriter file = null;


        JSONObject jResult = new JSONObject();
        JSONArray jArray = new JSONArray();

        for (int i = 0; i < noteList.size(); i++) {
            //System.out.println(noteList.get(i).getTitle());
            JSONObject jGroup = new JSONObject();

            jGroup.put("title", noteList.get(i).getTitle());
            jGroup.put("subtitle", noteList.get(i).getSubtitle());
            jGroup.put("body", noteList.get(i).getBody());
            jGroup.put("color", noteList.get(i).getNoteColor());
            jGroup.put("image", noteList.get(i).getImage());


            jArray.put(jGroup);

        }

        jResult.put("notes", jArray);

        try {


            // Constructs a FileWriter given a file name, using the platform's default charset
            file = new FileWriter("/storage/emulated/0/Download/NoteMe_Export.json");
            file.write(jResult.toString(1));

            Toast toast = Toast.makeText(getApplicationContext(), "Exported Sucessfully", Toast.LENGTH_SHORT);
            toast.show();


        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Export Failed", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        } finally {

            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }
}