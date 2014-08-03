package com.example.bug.remind;

/**
 * Created by bug on 11.07.14.
 */
public class Note {


    long id;

    ;
    String noteName;
    String noteText;
    int done;
    int groupId;
    String date;
    String time;

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return this.noteName;
    }

    public void setName(String name) {
        this.noteName = name;
    }

    public String getText() {
        return this.noteText;
    }

    public void setText(String text) {
        this.noteText = text;
    }

    public int getGroupId() {
        return this.groupId;
    }

    public void setGroupId(int id) {
        this.groupId = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getDone() {
        if (this.done == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setDone(boolean d) {
        if (d) {
            this.done = 1;
        } else {
            this.done = 0;
        }
    }

    public enum NotificaionType {NOTHING, VIBRO, SOUND, BOTH}

}
