package com.example.admin.mychatapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    CircleImageView circleImageView;
    TextView user_name;
    TextView user_status;
    Button changeStatusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        circleImageView = findViewById(R.id.setting_image);
        user_name = findViewById(R.id.setting_name);
        user_status = findViewById(R.id.setting_status);
        changeStatusBtn = findViewById(R.id.setting_changeStatus);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = firebaseUser.getUid();

        changeStatusBtn.setOnClickListener(changeStatus);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                user_name.setText(name);
                user_status.setText(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public View.OnClickListener changeStatus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent statusActivity = new Intent(SettingActivity.this, StatusActivity.class);
            statusActivity.putExtra("user_status", user_status.getText().toString());
            startActivity(statusActivity);
        }
    };
}
