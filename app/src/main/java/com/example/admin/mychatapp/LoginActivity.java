package com.example.admin.mychatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private Toolbar login_toolbar;
    private TextInputLayout _email;
    private TextInputLayout _password;
    private Button _login_btn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing XML Variables
        login_toolbar = findViewById(R.id.login_toolBar);
        _email = findViewById(R.id.login_email);
        _password = findViewById(R.id.login_password);
        _login_btn = findViewById(R.id.login_btn);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        //Setting Toolbar
        setSupportActionBar(login_toolbar);
        getSupportActionBar().setTitle("Login to Your Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _login_btn.setOnClickListener(login);
    }

    public View.OnClickListener login = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String email = _email.getEditText().getText().toString();
            String password = _password.getEditText().getText().toString();

            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                //setting ProgressDialog
                progressDialog.setTitle("Login User");
                progressDialog.setMessage("Please wait, we are checking your credentials.");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                loginUser(email, password);
            }
        }
    };

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainIntent);
                } else {
                    progressDialog.hide();
                    Toast.makeText(LoginActivity.this, "can't SignIn. Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
