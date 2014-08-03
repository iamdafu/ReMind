package com.example.bug.remind;

import android.graphics.Color;

/**
 * Created by bug on 11.07.14.
 */
public class NoteGroup {
    String groupName;
    long groupId;
    int groupColor;

    public NoteGroup() {
        this.groupId = 0;
        this.groupName = "Новая группа заметок";
        this.groupColor = Color.parseColor("#0000ff");
    }

    public NoteGroup(String id, String name, String color) {
        this.groupId = Integer.parseInt(id);
        this.groupName = name;
        this.groupColor = Color.parseColor(color);
    }

    public String getName() {
        return this.groupName;
    }

    public void setName(String name) {
        this.groupName = name;
    }

    public long getId() {
        return this.groupId;
    }

    public void setId(long id) {
        this.groupId = id;
    }

    public int getColor() {
        return this.groupColor;
    }

    public void setColor(String color) {
        this.groupColor = Color.parseColor(color);
    }

}
