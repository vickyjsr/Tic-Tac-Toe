package com.example.exlog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private EditText name,password;
    private ImageButton login;
    private ImageButton userregis,google_signin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private int counter=5;
    private CheckBox showPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user1=mAuth.getCurrentUser();
//        if(user1!=null)
//        {
//            startActivity(new Intent(MainActivity.this,SecondActivity.class));
//        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        createRequest();
        google_signin=(ImageButton)findViewById(R.id.googlesignbutton);

        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
            finish();
        }

        name=(EditText)findViewById(R.id.username);
        password =(EditText)findViewById(R.id.password);
        login=(ImageButton)findViewById(R.id.btnlogin);
        userregis=(ImageButton)findViewById(R.id.regisbutton);

        showPassword=(CheckBox)findViewById(R.id.checkBox);

        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
                progressDialog.setMessage("What is happenning XD :)");
                progressDialog.show();
            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty()&&!password.getText().toString().isEmpty()) {
                    validation(name.getText().toString(), password.getText().toString());

                }
                else {
                    Toast.makeText(getApplicationContext(),"Fill all details",Toast.LENGTH_SHORT).show();
                }

            }
        });
        userregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //google sign in
    private void createRequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            finish();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this,user.getDisplayName(),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this,SecondActivity.class));

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),task.getException().toString(),Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private void validation(final String userName, String userPassword )
    {
        progressDialog.setMessage("Wait :)");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    String s = userName.substring(0,userName.indexOf('@'));
                    s.replaceAll(".", "");
                    Toast.makeText(MainActivity.this,"Login Succesfull "+ s,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Login Failed, you have " +counter+" chances left!!",Toast.LENGTH_SHORT).show();
                    counter--;
                    if(counter==0)
                    {
                        Toast.makeText(MainActivity.this,"Button Disabled",Toast.LENGTH_SHORT).show();
                        login.setEnabled(false);
                    }
                }
            }
        });


    }
}