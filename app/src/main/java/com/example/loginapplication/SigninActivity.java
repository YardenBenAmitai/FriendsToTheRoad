package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SigninActivity extends AppCompatActivity {

    private TextView message;
    private int counter=5;
    private EditText Email;
    private EditText Password;
    private RadioButton radio_b;
    private RadioGroup group;
    private Button Login;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        message= findViewById(R.id.msg);
        Email = findViewById(R.id.etEmail);
        Password = findViewById(R.id.etPassword);
        group=findViewById(R.id.radio_g);
        Login = findViewById(R.id.b_login);
        firebaseAuth= FirebaseAuth.getInstance();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set role
                int selectedId= group.getCheckedRadioButtonId();
                radio_b= findViewById(selectedId);

                //proceed to authentication
                validate();
            }
        });

    }

    private void validate(){

        firebaseAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    counter--;
                    if (counter==0){
                        message.setText("No More Attempts\nSystem Locking Out");
                        Login.setEnabled(false);
                    } else{
                        message.setText(counter+" Attempts Remaining");
                    }
                } else{
                    FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("signin_role").setValue(radio_b.getText().toString());

                    startActivity(new Intent(SigninActivity.this, HomeActivity.class));
                }
            }
        });


    }
}
