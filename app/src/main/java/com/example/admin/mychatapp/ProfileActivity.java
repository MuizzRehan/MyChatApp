package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    private TextView profile_userName;
    private TextView profile_userStatus;
    private TextView profile_userFriends;
    private ImageView profile_userImage;
    private Button sendRequest_btn;
    private Button declineRequest_btn;
    private DatabaseReference profile_databaseReference;
    private FirebaseUser profile_firebaseUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting User_id from AllUser Activity
        String uid = getIntent().getStringExtra("uid");

        //initializing Database Reference
        profile_databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        //initializing XML Variables
        profile_userName = findViewById(R.id.profile_userName);
        profile_userStatus = findViewById(R.id.profile_userStatus);
        profile_userFriends = findViewById(R.id.profile_user_totalFriends);
        profile_userImage = findViewById(R.id.profile_userImage);
        sendRequest_btn = findViewById(R.id.profile_sendFriendRequest_btn);
        declineRequest_btn = findViewById(R.id.profile_declineFriendRequest_btn);

        //initializing and setting ProcessDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading User Data");
        progressDialog.setMessage("Please wait while we load the user data");
        progressDialog.show();

        //retrieving USER DATA from Firebase Database
        profile_databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                String userStatus = dataSnapshot.child("status").getValue().toString();
                String userImage = dataSnapshot.child("image").getValue().toString();

                profile_userName.setText(userName);
                profile_userStatus.setText(userStatus);

                Picasso.get().load(userImage).placeholder(R.drawable.avatar_image).into(profile_userImage);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
