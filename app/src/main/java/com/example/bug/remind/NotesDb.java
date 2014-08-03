package com.example.bug.remind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by bug on 11.07.14.
 */
public class NotesDb extends SQLiteOpenHelper {
    public NotesDb(Context context) {
        super(context, "NotesDB", null, 1);
    }

    //#959595
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("NotesDB", "Create new DB");
        db.execSQL("create table NoteGroups ( id integer primary key autoincrement, name text, color text);");
        db.execSQL("create table Notes (" +
                        "id integer primary key autoincrement," +
                        "name text, content text, note_date text, note_time text," +
                        "group_id integer, done integer);"
        );
        db.execSQL("insert into NoteGroups(name, color) values('Важно','#FF8800');");
        db.execSQL("insert into NoteGroups(name, color) values('Срочно','#CC0000');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
