package com.example.bug.remind;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class ViewGroupActivity extends ActionBarActivity {
    ListView lvItems;
    Button addButton;
    NotesListViewAdapter nladapt;
    NotesDBHelper ndbh;
    long group_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);


        ndbh = new NotesDBHelper(new NotesDb(this));
        setTitle(this.getIntent().getStringExtra("ng_name"));
        group_id = this.getIntent().getLongExtra("ng_id", 0);
        addButton = (Button) findViewById(R.id.btnAddNote);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewGroupActivity.this, NoteViewEditActivity.class);
                intent.putExtra("n_gid", group_id);
                startActivityForResult(intent, 1);
            }
        });
        lvItems = (ListView) findViewById(R.id.lvGroupNotesView);
        nladapt = new NotesListViewAdapter(this, ndbh.getNotesByGroupId((int) group_id), ndbh.getGroups());
        lvItems.setAdapter(nladapt);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ViewGroupActivity.this, NoteViewEditActivity.class);
                long iii = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getId();
                long giii = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getGroupId();
                intent.putExtra("n_id", iii);
                intent.putExtra("n_gid", giii);
                startActivityForResult(intent, 1);
            }
        });

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long iid = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getId();
                String name = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getName();
                showNoteDelDialog(ndbh, iid, name);
                return true;
            }
        });
    }


    /**
     * A placeholder fragment containing a simple view.
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            nladapt.clear();
            for (Note n : ndbh.getNotesByGroupId((int) group_id)) {
                nladapt.add(n);
            }
            nladapt.notifyDataSetChanged();
            return;
        }
        return;
    }

    public void showNoteDelDialog(final NotesDBHelper ndbm, final long id, final String name) {
        AlertDialog.Builder adb = new AlertDialog.Builder(ViewGroupActivity.this);
        adb.setTitle(name);
        adb.setItems(new String[]{"Удалить"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ndbm.deleteNote(id);
                nladapt.clear();
                for (Note n : ndbm.getNotesByGroupId((int) group_id)) {
                    nladapt.add(n);
                }
                nladapt.notifyDataSetChanged();
            }
        });
        adb.show();
    }
}
