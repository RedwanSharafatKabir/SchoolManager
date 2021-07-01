package com.classapp.kidssolution.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.MainActivity;
import com.classapp.kidssolution.AppAction.SplashScreen;
import com.classapp.kidssolution.ModelClasses.StoreGuardianData;
import com.classapp.kidssolution.ModelClasses.StoreTeacherData;
import com.classapp.kidssolution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity{

    View parentLayout;
    EditText signupEmailText, signupUsernameText, signupPasswordText, signupPhoneText;
    ImageButton signupButton;
    ProgressDialog waitingDialog;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    FirebaseAuth mAuth;
    DatabaseReference databaseReferenceForTeacher, databaseReferenceForGuardian;
    RadioButton signUpTc, signUpGd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        waitingDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReferenceForTeacher = FirebaseDatabase.getInstance().getReference("Teacher Information");
        databaseReferenceForGuardian = FirebaseDatabase.getInstance().getReference("Guardian Information");
        parentLayout = findViewById(android.R.id.content);

        signUpTc = findViewById(R.id.signUpAsTcId);
        signUpGd = findViewById(R.id.signUpAsGdId);
        signupEmailText = findViewById(R.id.signupEmailID);
        signupUsernameText = findViewById(R.id.signupUsernameID);
        signupPhoneText = findViewById(R.id.signupPhoneID);
        signupPasswordText = findViewById(R.id.signupPasswordID);
        signupButton = findViewById(R.id.SignupID);

    }

    public void registerCompleteBtn(View v){
        final String email = signupEmailText.getText().toString();
        final String username = signupUsernameText.getText().toString();
        final String phone = signupPhoneText.getText().toString();
        final String password = signupPasswordText.getText().toString();
        final String phonenumber = "+88" + phone;

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        waitingDialog.setMessage("Signing up...");
        waitingDialog.show();

        if (email.isEmpty()) {
            waitingDialog.dismiss();
            signupEmailText.setError("Please enter email address");
            return;
        }

        if (username.isEmpty()) {
            waitingDialog.dismiss();
            signupUsernameText.setError("Please enter username");
            return;
        }

        if (phone.isEmpty()) {
            waitingDialog.dismiss();
            signupPhoneText.setError("Please enter your contact number");
            return;
        }

        if (password.isEmpty()) {
            waitingDialog.dismiss();
            signupPasswordText.setError("Please enter password");
            return;
        }

        if (password.length() < 8) {
            waitingDialog.dismiss();
            signupPasswordText.setError("Password must be at least 8 characters");
            return;
        }

        if((phone.length() < 11) || phone.length() > 11) {
            waitingDialog.dismiss();
            signupPhoneText.setError("Invalid phone number");
        }

        if(!signUpGd.isChecked() && !signUpTc.isChecked()){
            waitingDialog.dismiss();
            Toast.makeText(this, "Choose teacher or guardian", Toast.LENGTH_LONG).show();
        }

        else {
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                if(signUpTc.isChecked()){
                    signupTcWithEmail(email, username, phonenumber, password);
                } else if(signUpGd.isChecked()){
                    signupGdWithEmail(email, username, phonenumber, password);
                }

                signupEmailText.setText("");
                signupUsernameText.setText("");
                signupPhoneText.setText("");
                signupPasswordText.setText("");
                signUpTc.setChecked(false);
                signUpGd.setChecked(false);
            } else {
                waitingDialog.dismiss();
                Snackbar snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.Red));
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                snackbar.setDuration(10000).show();
            }
        }
    }

    private void signupTcWithEmail(String email, String username, String phonenumber, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    storeTeacherDataMethod(email, username, phonenumber);
                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
                    waitingDialog.dismiss();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                Intent it = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                waitingDialog.dismiss();
                                Toast t = Toast.makeText(getApplicationContext(), "Authentication failed\nError : " +
                                        task.getException().getMessage(), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                            }
                        }
                    });

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), R.string.email_alert,
                                Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed. Error : "
                                + "Connection lost.", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            }
        });
    }

    private void signupGdWithEmail(String email, String username, String phonenumber, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    storeGuardianDataMethod(email, username, phonenumber);
                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
                    waitingDialog.dismiss();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                finish();
                                Intent it = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(it);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            } else {
                                waitingDialog.dismiss();
                                Toast t = Toast.makeText(getApplicationContext(), "Authentication failed\nError : " +
                                        task.getException().getMessage(), Toast.LENGTH_LONG);
                                t.setGravity(Gravity.CENTER, 0, 0);
                                t.show();
                            }
                        }
                    });

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), R.string.email_alert,
                                Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    } else {
                        waitingDialog.dismiss();
                        Toast t = Toast.makeText(getApplicationContext(), "Authentication failed. Error : "
                                + "Connection lost.", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            }
        });
    }

    private void storeTeacherDataMethod(String email, String username, String phone){
        String displayname = phone;
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            UserProfileChangeRequest profile;
            profile= new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();
            user.updateProfile(profile).addOnCompleteListener(task -> {});
        }

        String Key_User_Info = phone;
        StoreTeacherData storeTeacherData = new StoreTeacherData(email, username, phone);
        databaseReferenceForTeacher.child(Key_User_Info).setValue(storeTeacherData);
    }

    private void storeGuardianDataMethod(String email, String username, String phone) {
        String displayname = phone;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder().setDisplayName(displayname).build();
            user.updateProfile(profile).addOnCompleteListener(task -> {
            });
        }

        String Key_User_Info = phone;
        StoreGuardianData storeGuardianData = new StoreGuardianData(email, username, phone);
        databaseReferenceForGuardian.child(Key_User_Info).setValue(storeGuardianData);
    }

    public void backToSplashScreen(View v){
        finish();
        Intent it = new Intent(RegisterActivity.this, SplashScreen.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent it = new Intent(RegisterActivity.this, SplashScreen.class);
        startActivity(it);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onBackPressed();
    }
}
