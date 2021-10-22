package com.sofe4640.noteme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.Date;

public class NoteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NoteDB.db";
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_DATE = "date";
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_SUBTITLE = "subtitle";
    public static final String NOTES_COLUMN_BODY = "body";

    public NoteDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists notes" +
                "(id integer primary key, title text, date text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(sqLiteDatabase);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean saveNote(String title, String subtitle, String body) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NOTES_COLUMN_TITLE, title);
        cv.put(NOTES_COLUMN_SUBTITLE, subtitle);
        cv.put(NOTES_COLUMN_BODY, body);
        cv.put(NOTES_COLUMN_DATE, LocalDateTime.now().toString());
        sqldb.insert(NOTES_TABLE_NAME, null, cv);

        return true;
    }
}
