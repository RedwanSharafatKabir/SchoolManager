package com.kids.schoolmanager.AppAction;

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
import com.kids.schoolmanager.About_and_Profile.AboutInstituteTc;
import com.kids.schoolmanager.BackFromFragment.BackListenerFragment;
import com.kids.schoolmanager.ClassDetails.ClassListActivityTc;
import com.kids.schoolmanager.NoticeBoard.NoticeTcActivity;
import com.kids.schoolmanager.R;

public class TeacherMainActivity extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    View views;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    LinearLayout homePage, classesPage, noticePage, helpPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_teacher_main, container, false);

        homePage = views.findViewById(R.id.homePageId);
        homePage.setOnClickListener(this);
        classesPage = views.findViewById(R.id.classesPageId);
        classesPage.setOnClickListener(this);
        noticePage = views.findViewById(R.id.noticePageId);
        noticePage.setOnClickListener(this);
        helpPage = views.findViewById(R.id.helpLinePageId);
        helpPage.setOnClickListener(this);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.homePageId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new AboutInstituteTc();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.classesPageId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new ClassListActivityTc();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.noticePageId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new NoticeTcActivity();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.helpLinePageId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new HelpLineTc();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentID, fragment);
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
        TeacherMainActivity myFragment = (TeacherMainActivity)getActivity().getSupportFragmentManager().findFragmentByTag("MY_FRAGMENT");
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
