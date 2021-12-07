package com.sofe4640.noteme.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.kyanogen.signatureview.SignatureView;
import com.sofe4640.noteme.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DrawingActivity extends AppCompatActivity {

    SignatureView signatureView;
    ImageButton  btnEraser, btnSave, btnBack;
    private static String fileName;
    File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures");

    int incomingActivity;
    String title, subtitle, body, imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        signatureView = findViewById(R.id.signature_view);
        btnEraser = findViewById(R.id.btnEraser);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String date = format.format(new Date());
        fileName =path + "/" + date + ".png";
        if (!path.exists()){
            path.mkdirs();
        };

        Intent intent = getIntent();
        if (intent.getIntExtra("id",0 ) == 2){
            incomingActivity = 2;
            title = intent.getStringExtra("title");
            subtitle = intent.getStringExtra("subtitle");
            body = intent.getStringExtra("body");
            imgPath = intent.getStringExtra("imgPath");
        }
        if (intent.getIntExtra("id",0 ) == 1){
            incomingActivity = 1;
            title = intent.getStringExtra("title");
            subtitle = intent.getStringExtra("subtitle");
            body = intent.getStringExtra("body");
        }

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!signatureView.isBitmapEmpty()){
                    try {
                        saveImage();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewNoteActivity.class);
                if (incomingActivity == 1 ){
                    intent.putExtra("title", title);
                    intent.putExtra("subtitle", subtitle);
                    intent.putExtra("body", body);
                }
                if (incomingActivity == 2 ){
                    intent.putExtra("title", title);
                    intent.putExtra("subtitle", subtitle);
                    intent.putExtra("body", body);
                    intent.putExtra("image", imgPath);
                }
                startActivity(intent);
            }
        });

    }

    private void saveImage() throws IOException {
        //create a file to write bitmap data
        File file = new File(fileName);
        //Convert bitmap to byte array
        Bitmap bitmap = signatureView.getSignatureBitmap();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, NewNoteActivity.class);
        if (incomingActivity == 1 ){
            intent.putExtra("title", title);
            intent.putExtra("subtitle", subtitle);
            intent.putExtra("body", body);
            intent.putExtra("image", fileName);
        }
        if (incomingActivity == 2 ){
            intent.putExtra("title", title);
            intent.putExtra("subtitle", subtitle);
            intent.putExtra("body", body);
            intent.putExtra("image", fileName);
        }
        startActivity(intent);
    }
}