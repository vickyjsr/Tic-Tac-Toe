package com.example.exlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private ImageButton loginback,registolog;
    private EditText name,password2,phone,username1,email;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();

        name=(EditText)findViewById(R.id.editTextTextPersonName);
        password2=(EditText)findViewById(R.id.editTextTextPassword);
        phone=(EditText)findViewById(R.id.editTextPhone);
        username1=(EditText)findViewById(R.id.editTextTextUsername);
        email=(EditText)findViewById(R.id.editTextTextEmailAddress);

        loginback=(ImageButton)findViewById(R.id.backtologin);
        registolog=(ImageButton)findViewById(R.id.registologin);

        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        registolog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate()
    {
        if(!name.getText().toString().isEmpty()&&!password2.getText().toString().isEmpty()&&!phone.getText().toString().isEmpty()&&!username1.getText().toString().isEmpty()&&!email.getText().toString().isEmpty())
        {
            //upload data to database
            String user_email=email.getText().toString().trim();
            String user_password=password2.getText().toString().trim();
            firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        sendUserData();
                        Toast.makeText(RegistrationActivity.this,"Registration Succesful",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(RegistrationActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(RegistrationActivity.this,"Registration UnSuccesful",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"Fill all Details",Toast.LENGTH_SHORT).show();
        }
    }

    private void sendUserData(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference(firebaseAuth.getUid());

        UserProfile userProfile = new UserProfile(username1.getText().toString(),phone.getText().toString(), email.getText().toString(), name.getText().toString());
        myRef.setValue(userProfile);
    }

}