package com.example.admin.mychatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout _name;
    private TextInputLayout _email;
    private TextInputLayout _password;
    private Button _createBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        _name = findViewById(R.id.reg_name);
        _email = findViewById(R.id.reg_email);
        _password = findViewById(R.id.reg_password);
        _createBtn = findViewById(R.id.reg_create_account);

        _createBtn.setOnClickListener(createAccount);
    }

    public View.OnClickListener createAccount = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String name = _name.getEditText().getText().toString();
            String email = _email.getEditText().getText().toString();
            String password = _password.getEditText().getText().toString();
            registerUser(name, email, password);
        }
    };

    private void registerUser(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else{
                    Toast.makeText(RegisterActivity.this, "get some error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
