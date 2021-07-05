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
import android.view.MenuItem;
import android.view.View;

import com.classapp.kidssolution.NoticeBoard.NoticeTcActivity;
import com.classapp.kidssolution.About_and_Profile.ProfileTcActivity;
import com.classapp.kidssolution.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener{

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
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        parentLayout = findViewById(android.R.id.content);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        switch (id){
            case R.id.homeGdID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new TeacherMainActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
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

            case R.id.noticeID:
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    fragment = new NoticeTcActivity();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
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
                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
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
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("EXIT !");
        alertDialogBuilder.setMessage("Are you sure you want to close this app ?");
        alertDialogBuilder.setIcon(R.drawable.exit);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
