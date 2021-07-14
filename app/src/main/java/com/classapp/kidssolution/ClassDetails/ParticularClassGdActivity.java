package com.classapp.kidssolution.ClassDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.Attendance.AttendanceGdActivity;
import com.classapp.kidssolution.NoteBookHW.NoteBookGdActivity;
import com.classapp.kidssolution.NoteBookHW.NoteBookTcActivity;
import com.classapp.kidssolution.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularClassGdActivity extends Fragment implements View.OnClickListener{

    View views;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    CircleImageView circleImageView;
    TextView classNameTextView, teacherNameTextView;
    LinearLayout giveAttendance, seeNotebook, chatWithTeacher;
    String classIdText, classNameText, classTeacherText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_particular_class_gd, container, false);

        classIdText = getArguments().getString("IdKeyGd");
        classNameText = getArguments().getString("NameKeyGd");
        classTeacherText = getArguments().getString("TeacherKeyGd");

        circleImageView = views.findViewById(R.id.backFromParticularClassPageGdId);
        circleImageView.setOnClickListener(this);
        giveAttendance = views.findViewById(R.id.giveAttendanceId);
        giveAttendance.setOnClickListener(this);
        seeNotebook = views.findViewById(R.id.seeNotebookId);
        seeNotebook.setOnClickListener(this);
        chatWithTeacher = views.findViewById(R.id.chatWithTeacherId);
        chatWithTeacher.setOnClickListener(this);

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

        if(v.getId()==R.id.giveAttendanceId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKeyGd", classIdText);
            bundle.putString("NameKeyGd", classNameText);
            bundle.putString("TeacherKeyGd", classTeacherText);

            fragment = new AttendanceGdActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.seeNotebookId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKeyGd", classIdText);
            bundle.putString("NameKeyGd", classNameText);
            bundle.putString("TeacherKeyGd", classTeacherText);

            fragment = new NoteBookGdActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.chatWithTeacherId){
//            fragment = new ChatWithTeacher();
//            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
//            fragmentTransaction.commit();
        }
    }
}
