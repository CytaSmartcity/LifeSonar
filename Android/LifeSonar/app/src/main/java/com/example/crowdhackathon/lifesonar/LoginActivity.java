package com.example.crowdhackathon.lifesonar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.crowdhackathon.graphs.MeasurementsCapture;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private Button login, register, guest;
    private TextView title, orTitle;
    private EditText username, password;
    private CheckBox check;
    private Vibrator vibe;
    private ImageButton language, about;
    private Toolbar toolbar;

    private static final int VIBRATE_MILLISECONDS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbarlogin);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        declareLoginWidgets();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login:
                vibe.vibrate(VIBRATE_MILLISECONDS);
                Intent intentLogin = new Intent(LoginActivity.this,
                        MeasurementsCapture.class);
                intentLogin.putExtra("username",  username.getText().toString());
                startActivity(intentLogin);
                break;

            case R.id.registerButton:
                Intent intentSignUp = new Intent(LoginActivity.this,
                        SignUpActivity.class);
                startActivity(intentSignUp);
                break;

            case R.id.quest:
                vibe.vibrate(VIBRATE_MILLISECONDS);
                Intent intentQuest = new Intent(LoginActivity.this,
                        MeasurementsCapture.class);
                intentQuest.putExtra("username",  "");
                startActivity(intentQuest);
                break;

            case R.id.about:
                vibe.vibrate(VIBRATE_MILLISECONDS);
                break;

            case R.id.language:
                vibe.vibrate(VIBRATE_MILLISECONDS);
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

    @Override
    public boolean onSupportNavigateUp() {
        vibe.vibrate(VIBRATE_MILLISECONDS);
        onBackPressed();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void declareLoginWidgets() {

        login = (Button) findViewById(R.id.login);
        guest = (Button) findViewById(R.id.quest);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.registerButton);
        title = (TextView) findViewById(R.id.title);
        check = (CheckBox) findViewById(R.id.check);
        orTitle = (TextView) findViewById(R.id.orTitle);
        language = (ImageButton) toolbar.findViewById(R.id.language);
        about = (ImageButton) toolbar.findViewById(R.id.about);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        guest.setOnClickListener(this);
        check.setOnCheckedChangeListener(this);
        language.setOnClickListener(this);
        about.setOnClickListener(this);

    }
}
