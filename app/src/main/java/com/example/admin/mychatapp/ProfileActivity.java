package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private TextView profile_userName;
    private TextView profile_userStatus;
    private TextView profile_userFriends;
    private ImageView profile_userImage;
    private Button sendRequest_btn;
    private Button declineRequest_btn;
    private DatabaseReference profile_databaseReference;
    private DatabaseReference friends_databaseReference;
    private DatabaseReference friendRequest_databaseReference;
    private FirebaseUser profile_firebaseUser;
    private ProgressDialog progressDialog;
    private int currentUser_status;
    int RECEIVE_REQUEST = 0;
    int SEND_REQUEST = 1;
    int CANCEL_REQUEST = 2;
    int FRIENDS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //getting User_id from AllUser Activity
        final String uid = getIntent().getStringExtra("uid");

        //initializing Database Reference
        profile_databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        friendRequest_databaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_Requests");
        friends_databaseReference = FirebaseDatabase.getInstance().getReference().child("Friends");

        //initializing FirebaseUser
        profile_firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //initializing XML Variables
        profile_userName = findViewById(R.id.profile_userName);
        profile_userStatus = findViewById(R.id.profile_userStatus);
        profile_userFriends = findViewById(R.id.profile_user_totalFriends);
        profile_userImage = findViewById(R.id.profile_userImage);
        sendRequest_btn = findViewById(R.id.profile_sendFriendRequest_btn);
        declineRequest_btn = findViewById(R.id.profile_declineFriendRequest_btn);
        currentUser_status = SEND_REQUEST;

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

                friendRequest_databaseReference.child(profile_firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(uid)){

                        String req_type = dataSnapshot.child(uid).child("request_type").getValue().toString();

                        if(req_type.equals("receive") ){

                            currentUser_status = RECEIVE_REQUEST;
                            sendRequest_btn.setText("Accept Friend Request");

                        } else if(req_type.equals("sent")){

                            currentUser_status = CANCEL_REQUEST;
                            sendRequest_btn.setText("Cancel Friend Request");

                        }
                            progressDialog.dismiss();

                     }else {
                            friends_databaseReference.child(profile_firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(uid)){
                                        currentUser_status = FRIENDS;
                                        sendRequest_btn.setText("Unfriend");
                                    }
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressDialog.dismiss();
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
}

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //On sendRequest Button Click
        sendRequest_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser_status == SEND_REQUEST){

                    sendRequest_btn.setEnabled(false);
                    friendRequest_databaseReference.child(profile_firebaseUser.getUid()).child(uid)
                            .child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                            friendRequest_databaseReference.child(uid).child(profile_firebaseUser.getUid())
                                    .child("request_type").setValue("receive")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    sendRequest_btn.setEnabled(true);
                                    sendRequest_btn.setText("Cancel Friend Request");
                                    currentUser_status = CANCEL_REQUEST;
                                    Toast.makeText(ProfileActivity.this, "Request Sent", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                                Toast.makeText(ProfileActivity.this, "Send Request Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if(currentUser_status == CANCEL_REQUEST){

                    sendRequest_btn.setEnabled(false);
                    friendRequest_databaseReference.child(profile_firebaseUser.getUid()).child(uid).removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friendRequest_databaseReference.child(uid).child(profile_firebaseUser.getUid()).removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendRequest_btn.setEnabled(true);
                                    sendRequest_btn.setText("Send Friend Request");
                                    currentUser_status = SEND_REQUEST;
                                    Toast.makeText(ProfileActivity.this, "Cancel Request", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                } else if(currentUser_status == RECEIVE_REQUEST){
                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                    friends_databaseReference.child(profile_firebaseUser.getUid()).child(uid).setValue(currentDate)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            friends_databaseReference.child(uid).child(profile_firebaseUser.getUid()).setValue(currentDate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    friendRequest_databaseReference.child(profile_firebaseUser.getUid()).child(uid).removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    friendRequest_databaseReference.child(uid).child(profile_firebaseUser.getUid()).removeValue()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    sendRequest_btn.setEnabled(true);
                                                                    sendRequest_btn.setText("Unfriend");
                                                                    currentUser_status = FRIENDS;
                                                                    Toast.makeText(ProfileActivity.this, "Friends", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            });

                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
