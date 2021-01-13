package com.example.exlog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SecondActivity extends AppCompatActivity {

    private ImageButton logout,singlePlayer,duoPlayer;
    private FirebaseAuth firebaseAuth;
    private long backPresssedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        firebaseAuth=FirebaseAuth.getInstance();

        logout=(ImageButton)findViewById(R.id.log_out);

        singlePlayer=(ImageButton)findViewById(R.id.singlepl);
        duoPlayer=(ImageButton)findViewById(R.id.multipl);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(SecondActivity.this);
                builder.setMessage("Are you sure you want to Logout!!").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(SecondActivity.this,MainActivity.class));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        singlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ToSingleplGame.class));
                finish();
            }
        });

        duoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this, Multi_choose.class);
                startActivity(intent);
                //finish();

            }
        });
    }

    @Override
    public void onBackPressed() {

        if(backPresssedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            super.onBackPressed();return;
        }
        else
        {
            backToast= Toast.makeText(getApplicationContext(),"Press Back Again to Exit!!",Toast.LENGTH_SHORT);
            backToast.show();
        }
            backPresssedTime = System.currentTimeMillis();

    }

}