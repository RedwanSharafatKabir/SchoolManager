package com.classapp.kidssolution.AppAction;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.classapp.kidssolution.Authentication.RegisterActivity;
import com.classapp.kidssolution.Authentication.SigninGdActivity;
import com.classapp.kidssolution.Authentication.SigninTcActivity;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashScreen extends AppCompatActivity implements View.OnClickListener{

    TextView signInTcButton, signInGdButton, signUpPageButton;
    FirebaseUser firebaseUser = null;
    String passedStringTc = "", passedStringGd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        signInTcButton = findViewById(R.id.signInTcPageID);
        signInTcButton.setOnClickListener(this);
        signInGdButton = findViewById(R.id.signInGdPageID);
        signInGdButton.setOnClickListener(this);
        signUpPageButton = findViewById(R.id.signUpPageID);
        signUpPageButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        rememberMeTcMethod();
        rememberMeGdMethod();

        if (firebaseUser != null && !passedStringTc.isEmpty()) {
            finish();
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
        }

        if (firebaseUser != null && !passedStringGd.isEmpty()) {
            finish();
            Intent it = new Intent(getApplicationContext(), MainActivityGd.class);
            startActivity(it);
        }
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.signInTcPageID){
            finish();
            Intent it = new Intent(SplashScreen.this, SigninTcActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if(v.getId()==R.id.signInGdPageID){
            finish();
            Intent it = new Intent(SplashScreen.this, SigninGdActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }

        if(v.getId()==R.id.signUpPageID){
            finish();
            Intent it = new Intent(SplashScreen.this, RegisterActivity.class);
            startActivity(it);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    private void rememberMeTcMethod(){
        try {
            String recievedMessageTc;
            FileInputStream fileInputStreamTc = openFileInput("Teacher_Info.txt");
            InputStreamReader inputStreamReaderTc = new InputStreamReader(fileInputStreamTc);
            BufferedReader bufferedReaderTc = new BufferedReader(inputStreamReaderTc);
            StringBuffer stringBufferTc = new StringBuffer();

            while((recievedMessageTc=bufferedReaderTc.readLine())!=null){
                stringBufferTc.append(recievedMessageTc);
            }
            passedStringTc = stringBufferTc.toString();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void rememberMeGdMethod(){
        try {
            String recievedMessageGd;
            FileInputStream fileInputStreamGd = openFileInput("Guardian_Info.txt");
            InputStreamReader inputStreamReaderGd = new InputStreamReader(fileInputStreamGd);
            BufferedReader bufferedReaderGd = new BufferedReader(inputStreamReaderGd);
            StringBuffer stringBufferGd = new StringBuffer();

            while((recievedMessageGd=bufferedReaderGd.readLine())!=null){
                stringBufferGd.append(recievedMessageGd);
            }

            passedStringGd = stringBufferGd.toString();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.alert_title);
        alertDialogBuilder.setMessage(R.string.alert_message);
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
