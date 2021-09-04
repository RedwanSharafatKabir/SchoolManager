package com.classapp.kidssolution.AppAction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.classapp.kidssolution.About_and_Profile.AboutInstituteGd;
import com.classapp.kidssolution.About_and_Profile.AboutInstituteTc;
import com.classapp.kidssolution.About_and_Profile.ProfileGdActivity;
import com.classapp.kidssolution.Attendance.AttendanceGdActivity;
import com.classapp.kidssolution.Attendance.AttendanceSheet;
import com.classapp.kidssolution.Attendance.AttendanceTcActivity;
import com.classapp.kidssolution.ClassDetails.ClassListActivityGd;
import com.classapp.kidssolution.ClassDetails.ClassListActivityTc;
import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.LiveChat.ChatGdActivity;
import com.classapp.kidssolution.LiveChat.ChatTcActivity;
import com.classapp.kidssolution.NoteBookHW.NoteBookGdActivity;
import com.classapp.kidssolution.NoteBookHW.NoteBookTcActivity;
import com.classapp.kidssolution.NoticeBoard.NoticeGdActivity;
import com.classapp.kidssolution.NoticeBoard.NoticeTcActivity;
import com.classapp.kidssolution.About_and_Profile.ProfileTcActivity;
import com.classapp.kidssolution.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivityGd extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    View parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    Snackbar snackbar;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gd);

        bottomNavigationView = findViewById(R.id.bottomNavigationGdID);
        bottomNavigationView.setOnItemSelectedListener(this);

        parentLayout = findViewById(android.R.id.content);

        fragment = new GuardianMainActivity();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();

        try {
            switch (getIntent().getStringExtra("EXTRA")) {
                case "openChatFragment":
                    fragment = new ChatGdActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                    fragmentTransaction.commit();
                    break;
            }
        } catch (Exception e){
            Log.i("Error ", e.getMessage());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        switch (id){
            case R.id.homeGdID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new GuardianMainActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivityGd.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.chatGdID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new ChatGdActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivityGd.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.helpGdID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new HelpLineGd();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivityGd.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.profileGdID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new ProfileGdActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivityGd.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(GuardianMainActivity.backBtnListener!=null){
            GuardianMainActivity.backBtnListener.onBackPressed();
        }

        if(AboutInstituteGd.backBtnListener!=null){
            AboutInstituteGd.backBtnListener.onBackPressed();
        }

        if(ProfileGdActivity.backBtnListener!=null){
            ProfileGdActivity.backBtnListener.onBackPressed();
        }

        if(HelpLineGd.backBtnListener!=null){
            HelpLineGd.backBtnListener.onBackPressed();
        }

        if(AttendanceGdActivity.backBtnListener!=null){
            AttendanceGdActivity.backBtnListener.onBackPressed();
        }

        if(ClassListActivityGd.backBtnListener!=null){
            ClassListActivityGd.backBtnListener.onBackPressed();
        }

        if(ParticularClassGdActivity.backBtnListener!=null){
            ParticularClassGdActivity.backBtnListener.onBackPressed();
        }

        if(ChatGdActivity.backBtnListener!=null){
            ChatGdActivity.backBtnListener.onBackPressed();
        }

        if(NoticeGdActivity.backBtnListener!=null){
            NoticeGdActivity.backBtnListener.onBackPressed();
        }

        if(NoteBookGdActivity.backBtnListener!=null){
            NoteBookGdActivity.backBtnListener.onBackPressed();
        }

        ///

        if(TeacherMainActivity.backBtnListener!=null){
            TeacherMainActivity.backBtnListener.onBackPressed();
        }

        if(AboutInstituteTc.backBtnListener!=null){
            AboutInstituteTc.backBtnListener.onBackPressed();
        }

        if(ProfileTcActivity.backBtnListener!=null){
            ProfileTcActivity.backBtnListener.onBackPressed();
        }

        if(HelpLineTc.backBtnListener!=null){
            HelpLineTc.backBtnListener.onBackPressed();
        }

        if(AttendanceTcActivity.backBtnListener!=null){
            AttendanceTcActivity.backBtnListener.onBackPressed();
        }

        if(ClassListActivityTc.backBtnListener!=null){
            ClassListActivityTc.backBtnListener.onBackPressed();
        }

        if(ParticularClassTcActivity.backBtnListener!=null){
            ParticularClassTcActivity.backBtnListener.onBackPressed();
        }

        if(ChatTcActivity.backBtnListener!=null){
            ChatTcActivity.backBtnListener.onBackPressed();
        }

        if(NoticeTcActivity.backBtnListener!=null){
            NoticeTcActivity.backBtnListener.onBackPressed();
        }

        if(NoteBookTcActivity.backBtnListener!=null){
            NoteBookTcActivity.backBtnListener.onBackPressed();
        }

        if(AttendanceSheet.backBtnListener!=null){
            AttendanceSheet.backBtnListener.onBackPressed();
        }
    }
}
