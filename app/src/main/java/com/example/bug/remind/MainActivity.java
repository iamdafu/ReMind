package com.example.bug.remind;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class MainActivity extends ActionBarActivity {
    NotesDBHelper ndbh;
    ListView lvToday;
    ListView lvGroups;
    NoteGroupsListViewAdapter adapt;
    NotesListViewAdapter ntdAdapt;

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ListView lvToday = (ListView) findViewById(R.id.listView);
        ListView lvGroups = (ListView) findViewById(R.id.listView2);
        ndbh = new NotesDBHelper(new NotesDb(this));
        List<NoteGroup> lng = ndbh.getGroups();
        if (!isMyServiceRunning(MyNotifyService.class)) {
            Intent intent = new Intent(this, MyNotifyService.class);
            startService(intent);
        }

        ntdAdapt = new NotesListViewAdapter(this, ndbh.getTodayNotes(), lng);
        lvToday.setAdapter(ntdAdapt);
        lvToday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NoteViewEditActivity.class);
                long iii = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getId();
                long giii = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getGroupId();
                //TODO: fix
                intent.putExtra("n_id", iii);
                intent.putExtra("n_gid", giii);
                startActivityForResult(intent, 1);
            }
        });
        lvToday.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long iid = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getId();
                String name = ((NotesListViewAdapter) adapterView.getAdapter()).getNoteByPos(i).getName();
                showNoteDelDialog(ndbh, iid, name);
                return true;
            }
        });
        adapt = new NoteGroupsListViewAdapter(this, lng);

        lvGroups.setAdapter(adapt);
        lvGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long iii = ((NoteGroupsListViewAdapter) adapterView.getAdapter()).getNGByPosition(i)
                        .getId();
                if (iii == -1) {
                    //TODO: fix
                    Intent intent = new Intent(MainActivity.this, AddEditGroupActivity.class);
                    intent.putExtra("ng_id", "-1");
                    intent.putExtra("ng_name", "");
                    startActivityForResult(intent, 1);
                    return;
                }
                //TODO: fix
                Intent intent = new Intent(MainActivity.this, ViewGroupActivity.class);
                intent.putExtra("ng_id", iii);
                intent.putExtra("ng_name", ((NoteGroupsListViewAdapter) adapterView.getAdapter())
                        .getNGByPosition(i).getName());
                startActivityForResult(intent, 1);
            }
        });
        lvGroups.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long iii = ((NoteGroupsListViewAdapter) adapterView.getAdapter()).getNGByPosition(i)
                        .getId();
                if (iii == -1) return false;
                NoteGroup ng = ((NoteGroupsListViewAdapter) adapterView.getAdapter()).getNGByPosition(i);
                showGroupUpdateEditDialog(ndbh, ng, (NoteGroupsListViewAdapter) adapterView.getAdapter());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showNoteDelDialog(final NotesDBHelper ndbm, final long id, final String name) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle(name);
        adb.setItems(new String[]{"Удалить"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ndbm.deleteNote(id);
                updateGroupsAdapter();
            }
        });
        adb.show();
    }

    public void showGroupUpdateEditDialog(final NotesDBHelper ndb, final NoteGroup ng, final NoteGroupsListViewAdapter adapter) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setTitle(ng.getName());
        adb.setItems(new String[]{"Редактировать", "Удалить"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 1) {
                    ndb.deleteGroup(ng.getId());
                    updateGroupsAdapter();
                    return;
                } else {
                    //TODO: fix
                    Intent intent = new Intent(MainActivity.this, AddEditGroupActivity.class);
                    intent.putExtra("ng_id", String.valueOf(ng.getId()));
                    intent.putExtra("ng_name", ng.getName());
                    startActivityForResult(intent, 1);
                }

            }
        });
        adb.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            updateGroupsAdapter();
            return;
        }

        String name = data.getStringExtra("ng_name");
        String color = data.getStringExtra("ng_color");
        String id = data.getStringExtra("ng_id");
        if (id.contains("-1")) {
            //Toast.makeText(this, "fgsfds!", Toast.LENGTH_LONG).show();
            NoteGroup ng = new NoteGroup(id, name, color);
            ng.setId(ndbh.insertGroup(ng));
            updateGroupsAdapter();
        } else {
            NoteGroup ng = new NoteGroup(id, name, color);
            ndbh.updateGroup(ng);
            updateGroupsAdapter();
        }

    }

    public void updateGroupsAdapter() {
        adapt.clear();
        List<NoteGroup> ngl = ndbh.getGroups();
        for (NoteGroup nng : ngl) {
            adapt.add(nng);
        }
        adapt.add(new NoteGroup("-1", "Добавить группу", "#000000"));
        adapt.notifyDataSetChanged();
        ntdAdapt.clear();
        List<Note> nl = ndbh.getTodayNotes();
        for (Note nn : nl) {
            ntdAdapt.add(nn);
        }
        ntdAdapt.notifyDataSetChanged();

    }
}



