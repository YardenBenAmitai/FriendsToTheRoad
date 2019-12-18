package com.example.loginapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText full_name;
    private EditText email;
    private EditText date;
    private EditText country;
    private EditText password1;
    private EditText password2;
    private TextView info;
    private Button execute;

    private FirebaseAuth fb_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        full_name= findViewById(R.id.fullname);
        email= findViewById(R.id.email_ad);
        date= findViewById(R.id.date);
        country= findViewById(R.id.country);
        password1= findViewById(R.id.pass1);
        password2= findViewById(R.id.pass2);
        info= findViewById(R.id.msg);
        execute= findViewById(R.id.b_signup);

        fb_auth= FirebaseAuth.getInstance();


        execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    //uploading users into database
                    fb_auth.createUserWithEmailAndPassword(email.getText().toString().trim(), password1.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        UserProfile user=new UserProfile(
                                                full_name.getText().toString().trim(),
                                                date.getText().toString(),
                                                country.getText().toString()
                                        );
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(SignupActivity.this, "Registered successfully", Toast.LENGTH_LONG).show();

                                                    Intent intent1 = new Intent( SignupActivity.this , IntroductionActivity.class);
                                                    startActivity(intent1);
                                                }
                                            }
                                        });

                                    } else{
                                        Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });

                }
            }
        });
    }

    private boolean validate(){
            boolean ans=false;
            if ((full_name.getText().toString().isEmpty()) ||
                    (email.getText().toString().isEmpty()) ||
                    (date.getText().toString().isEmpty()) ||
                    (country.getText().toString().isEmpty()) ||
                    (password1.getText().toString().isEmpty()) ||
                    (password2.getText().toString().isEmpty())){
                info.setText("please fill out all the given fields");
            } else if(password1.getText().toString().trim().compareTo(password2.getText().toString().trim()) !=0){
                info.setText("passwords don't match ");
            }
            else if((Integer.parseInt(date.getText().toString())<18)) {
                info.setText("The minimum age to use the app is 18.");
            }
            else{
                ans=true;
            }
            return ans;
    }
}
