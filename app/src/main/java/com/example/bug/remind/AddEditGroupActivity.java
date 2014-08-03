package com.example.bug.remind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AddEditGroupActivity extends ActionBarActivity {
    Spinner spinColor;
    EditText edtName;
    Map<String, String> colors;
    List<String> colorsCaption;
    Button btnSave;
    String noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_group);


        btnSave = (Button) findViewById(R.id.button);
        edtName = (EditText) findViewById(R.id.editText);
        colors = new TreeMap<String, String>();
        colors.put("Красный", "#CC0000");
        colors.put("Оранжевый", "#FF8800");
        colors.put("Зелёный", "#669900");
        colors.put("Фиолетовый", "#9933CC");
        colors.put("Cиний", "#0099CC");
        colorsCaption = new ArrayList<String>();
        colorsCaption.addAll(colors.keySet());
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, colorsCaption);
        spinColor = (Spinner) findViewById(R.id.spinner);
        spinColor.setAdapter(adp);
        noteId = this.getIntent().getStringExtra("ng_id");
        String ngName = this.getIntent().getStringExtra("ng_name");
        edtName.setText(ngName);
        if (!ngName.isEmpty()) {
            setTitle(ngName);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("ng_name", edtName.getText() + "");
                intent.putExtra("ng_color", colors.get(colorsCaption.get(spinColor.getSelectedItemPosition())));
                intent.putExtra("ng_id", noteId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
