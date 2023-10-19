package com.kids.schoolmanager.AppAction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.kids.schoolmanager.About_and_Profile.AboutInstituteGd;
import com.kids.schoolmanager.About_and_Profile.AboutInstituteTc;
import com.kids.schoolmanager.About_and_Profile.ProfileGdActivity;
import com.kids.schoolmanager.Attendance.AttendanceGdActivity;
import com.kids.schoolmanager.Attendance.AttendanceSheet;
import com.kids.schoolmanager.Attendance.AttendanceTcActivity;
import com.kids.schoolmanager.ClassDetails.ClassListActivityGd;
import com.kids.schoolmanager.ClassDetails.ClassListActivityTc;
import com.kids.schoolmanager.ClassDetails.ParticularClassGdActivity;
import com.kids.schoolmanager.ClassDetails.ParticularClassTcActivity;
import com.kids.schoolmanager.LiveChat.ChatGdActivity;
import com.kids.schoolmanager.LiveChat.ChatTcActivity;
import com.kids.schoolmanager.NoteBookHW.NoteBookGdActivity;
import com.kids.schoolmanager.NoteBookHW.NoteBookTcActivity;
import com.kids.schoolmanager.NoticeBoard.NoticeGdActivity;
import com.kids.schoolmanager.NoticeBoard.NoticeTcActivity;
import com.kids.schoolmanager.About_and_Profile.ProfileTcActivity;
import com.kids.schoolmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

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
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationID);
        bottomNavigationView.setOnItemSelectedListener(this);

        parentLayout = findViewById(android.R.id.content);

        fragment = new TeacherMainActivity();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();

        try {
            switch (getIntent().getStringExtra("EXTRA")) {
                case "openChatFragment":
                    fragment = new ChatTcActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
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
            case R.id.homeID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new TeacherMainActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.chatID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new ChatTcActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.helpID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new HelpLineTc();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;

            case R.id.profileID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new ProfileTcActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentID, fragment);
                    fragmentTransaction.commit();
                } else {
                    snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.Red));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
                    snackbar.setDuration(10000).show();
                }
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
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

        if(AttendanceSheet.backBtnListener!=null){
            AttendanceSheet.backBtnListener.onBackPressed();
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

        ///

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
    }
}
