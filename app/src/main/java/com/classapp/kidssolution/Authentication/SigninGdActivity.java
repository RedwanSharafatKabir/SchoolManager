package com.classapp.kidssolution.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.AppAction.MainActivity;
import com.classapp.kidssolution.AppAction.MainActivityGd;
import com.classapp.kidssolution.AppAction.SplashScreen;
import com.classapp.kidssolution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SigninGdActivity extends AppCompatActivity {

    View parentLayout;
    FirebaseAuth mAuth;
    ProgressDialog waitingDialog;
    EditText signinPhoneText, signinpasswordText;
    CheckBox checkBox;
    String emailObj, passObj, passedString = "I am guardian", phoneObj, phonenumber;
    boolean connection = false;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_gd);

        waitingDialog = new ProgressDialog(this);
        parentLayout = findViewById(android.R.id.content);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Guardian Information");

        checkBox = findViewById(R.id.rememberCheckBoxGdID);
        checkBox.setChecked(true);
        signinPhoneText = findViewById(R.id.signinPhoneGdID);
        signinpasswordText = findViewById(R.id.signinPasswordGdID);
    }

    public void loginCompleteBtnGd(View v){
        phoneObj = signinPhoneText.getText().toString();
        passObj = signinpasswordText.getText().toString();
        waitingDialog.setMessage("Logging in...");
        waitingDialog.show();

        if (phoneObj.isEmpty()) {
            signinPhoneText.setError("Please enter email address");
            waitingDialog.dismiss();
            return;
        }

        if (passObj.isEmpty()) {
            signinpasswordText.setError("Please enter phone number");
            waitingDialog.dismiss();
            return;
        }

        if(checkBox.isChecked()){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                connection = true;
            } else {
                connection = false;
                Toast.makeText(getApplicationContext(), "wifi or mobile data connection lost", Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }

            if(connection==true){
                rememberMethod(passedString);
                phonenumber = "+88" + phoneObj;
                databaseReference.child(phonenumber).child("email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            emailObj = snapshot.getValue(String.class);
                            mAuth.signInWithEmailAndPassword(emailObj, passObj).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        waitingDialog.dismiss();
                                        finish();
                                        Intent it = new Intent(SigninGdActivity.this, MainActivityGd.class);
                                        startActivity(it);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        signinPhoneText.setText("");
                                        signinpasswordText.setText("");
                                    } else {
                                        waitingDialog.dismiss();
                                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed\nError : " +
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                    }
                                }
                            });
                        } catch (Exception e){
                            signinPhoneText.setError("Wrong phone number !");
                            waitingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        waitingDialog.dismiss();
                    }
                });
            }
        }

        if(!checkBox.isChecked()) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                connection = true;
            } else {
                connection = false;
                Toast.makeText(getApplicationContext(), "Internet connection lost", Toast.LENGTH_SHORT).show();
                waitingDialog.dismiss();
            }

            if(connection==true){
                passedString = "";
                setNullDataMethod(passedString);
                phonenumber = "+88" + phoneObj;
                databaseReference.child(phonenumber).child("email").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            emailObj = snapshot.getValue(String.class);
                            mAuth.signInWithEmailAndPassword(emailObj, passObj).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        waitingDialog.dismiss();
                                        finish();
                                        Intent it = new Intent(SigninGdActivity.this, MainActivityGd.class);
                                        startActivity(it);
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        signinPhoneText.setText("");
                                        signinpasswordText.setText("");
                                    } else {
                                        waitingDialog.dismiss();
                                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed\nError : " +
                                                task.getException().getMessage(), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                    }
                                }
                            });
                        } catch (Exception e){
                            signinPhoneText.setError("Wrong phone number !");
                            waitingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        waitingDialog.dismiss();
                    }
                });
            }
        }
    }

    private void rememberMethod(String passedString){
        try {
            FileOutputStream fileOutputStream = openFileOutput("Guardian_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(passedString.getBytes());
            fileOutputStream.close();
            Snackbar.make(parentLayout, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNullDataMethod(String passedString){
        try {
            FileOutputStream fileOutputStream = openFileOutput("Guardian_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(passedString.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backToSplashScreen(View v){
        finish();
        Intent it = new Intent(SigninGdActivity.this, SplashScreen.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent it = new Intent(SigninGdActivity.this, SplashScreen.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
