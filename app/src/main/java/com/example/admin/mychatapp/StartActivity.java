package com.example.admin.mychatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
    Button _registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        _registration = findViewById(R.id.start_reg_button);

        _registration.setOnClickListener(registration);

    }

    public View.OnClickListener registration = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
            startActivity(reg_intent);
        }
    };
}
