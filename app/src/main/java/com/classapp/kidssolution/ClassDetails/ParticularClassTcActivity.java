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

public class ParticularClassTcActivity extends Fragment implements View.OnClickListener{

    View views;
    Fragment fragment;
    FragmentTransaction feedbackTransaction;
    CircleImageView circleImageView;
    TextView classNameTextView, teacherNameTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_particular_class_tc, container, false);

        String classIdText = getArguments().getString("IdKey");
        String classNameText = getArguments().getString("NameKey");
        String classTeacherText = getArguments().getString("TeacherKey");

        circleImageView = views.findViewById(R.id.backFromParticularClassPageTcId);
        circleImageView.setOnClickListener(this);

        classNameTextView = views.findViewById(R.id.classNameTextViewId);
        teacherNameTextView = views.findViewById(R.id.teacherNameTextViewId);

        classNameTextView.setText("Subject:" + classNameText);
        teacherNameTextView.setText("Moderator:" + classTeacherText);

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromParticularClassPageTcId){
            fragment = new ClassListActivityTc();
            feedbackTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            feedbackTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            feedbackTransaction.replace(R.id.fragmentID, fragment);
            feedbackTransaction.commit();
        }
    }
}
