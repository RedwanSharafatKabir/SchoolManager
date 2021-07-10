package com.classapp.kidssolution.About_and_Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutInstituteTc extends Fragment implements View.OnClickListener{

    View views;
    CircleImageView circleImageView;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_about_institute_tc, container, false);

        circleImageView = views.findViewById(R.id.backFromAboutTcPageId);
        circleImageView.setOnClickListener(this);

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromAboutTcPageId){
            fragment = new TeacherMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }
    }
}
