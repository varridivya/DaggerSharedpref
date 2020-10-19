package com.example.daggerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import javax.inject.Inject;
import com.example.daggerexample.MyComponent;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {
    //EditText inUsername, inNumber;
    String[] courses = { "Choose course","Java", "Algorithms and DataStructures", "Kotlin", "Android" };
    Button btnSave, btnGet;
    Spinner spin;
    DatePickerDialog picker;
    EditText eText;
    private MyComponent myComponent;
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                //date picker dialog
                picker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    // i=year,i1=monthofyear,i2=dayofmonth
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        eText.setText(i2 + "/" + (i1 + 1) + "/" +i);
                    }
                },year,month,day);
                picker.show();

            }
        });
        myComponent = DaggerMyComponent.builder().sharedPrefModule(new SharedPrefModule(this)).build();
        myComponent.inject(this);


    }

    private void initViews() {
        btnGet = findViewById(R.id.btnGet);
        btnSave = findViewById(R.id.btnSave);
        //inUsername = findViewById(R.id.inUsername);
        //inNumber = findViewById(R.id.inNumber);
        spin = (Spinner) findViewById(R.id.spinner1);
        eText=(EditText) findViewById(R.id.editText1);
        btnSave.setOnClickListener(this);
        btnGet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnGet:
                //spin.setText(sharedPreferences.getString("username", "default"));
                eText.setText(sharedPreferences.getString("number", "12345"));
                spin.setSelection(sharedPreferences.getInt("spinnerSelection",0));
                break;
            case R.id.btnSave:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //editor.putString("username", spin.getText().toString().trim());
                editor.putString("number", eText.getText().toString().trim());
                int selectedPosition = spin.getSelectedItemPosition();
                editor.putInt("spinnerSelection", selectedPosition);
                editor.apply();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "Selected Course: "+courses[i] ,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
