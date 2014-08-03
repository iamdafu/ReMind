package com.example.bug.remind;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bug on 11.07.14.
 */
public class NotesDBHelper {
    private NotesDb dbcon;

    public NotesDBHelper(NotesDb dbConnection) {
        dbcon = dbConnection;
    }

    public static Note.NotificaionType getNotifyType(int t) {
        switch (t) {
            case 1:
                return Note.NotificaionType.SOUND;
            case 2:
                return Note.NotificaionType.VIBRO;
            case 3:
                return Note.NotificaionType.BOTH;
            default:
                return Note.NotificaionType.NOTHING;
        }
    }

    public static int getNotifyTypeId(Note.NotificaionType n) {
        if (n == null) return 0;
        switch (n) {
            case SOUND:
                return 1;
            case VIBRO:
                return 2;
            case BOTH:
                return 3;
            default:
                return 0;
        }
    }

    private Note getNote(Cursor cur, int iId, int iName, int iText, int iDate, int iTime,
                         int iGroupId, int iDone) {
        Note result = new Note();
        result.setId(cur.getInt(iId));
        result.setName(cur.getString(iName));
        result.setText(cur.getString(iText));
        result.setDate(cur.getString(iDate));
        result.setTime(cur.getString(iTime));
        result.setGroupId(cur.getInt(iGroupId));
        result.setDone(cur.getInt(iDone) == 1 ? true : false);
        return result;
    }

    public List<Note> getAllNotes() {
        SQLiteDatabase db = dbcon.getWritableDatabase();
        Cursor c = db.query("Notes", null, null, null, null, null, "note_date");
        List<Note> result = getNotes(c);
        dbcon.close();
        return result;
    }

    public List<Note> getTodayNotes() {

        SQLiteDatabase db = dbcon.getWritableDatabase();
        Cursor c = db.query("Notes", null, "date('now') = note_date", null, null, null, "done, note_time");
        List<Note> result = getNotes(c);
        dbcon.close();
        return result;
    }

    public List<Note> getNotesByGroupId(int id) {

        SQLiteDatabase db = dbcon.getWritableDatabase();
        Cursor c = db.query("Notes", null, "group_id = " + String.valueOf(id), null, null, null, "done, note_date, note_time");
        List<Note> result = getNotes(c);
        dbcon.close();
        return result;
    }

    private List<Note> getNotes(Cursor c) {
        List<Note> result = new ArrayList<Note>();
        if (c.moveToFirst()) {
            int iId = c.getColumnIndex("id");
            int iName = c.getColumnIndex("name");
            int iText = c.getColumnIndex("content");
            // "name text, content text, note_date text, note_time text,"+
            //"group_id integer, minutes_to_alarm integer, notification_type integer);"
            int iDate = c.getColumnIndex("note_date");
            int iTime = c.getColumnIndex("note_time");
            int iGroupId = c.getColumnIndex("group_id");
            int iDone = c.getColumnIndex("done");
            do {
                result.add(getNote(c, iId, iName, iText, iDate, iTime, iGroupId, iDone));
            } while (c.moveToNext());
        }
        c.close();
        return result;
    }

    private ContentValues getCVfromNote(Note n) {
        ContentValues cv = new ContentValues();
        cv.put("name", n.getName());
        cv.put("content", n.getText());
        cv.put("note_date", n.getDate());
        cv.put("note_time", n.getTime());
        cv.put("group_id", n.getGroupId());
        cv.put("done", n.getDone() ? 1 : 0);
        return cv;
    }

    public long insertNote(Note n) {
        ContentValues cv = getCVfromNote(n);
        SQLiteDatabase db = dbcon.getWritableDatabase();
        long result = db.insert("Notes", null, cv);
        dbcon.close();
        return result;
    }

    public long updateNote(Note n) {
        ContentValues cv = getCVfromNote(n);
        SQLiteDatabase db = dbcon.getWritableDatabase();
        long result = db.update("Notes", cv, "id=?", new String[]{String.valueOf(n.getId())});
        dbcon.close();
        return result;
    }

    public void deleteNote(long id) {
        SQLiteDatabase db = dbcon.getWritableDatabase();
        db.delete("Notes", "id = " + id, null);
        dbcon.close();
    }

    public List<NoteGroup> getGroups() {
        SQLiteDatabase db = dbcon.getWritableDatabase();
        Cursor c = db.query("NoteGroups", null, null, null, null, null, "id");
        List<NoteGroup> result = new ArrayList<NoteGroup>();
        if (c.moveToFirst()) {
            int iId = c.getColumnIndex("id");
            int iName = c.getColumnIndex("name");
            int iColor = c.getColumnIndex("color");
            do {
                NoteGroup ng = new NoteGroup();
                ng.setId(c.getInt(iId));
                ng.setName(c.getString(iName));
                ng.setColor(c.getString(iColor));
                result.add(ng);
            } while (c.moveToNext());
        }
        c.close();
        dbcon.close();
        return result;
    }

    public void deleteGroup(long id) {
        SQLiteDatabase db = dbcon.getWritableDatabase();
        db.delete("NoteGroups", "id = " + id, null);
        db.delete("Notes", "group_id = " + id, null);
        dbcon.close();
    }

    public long insertGroup(NoteGroup ng) {
        ContentValues cv = new ContentValues();
        cv.put("name", ng.getName());
        cv.put("color", String.format("#%06X", 0xFFFFFF & ng.getColor()));
        SQLiteDatabase db = dbcon.getWritableDatabase();
        long result = db.insert("NoteGroups", null, cv);
        dbcon.close();
        return result;
    }

    public long updateGroup(NoteGroup ng) {
        ContentValues cv = new ContentValues();
        cv.put("name", ng.getName());
        cv.put("color", String.format("#%06X", 0xFFFFFF & ng.getColor()));
        SQLiteDatabase db = dbcon.getWritableDatabase();
        long result = db.update("NoteGroups", cv, "id=?", new String[]{String.valueOf(ng.getId())});
        dbcon.close();
        return result;
    }

    public Note getNoteById(long id) {
        SQLiteDatabase db = dbcon.getWritableDatabase();
        Cursor c = db.query("Notes", null, "id = " + String.valueOf(id), null, null, null, "note_time");
        List<Note> nt = getNotes(c);
        dbcon.close();
        return nt.get(0);
    }
}
