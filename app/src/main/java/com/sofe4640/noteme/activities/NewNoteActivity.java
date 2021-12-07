package com.sofe4640.noteme.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sofe4640.noteme.db.DBHandler;
import com.sofe4640.noteme.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import petrov.kristiyan.colorpicker.ColorPicker;

public class NewNoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteSubtitle, noteBody;
    DBHandler db;
    int colorPicked;
    ImageView imageNote;
    private String selectedImagePath;
    String currentPhotoPath;
    File photoFile;

    String title, subtitle, body;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private static final int CAMERA_REQUEST = 1888;

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
        imageNote = findViewById(R.id.noteImage);

        //Save button navigate back to home screen
        FloatingActionButton saveButton = findViewById(R.id.new_note_btn);
        saveButton.setOnClickListener(view -> {
            boolean saved = saveNote();
            if (saved) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }

        });

        selectedImagePath="";

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null){
            if (extras.containsKey("image")){
                selectedImagePath = extras.getString("image");
                imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                imageNote.setVisibility(View.VISIBLE);
            }
            if (extras.containsKey("title")){
                title = extras.getString("title");
                noteTitle.setText(title);
            }
            if (extras.containsKey("subtitle")){
                subtitle = extras.getString("subtitle");
                noteSubtitle.setText(subtitle);
            }
            if (extras.containsKey("body")){
                body = extras.getString("body");
                noteBody.setText(body);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
            case R.id.open_camera:
                addImage();
                break;
            case R.id.open_drawing:
                addDrawing();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addDrawing(){
        title = noteTitle.getText().toString();
        subtitle = noteSubtitle.getText().toString();
        body = noteBody.getText().toString();

        Intent intent = new Intent(this, DrawingActivity.class);
        intent.putExtra("id", 1);
        if(title != null){
            intent.putExtra("title", title);
        }
        if(subtitle != null){
            intent.putExtra("subtitle", subtitle);
        }
        if(body != null){
            intent.putExtra("body", body);
        }
        startActivity(intent);
    }

    public void addImage(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println("error when creating file");
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sofe4640.noteme.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //takePictureIntent.putExtra("uri", photoURI);

                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
//        }

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


    private boolean saveNote() {
        boolean status = false;
        if (noteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Your note needs a title!", Toast.LENGTH_SHORT).show();
        } else {
            boolean success = db.saveNote(noteTitle.getText().toString(),
                    noteSubtitle.getText().toString(), noteBody.getText().toString(), Integer.toString(colorPicked), selectedImagePath);
            if (success) {
                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
                status = true;
            }

        }
        return status;
    }

    private void imagePermissions(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    NewNoteActivity.this,
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
        System.out.println("Request code" + requestCode);
        if (requestCode == CAMERA_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
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
                        imageNote.setImageBitmap(bitmap);
                        imageNote.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    } catch (FileNotFoundException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        System.out.println(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(Uri.fromFile(photoFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageNote.setImageBitmap(bitmap);
            imageNote.setVisibility(View.VISIBLE);

            selectedImagePath = getPathFromUri(Uri.fromFile(photoFile));
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
}