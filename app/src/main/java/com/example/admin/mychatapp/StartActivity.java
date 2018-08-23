package com.example.admin.mychatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button _registration;
    Button _login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        _login = findViewById(R.id.start_login_btn);
        _registration = findViewById(R.id.start_reg_button);

        _registration.setOnClickListener(registration);
        _login.setOnClickListener(login);

    }

    public View.OnClickListener registration = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(reg_intent);
        }
    };

    public View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(login_intent);
        }
    };
}
