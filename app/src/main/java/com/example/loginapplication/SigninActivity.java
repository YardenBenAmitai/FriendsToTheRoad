package com.example.loginapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SigninActivity extends AppCompatActivity {

    private TextView message;
    private int counter=5;
    private EditText Email;
    private EditText Password;
    private Button Login;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        message= findViewById(R.id.msg);
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.b_login);
        firebaseAuth= FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }


    private void validate(){
        firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(SigninActivity.this, UserActivity.class));
                } else if ((Email.getText().toString().trim().compareTo("admin"))==0 && ( Password.getText().toString().trim().compareTo("admin1")==0)){
                    startActivity(new Intent(SigninActivity.this , AdminActivity.class));
                } else{
                    counter--;
                    if (counter==0){
                        message.setText("No More Attempts\nSystem Locking Out");
                        Login.setEnabled(false);
                    } else{
                        message.setText(counter+" Attempts Remaining");
                    }

                }
            }
        });


    }
}
