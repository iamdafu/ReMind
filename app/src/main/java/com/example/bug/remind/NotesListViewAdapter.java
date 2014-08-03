package com.example.bug.remind;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bug on 13.07.14.
 */
public class NotesListViewAdapter extends ArrayAdapter<Note> {
    Context context;
    List<Note> values;
    List<NoteGroup> groups;

    public NotesListViewAdapter(Context context, List<Note> values, List<NoteGroup> groups) {
        super(context, R.layout.note_grouped_list_item, values);
        this.context = context;
        this.values = values;
        this.groups = groups;
    }

    public Note getNoteByPos(int i) {
        return this.values.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.note_grouped_list_item, parent, false);

        int color = Color.parseColor("#000000");
        for (NoteGroup ng : groups) {
            if (ng.getId() == values.get(position).getGroupId()) {
                color = ng.getColor();
            }
        }
        if (values.get(position).getDone()) {
            color = Color.parseColor("#959595");
        }
        TextView textName = (TextView) rowView.findViewById(R.id.nl_name);
        TextView textDate = (TextView) rowView.findViewById(R.id.nl_date);
        TextView textTime = (TextView) rowView.findViewById(R.id.nl_time);

        textName.setText(values.get(position).getName());
        textName.setBackgroundColor(color);

        textDate.setText(values.get(position).getDate());
        textDate.setBackgroundColor(color);

        textTime.setText(values.get(position).getTime());
        textTime.setBackgroundColor(color);

        rowView.setBackgroundColor(color);
        return rowView;
    }
}
