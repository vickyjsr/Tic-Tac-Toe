package com.example.exlog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Delayed;

public class ToSingleplGame extends AppCompatActivity {

    private ImageButton logout,back,reset;
    private FirebaseAuth firebaseAuth;
    int gameState,player=0,ai=0;
    private TextView textViewPlayer1,textViewPlayer2;
    private long backPresssedTime;
    private Toast backToast;
    ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();
    Map<Integer, Integer> m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_single_game);

        gameState = 1;//1-can_play, 2 - gameOver, 3 - draw


        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_View_p2);

        firebaseAuth=FirebaseAuth.getInstance();
        logout=(ImageButton)findViewById(R.id.log_out);
        back=(ImageButton)findViewById(R.id.back);
        reset = (ImageButton)findViewById(R.id.reset);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(ToSingleplGame.this);
                builder.setMessage("Are you sure you want to Logout!!").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(ToSingleplGame.this,MainActivity.class));
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =new AlertDialog.Builder(ToSingleplGame.this);
                builder.setMessage("Are you sure!!").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                        startActivity(new Intent(ToSingleplGame.this,SecondActivity.class));
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
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =new AlertDialog.Builder(ToSingleplGame.this);
                builder.setMessage("Sure you wanna Reset!!").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(ToSingleplGame.this,ToSingleplGame.class));
                        finish();
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

    }


    public void GameBoardClick(View view){
        ImageView selectedImage = (ImageView) view;

        int selectedBlock = 0;
        switch ((selectedImage.getId())) {
            case R.id.iv_11: selectedBlock = 1; break;
            case R.id.iv_12: selectedBlock = 2; break;
            case R.id.iv_13: selectedBlock = 3; break;

            case R.id.iv_21: selectedBlock = 4; break;
            case R.id.iv_22: selectedBlock = 5; break;
            case R.id.iv_23: selectedBlock = 6; break;

            case R.id.iv_31: selectedBlock = 7; break;
            case R.id.iv_32: selectedBlock = 8; break;
            case R.id.iv_33: selectedBlock = 9; break;
        }

        PlayGame(selectedBlock, selectedImage);
    }



    int activePlayer = 1;
    ArrayList<Integer> Player1 = new ArrayList<Integer>();
    ArrayList<Integer> Player2 = new ArrayList<Integer>();

    void PlayGame(int selectedBlock, ImageView selectedImage){
        if(gameState == 1) {
            if (activePlayer == 1) {
                selectedImage.setImageResource(R.drawable.ic_cross);
                Player1.add(selectedBlock);
                activePlayer = 2;
                AutoPlay();
            }else if (activePlayer == 2) {
                selectedImage.setImageResource(R.drawable.ic_zero);
                Player2.add(selectedBlock);
                activePlayer = 1;
            }

            selectedImage.setEnabled(false);
            CheckWinner();
        }
    }


    void CheckWinner(){
        int winner = 0;

        /********* for Player 1 *********/
        if(Player1.contains(1) && Player1.contains(2) && Player1.contains(3)){ winner = 1; }
        if(Player1.contains(4) && Player1.contains(5) && Player1.contains(6)){ winner = 1; }
        if(Player1.contains(7) && Player1.contains(8) && Player1.contains(9)){ winner = 1; }

        if(Player1.contains(1) && Player1.contains(4) && Player1.contains(7)){ winner = 1; }
        if(Player1.contains(2) && Player1.contains(5) && Player1.contains(8)){ winner = 1; }
        if(Player1.contains(3) && Player1.contains(6) && Player1.contains(9)){ winner = 1; }

        if(Player1.contains(1) && Player1.contains(5) && Player1.contains(9)){ winner = 1; }
        if(Player1.contains(3) && Player1.contains(5) && Player1.contains(7)){ winner = 1; }


        /********* for Player 2 *********/
        if(Player2.contains(1) && Player2.contains(2) && Player2.contains(3)){ winner = 2; }
        if(Player2.contains(4) && Player2.contains(5) && Player2.contains(6)){ winner = 2; }
        if(Player2.contains(7) && Player2.contains(8) && Player2.contains(9)){ winner = 2; }

        if(Player2.contains(1) && Player2.contains(4) && Player2.contains(7)){ winner = 2; }
        if(Player2.contains(2) && Player2.contains(5) && Player2.contains(8)){ winner = 2; }
        if(Player2.contains(3) && Player2.contains(6) && Player2.contains(9)){ winner = 2; }

        if(Player2.contains(1) && Player2.contains(5) && Player2.contains(9)){ winner = 2; }
        if(Player2.contains(3) && Player2.contains(5) && Player2.contains(7)){ winner = 2; }



        if(winner != 0 && gameState == 1){
            if(winner == 1){
                player++;
                ShowAlert("You win!!");
                textViewPlayer1.setText("Human : " + player);
            }else if(winner == 2){
                ai++;
                ShowAlert("Bot Wins!!");
                textViewPlayer2.setText("Bot : " + ai);
            }
            gameState = 2;
        }
    }


   void AutoPlay(){
        ArrayList<Integer> emptyBlocks = new ArrayList<Integer>();

        for(int i=1; i<=9; i++){
            if(!(Player1.contains(i) || Player2.contains(i))){
                emptyBlocks.add(i);
            }
        }

        if(emptyBlocks.size() == 0) {
            CheckWinner();
            if(gameState == 1) {
                AlertDialog.Builder b = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                ShowAlert("Draw");
            }
            gameState = 3;
        } else {
            Random r = new Random();
            int randomIndex = r.nextInt(emptyBlocks.size());
            int selectedBock = emptyBlocks.get(randomIndex);


            ImageView selectedImage = (ImageView) findViewById(R.id.iv_11);
            switch (selectedBock) {
                case 1: selectedImage = (ImageView) findViewById(R.id.iv_11); break;
                case 2: selectedImage = (ImageView) findViewById(R.id.iv_12); break;
                case 3: selectedImage = (ImageView) findViewById(R.id.iv_13); break;

                case 4: selectedImage = (ImageView) findViewById(R.id.iv_21); break;
                case 5: selectedImage = (ImageView) findViewById(R.id.iv_22); break;
                case 6: selectedImage = (ImageView) findViewById(R.id.iv_23); break;

                case 7: selectedImage = (ImageView) findViewById(R.id.iv_31); break;
                case 8: selectedImage = (ImageView) findViewById(R.id.iv_32); break;
                case 9: selectedImage = (ImageView) findViewById(R.id.iv_33); break;
            }
            PlayGame(selectedBock, selectedImage);
        }
    }
/*
    int index()
    {
        int index=-1;
        for(int i=0;i<emptyBlocks.size();i++)
        {
            index = miniMax(emptyBlocks.get(i));
        }
        return index;
    }

    private int miniMax(Integer integer) {
        
        return integer;
    }
    */

    void ResetGame(){
        gameState = 1;
        activePlayer = 1;
        Player1 = new ArrayList<Integer>();
        Player2 = new ArrayList<Integer>();

        ImageView iv;
        iv = (ImageView) findViewById(R.id.iv_11); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_12); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_13); iv.setImageResource(0); iv.setEnabled(true);

        iv = (ImageView) findViewById(R.id.iv_21); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_22); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_23); iv.setImageResource(0); iv.setEnabled(true);

        iv = (ImageView) findViewById(R.id.iv_31); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_32); iv.setImageResource(0); iv.setEnabled(true);
        iv = (ImageView) findViewById(R.id.iv_33); iv.setImageResource(0); iv.setEnabled(true);

    }




    void ShowAlert(String Title){
        Toast.makeText(getApplicationContext(),Title,Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                ResetGame();
            }
        }, 1500);

    }
    @Override
    public void onBackPressed() {

        if(backPresssedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            startActivity(new Intent(this,SecondActivity.class));
            finish();
            return;
        }
        else
        {
            backToast= Toast.makeText(getApplicationContext(),"Press Back Again to Exit!!",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPresssedTime = System.currentTimeMillis();

    }
}
