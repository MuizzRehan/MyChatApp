package com.example.admin.mychatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersActivity extends AppCompatActivity {
    private Toolbar usersToolbar;
    private RecyclerView users_recyclerView;
    DatabaseReference databaseReference;

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
        users_recyclerView.setHasFixedSize(true);
        users_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users, UsersRecycleViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersRecycleViewHolder>(
                Users.class,
                R.layout.single_user_layout,
                UsersRecycleViewHolder.class,
                databaseReference
                ) {
            @Override
            protected void populateViewHolder(UsersRecycleViewHolder viewHolder, Users model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setImage(model.getThumbImage());
                viewHolder.setStatus(model.getStatus());
                final String uid = getRef(position).getKey();

                viewHolder.userView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(UsersActivity.this, ProfileActivity.class);
                        profileIntent.putExtra("uid", uid);
                        startActivity(profileIntent);
                    }
                });
            }
        };
        users_recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
