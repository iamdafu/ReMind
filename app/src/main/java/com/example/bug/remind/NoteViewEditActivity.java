package com.example.bug.remind;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class NoteViewEditActivity extends ActionBarActivity {
    long group_id;
    long note_id;
    EditText edtName;
    EditText edtContent;
    TextView edtDate;
    TextView edtTime;
    Button btnSave;
    NotesDBHelper ndbh;
    Note nNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view_edit);
        this.note_id = this.getIntent().getLongExtra("n_id", -1);
        this.group_id = this.getIntent().getLongExtra("n_gid", -1);
        btnSave = (Button) findViewById(R.id.btnSaveNote);
        edtName = (EditText) findViewById(R.id.edtNoteName);
        edtContent = (EditText) findViewById(R.id.edtNoteContent);
        ndbh = new NotesDBHelper(new NotesDb(this));
        Calendar cldr = Calendar.getInstance();
        String currDate = cldr.get(Calendar.YEAR) + "-" + String.format("%02d", cldr
                .get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cldr.get(Calendar.DAY_OF_MONTH));
        String currTime = String.format("%02d", cldr.get(Calendar.HOUR_OF_DAY)) + ":" + String
                .format("%02d", cldr.get(Calendar.MINUTE));
        edtDate = (TextView) findViewById(R.id.tvNoteEdtDate);
        edtDate.setPaintFlags(edtDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        edtTime = (TextView) findViewById(R.id.tvNoteEditTime);
        edtTime.setPaintFlags(edtTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        nNote = new Note();
        nNote.setDate(currDate);
        nNote.setTime(currTime);
        if (this.note_id > 0) {
            nNote = ndbh.getNoteById(note_id);
            edtName.setText(nNote.getName());
            edtContent.setText(nNote.getText());
        }
        edtDate.setText(nNote.getDate());
        edtTime.setText(nNote.getTime());
        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] fulldate = ((TextView) view).getText().toString().split("-");
                int year = Integer.parseInt(fulldate[0]);
                int month = Integer.parseInt(fulldate[1]) - 1;
                int day = Integer.parseInt(fulldate[2]);
                DatePickerDialog tpd = new DatePickerDialog(NoteViewEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                                edtDate.setText(i + "-" + String.format("%02d", i2 + 1) + "-" + i3);
                            }
                        }, year, month, day
                );
                tpd.show();

            }
        });
        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ts = ((TextView) view).getText().toString().split(":");
                int hour = Integer.parseInt(ts[0]);
                int minute = Integer.parseInt(ts[1]);
                TimePickerDialog tpd = new TimePickerDialog(NoteViewEditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i2) {
                                edtTime.setText(String.format("%02d", i) + ":" + String.format("%02d", i2));
                            }

                        }, hour, minute, true
                );
                tpd.show();

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nNote.setDate(edtDate.getText().toString());
                nNote.setTime(edtTime.getText().toString());
                nNote.setName(edtName.getText().toString());
                nNote.setText(edtContent.getText().toString());
                nNote.setDone(false);
                nNote.setGroupId((int) group_id);
                if (note_id > 0) {
                    ndbh.updateNote(nNote);
                    finish();
                    return;
                } else {
                    ndbh.insertNote(nNote);
                    finish();
                    return;
                }
            }
        });


    }


    /**
     * A placeholder fragment containing a simple view.
     */


}
