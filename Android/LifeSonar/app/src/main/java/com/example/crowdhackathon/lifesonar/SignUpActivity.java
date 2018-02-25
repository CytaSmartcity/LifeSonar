package com.example.crowdhackathon.lifesonar;

import java.text.SimpleDateFormat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private Button signup, clear;
    private EditText id, name, surname, dateofBirth, street, numberofStreet,
            city, country, postalcode, telephone, email, username, password,
            gender;
    private TextView title;
    private CheckBox check;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Toolbar toolbar;
    private Vibrator vibe;
    private static final int VIBRATE_MILLISECONDS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbarsignup);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        declareSignUpWidgets();

    }


    @Override
    public boolean onSupportNavigateUp() {
        vibe.vibrate(VIBRATE_MILLISECONDS);
        onBackPressed();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first
    }

    private void declareSignUpWidgets() {
        signup = (Button) findViewById(R.id.signup);
        clear = (Button) findViewById(R.id.clearsignup);

        id = (EditText) findViewById(R.id.id);
        dateofBirth = (EditText) findViewById(R.id.age);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        gender = (EditText) findViewById(R.id.gender);
        street = (EditText) findViewById(R.id.street);
        numberofStreet = (EditText) findViewById(R.id.numberofStreet);
        city = (EditText) findViewById(R.id.city);
        country = (EditText) findViewById(R.id.country);
        postalcode = (EditText) findViewById(R.id.postalcode);
        telephone = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        title = (TextView) findViewById(R.id.titlesignup);
        check = (CheckBox) findViewById(R.id.check);

        signup.setOnClickListener(this);
        clear.setOnClickListener(this);
        check.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.signup:
                vibe.vibrate(VIBRATE_MILLISECONDS);
                break;

            case R.id.clearsignup:
                vibe.vibrate(20);
                username.setText("");
                password.setText("");
                id.setText("");
                name.setText("");
                dateofBirth.setText("");
                surname.setText("");
                gender.setText("");
                street.setText("");
                numberofStreet.setText("");
                city.setText("");
                country.setText("");
                postalcode.setText("");
                telephone.setText("");
                email.setText("");
                check.setChecked(false);
                id.requestFocus();
                break;


            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setBackgroundColor(Color.TRANSPARENT);
            password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            password.setInputType(129);
        }
    }
}
