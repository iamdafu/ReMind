package com.example.bug.remind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bug on 12.07.14.
 */
public class NoteGroupsListViewAdapter extends ArrayAdapter<NoteGroup> {
    private final Context context;
    private List<NoteGroup> values;

    public NoteGroupsListViewAdapter(Context context, List<NoteGroup> values) {
        super(context, R.layout.note_group_list_item, values);
        this.context = context;
        this.values = values;
        this.values.add(new NoteGroup("-1", "Добавить группу", "#000000"));

    }

    public void setData(List<NoteGroup> newData) {
        this.values = newData;
        this.values.add(new NoteGroup("-1", "Добавить группу", "#000000"));
    }

    public NoteGroup getNGByPosition(int position) {
        return this.values.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.note_group_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.ng_list_item);
        textView.setText(values.get(position).getName());
        textView.setBackgroundColor(values.get(position).getColor());
        rowView.setBackgroundColor(values.get(position).getColor());
        return rowView;
    }
}
