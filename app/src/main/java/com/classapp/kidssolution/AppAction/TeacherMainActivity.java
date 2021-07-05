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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.classapp.kidssolution.About_and_Profile.AboutInstituteTc;
import com.classapp.kidssolution.ClassDetails.ClassListActivityTc;
import com.classapp.kidssolution.NoticeBoard.NoticeTcActivity;
import com.classapp.kidssolution.R;

public class TeacherMainActivity extends Fragment implements View.OnClickListener{

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
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentID, fragment);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
