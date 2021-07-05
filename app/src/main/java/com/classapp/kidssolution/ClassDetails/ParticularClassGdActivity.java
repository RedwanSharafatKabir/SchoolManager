package com.classapp.kidssolution.ClassDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularClassGdActivity extends Fragment implements View.OnClickListener{

    View views;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    CircleImageView circleImageView;
    TextView classNameTextView, teacherNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_particular_class_gd, container, false);

        String classIdText = getArguments().getString("IdKeyGd");
        String classNameText = getArguments().getString("NameKeyGd");
        String classTeacherText = getArguments().getString("TeacherKeyGd");

        circleImageView = views.findViewById(R.id.backFromParticularClassPageGdId);
        circleImageView.setOnClickListener(this);

        classNameTextView = views.findViewById(R.id.classNameTextViewGdId);
        teacherNameTextView = views.findViewById(R.id.teacherNameTextViewGdId);

        classNameTextView.setText("Subject:" + classNameText);
        teacherNameTextView.setText("Moderator:" + classTeacherText);

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromParticularClassPageGdId){
            fragment = new ClassListActivityGd();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }
    }
}
