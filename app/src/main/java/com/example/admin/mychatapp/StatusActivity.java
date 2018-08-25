package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StatusActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout status;
    Button changeStatus_btn;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        progressDialog = new ProgressDialog(this);

        //initializing toolbar
        toolbar = findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        changeStatus_btn = findViewById(R.id.changeStatus_btn);
        status = findViewById(R.id.user_status);
        String previous_status = getIntent().getStringExtra("user_status");
        status.getEditText().setText(previous_status);

        changeStatus_btn.setOnClickListener(changeStatus);
    }

    public View.OnClickListener changeStatus = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressDialog.setTitle("Saving Changes");
            progressDialog.setMessage("Please wait while we save the changes");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            String user_status = status.getEditText().getText().toString();
            databaseReference.child("status").setValue(user_status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "There was some error while saving changes.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    };
}
