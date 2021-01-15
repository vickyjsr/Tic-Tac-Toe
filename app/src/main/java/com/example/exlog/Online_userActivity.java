package com.example.exlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class Online_userActivity extends AppCompatActivity {

    ListView lv_loginusers,lv_requestedusers;

    ArrayList<String> list_loginUsers = new ArrayList<String>();
    ArrayList<String> list_requestedUsers = new ArrayList<String>();

    TextView tvuserid,tvsendRequestid,tvAcceptRequestid;
    Button generateID,acceptedID;
    EditText etID;
    String uniqueID;
    private ImageButton back;
    private FirebaseAnalytics mFirebaseAnalytics;
    private long backPresssedTime;
    private Toast backToast;

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    String username;String login_uid,login_userid;
    DatabaseReference infoConnected = database.getReference("lastconnected");
    DatabaseReference lastConnected = database.getReference(".info/connected");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_user);

        back = (ImageButton)findViewById(R.id.back);
        tvuserid = (TextView)findViewById(R.id.tvLoginUser);

        generateID=(Button)findViewById(R.id.generateID);
        acceptedID = (Button)findViewById(R.id.id_toMulti);
        etID = (EditText)findViewById(R.id.editTextID);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        tvuserid = (TextView)findViewById(R.id.tvLoginUser);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        login_uid = user.getUid();
        if (user != null) {
            // Name, email address, and profile photo Url
             username = user.getDisplayName();


            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            login_userid = user.getUid();


            username = convertemailToUsername(user.getEmail());
            tvuserid.setText(username);
            username = username.replace(".", "");
            myRef.child("Users").child(username).child("Request").setValue(login_uid);
        }
        myRef.getRoot().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateLoginUsers(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        infoConnected.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                boolean connected = snapshot.getValue(Boolean.class);
//
//                if(connected)
//                {
//                    ////
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });





        generateID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uniqueID = givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect();
               AlertDialog.Builder b = new AlertDialog.Builder(Online_userActivity.this) ;
                b.setMessage(uniqueID);
                b.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied ID",uniqueID);
                        clipboard.setPrimaryClip(clip);
                    }
                });
                b.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                b.show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Online_userActivity.this,Multi_choose.class);
                startActivity(intent);
                finish();

            }
        });


    }


    @Override
    public void onBackPressed() {

        if(backPresssedTime + 2000 > System.currentTimeMillis())
        {
            backToast.cancel();
            startActivity(new Intent(this,Multi_choose.class));
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

    private String givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }



    void StartGame(String PlayerGameID, String OtherPlayer, String requestType){
        myRef.child("playing").child(PlayerGameID).removeValue();
        Intent intent = new Intent(getApplicationContext(), Multi_Online.class);
        intent.putExtra("player_session", PlayerGameID);
        intent.putExtra("user_name", username);
        intent.putExtra("other_player", OtherPlayer);
        intent.putExtra("login_uid", login_uid);
        intent.putExtra("request_type", requestType);
        startActivity(intent);
    }
    private void updateLoginUsers(DataSnapshot snapshot) {
        String key = "";
        Set<String> set = new HashSet<String>();
        Iterator i = snapshot.getChildren().iterator();

        while(i.hasNext())
        {
            key = ((DataSnapshot) i.next()).getKey();
            if(!key.equalsIgnoreCase(username))
            {
                set.add(key);
            }
        }
    }


    private String convertemailToUsername(String email) {

        String value = email.substring(0, email.indexOf('@'));
        value = value.replace(".", "");
        return value;
    }



}