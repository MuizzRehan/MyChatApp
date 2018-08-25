package com.example.admin.mychatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class UsersActivity extends AppCompatActivity {
    private Toolbar usersToolbar;
    private RecyclerView users_recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        //initializing and setting Toolbar
        usersToolbar = findViewById(R.id.users_app_bar);
        setSupportActionBar(usersToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        users_recyclerView = findViewById(R.id.allUsers_recyclerView);



    }
}
