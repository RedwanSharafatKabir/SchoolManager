package com.classapp.kidssolution.AppAction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.classapp.kidssolution.About_and_Profile.AboutInstitute;
import com.classapp.kidssolution.ClassDetails.ClassListActivityGd;
import com.classapp.kidssolution.ClassDetails.ClassListActivityTc;
import com.classapp.kidssolution.NoticeBoard.NoticeGdActivity;
import com.classapp.kidssolution.NoticeBoard.NoticeTcActivity;
import com.classapp.kidssolution.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GuardianMainActivity extends Fragment implements View.OnClickListener{

    View views;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    FragmentTransaction feedbackTransaction;
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
                fragment = new AboutInstitute();
                feedbackTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                feedbackTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                feedbackTransaction.replace(R.id.fragmentGdID, fragment);
                feedbackTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.classesPageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new ClassListActivityGd();
                feedbackTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                feedbackTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                feedbackTransaction.replace(R.id.fragmentGdID, fragment);
                feedbackTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.noticePageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new NoticeGdActivity();
                feedbackTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                feedbackTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                feedbackTransaction.replace(R.id.fragmentGdID, fragment);
                feedbackTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }

        if(v.getId()==R.id.helpLinePageGdId){
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                fragment = new HelpLine();
                feedbackTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                feedbackTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                feedbackTransaction.replace(R.id.fragmentGdID, fragment);
                feedbackTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
