package com.school.manager.AppAction;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.school.manager.About_and_Profile.AboutInstituteGd;
import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ClassDetails.ClassListActivityGd;
import com.school.manager.NoticeBoard.NoticeGdActivity;
import com.school.manager.R;

public class GuardianMainActivity extends Fragment implements View.OnClickListener, BackListenerFragment{

    public static BackListenerFragment backBtnListener;
    View views;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    LinearLayout homePage, classesPage, noticePage, helpPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_guardian_main, container, false);

        homePage = views.findViewById(R.id.homePageGdId);
        homePage.setOnClickListener(this);
        classesPage = views.findViewById(R.id.classesPageGdId);
        classesPage.setOnClickListener(this);
        noticePage = views.findViewById(R.id.noticePageGdId);
        noticePage.setOnClickListener(this);
        helpPage = views.findViewById(R.id.helpLinePageGdId);
        helpPage.setOnClickListener(this);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homePageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new AboutInstituteGd();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.classesPageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new ClassListActivityGd();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.noticePageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new NoticeGdActivity();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.helpLinePageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new HelpLineGd();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentGdID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        GuardianMainActivity myFragment = (GuardianMainActivity)getActivity().getSupportFragmentManager().findFragmentByTag("MY_FRAGMENT");
        if (myFragment != null && myFragment.isVisible()) {
            AlertDialog.Builder alertDialogBuilder;
            alertDialogBuilder = new AlertDialog.Builder(getActivity());
            alertDialogBuilder.setTitle("EXIT !");
            alertDialogBuilder.setMessage("Are you sure you want to close this app ?");
            alertDialogBuilder.setIcon(R.drawable.exit);
            alertDialogBuilder.setCancelable(false);

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
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
}
