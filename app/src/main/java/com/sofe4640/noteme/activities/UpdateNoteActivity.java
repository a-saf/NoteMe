package com.sofe4640.noteme.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.sofe4640.noteme.R;
import com.sofe4640.noteme.db.DBHandler;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;

public class UpdateNoteActivity extends AppCompatActivity {

    int colorPicked;
    String title;
    String subtitle;
    String body;
    int id;
    String imgPath;
    private String selectedImagePath;



    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    EditText noteTitle;
    EditText noteSubtitle;
    EditText noteBody;
    ImageView myImageView;
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
        this.myImageView = (ImageView) findViewById(R.id.imageView1);
        this.noteSubtitle = findViewById(R.id.note_subtitle_edit);
        this.noteBody = findViewById(R.id.note_body_edit);


        db = new DBHandler(this);

        Intent intent = getIntent();
        this.colorPicked = Integer.parseInt(intent.getStringExtra("color"));
        this.title = intent.getStringExtra("title");
        this.subtitle = intent.getStringExtra("subtitle");
        this.body = intent.getStringExtra("body");
        this.imgPath =intent.getStringExtra("image");


                this.id = db.getNoteId(title, subtitle, body);

        System.out.println("Title:" +title);
        System.out.println("subtitle" + subtitle);
        System.out.println("body "+ body);
        noteTitle.setText(title);
        noteSubtitle.setText(subtitle);
        noteBody.setText(body);
        myImageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
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
                imagePermissions();
                break;
            case R.id.delete_note:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true);
                builder.setTitle("Delete Note");
                builder.setMessage("Please confirm if you want to delete");
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
            boolean success = db.updateNote(Integer.toString(id), noteTitle.getText().toString(), noteSubtitle.getText().toString(), noteBody.getText().toString(), Integer.toString(colorPicked), selectedImagePath);
            if (success) {
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
                status = true;
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void imagePermissions(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    UpdateNoteActivity.this,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION
            );
        } else {
            selectImage();
        }
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if ( data != null){
                Uri selectedImageUri  = data.getData();
                if (selectedImageUri != null ){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        myImageView.setImageBitmap(bitmap);
                        myImageView.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor =getContentResolver().query(contentUri, null, null, null, null);
        if(cursor == null){
            filePath = contentUri.getPath();
        }
        else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    public void deleteNote(){
        db.deleteNote(Integer.toString(id));
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}