package com.sofe4640.noteme.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.sofe4640.noteme.models.Note;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DBHandler.db";
    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_DATE = "date";
    public static final String NOTES_COLUMN_TITLE = "title";
    public static final String NOTES_COLUMN_SUBTITLE = "subtitle";
    public static final String NOTES_COLUMN_BODY = "body";
    public static final String NOTES_COLUMN_COLOUR = "colour";
    public static final String NOTES_COLUMN_IMG = "image";

    public DBHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists notes" +
                "(id integer primary key autoincrement not null unique, " +
                "title text, subtitle text, body text, colour text, date text, image text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(sqLiteDatabase);
    }


    @SuppressLint("NewApi")
    public boolean saveNote(String title, String subtitle, String body, String colour, String imgPath) {
        SQLiteDatabase sqldb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //format the date and time stamp
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDt = dt.format(formatter);

        cv.put(NOTES_COLUMN_TITLE, title);
        cv.put(NOTES_COLUMN_SUBTITLE, subtitle);
        cv.put(NOTES_COLUMN_BODY, body);
        cv.put(NOTES_COLUMN_COLOUR, colour);
        cv.put(NOTES_COLUMN_DATE, formattedDt);
        cv.put(NOTES_COLUMN_IMG, imgPath);
        long inserted = sqldb.insert(NOTES_TABLE_NAME, null, cv);

        boolean saved = inserted > 0;
        return saved;
    }

    @SuppressLint("NewApi")
    public boolean updateNote(String id, String title, String subtitle, String body, String colour, String imgPath){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        LocalDateTime dt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDt = dt.format(formatter);

        cv.put(NOTES_COLUMN_TITLE, title);
        cv.put(NOTES_COLUMN_SUBTITLE, subtitle);
        cv.put(NOTES_COLUMN_BODY, body);
        cv.put(NOTES_COLUMN_COLOUR, colour);
        cv.put(NOTES_COLUMN_DATE, formattedDt);
        cv.put(NOTES_COLUMN_IMG, imgPath);

        long result = sqldb.update(NOTES_TABLE_NAME, cv,"id=?", new String[]{id});

        boolean saved = result > 0 ? true : false;
        return saved;
    }

    public void deleteNote(String id){
        SQLiteDatabase sqldb = this.getWritableDatabase();
        long result = sqldb.delete(NOTES_TABLE_NAME, "id=?", new String[]{id});
    }

    // Returns cursor on rows that correspond to search string
    public Cursor searchNote(String searchable){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE_NAME + " where title = '" + searchable +"';";

        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    // Returns all notes in db
    public Cursor getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE_NAME;

        Cursor cursor = null;
        if(db != null ){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public int getNoteId(String title, String subtitle, String body){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ROWID, * FROM " + NOTES_TABLE_NAME + " WHERE title = '" + title + "' AND subtitle = '" + subtitle + "' AND body = '" + body +"';";
        Cursor res = db.rawQuery(query, null);
        int id=-1;
        if(res!=null&&res.moveToFirst()) {
            id = res.getInt(res.getColumnIndex("id"));
        }
        return id;
    }

}
