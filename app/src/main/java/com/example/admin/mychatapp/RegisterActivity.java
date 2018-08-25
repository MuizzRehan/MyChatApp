package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout _name;
    private TextInputLayout _email;
    private TextInputLayout _password;
    private Button _createBtn;
    private Toolbar reg_toolbar;
    private ProgressDialog progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing FireBase instance
        mAuth = FirebaseAuth.getInstance();

        //Initializing XML variables
        _name = findViewById(R.id.reg_name);
        _email = findViewById(R.id.reg_email);
        _password = findViewById(R.id.reg_password);
        _createBtn = findViewById(R.id.reg_create_account);
        reg_toolbar = findViewById(R.id.reg_toolBar);
        progressBar = new ProgressDialog(this);

        //setting Toolbar
        setSupportActionBar(reg_toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //onClick Listener on CreateAccount Button
        _createBtn.setOnClickListener(createAccount);
    }

    public View.OnClickListener createAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = _name.getEditText().getText().toString();
            String email = _email.getEditText().getText().toString();
            String password = _password.getEditText().getText().toString();

            if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

                //setting ProgressBar
                progressBar.setTitle("Registering User");
                progressBar.setMessage("Please wait we are creating your account");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.show();

                registerUser(name, email, password);
            }
        }
    };

    //For registering a new user
    private void registerUser(final String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.dismiss();

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("status", "Hi there, I am MyChat user " + name);
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");

                    databaseReference.setValue(userMap);
                    /*
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                    finish();
                    */

                } else{
                    progressBar.hide();
                    Toast.makeText(RegisterActivity.this, "Cannot SignIn. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
