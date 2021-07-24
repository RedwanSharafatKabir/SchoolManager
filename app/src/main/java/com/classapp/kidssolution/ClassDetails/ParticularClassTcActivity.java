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
import com.classapp.kidssolution.Attendance.AttendanceTcActivity;
import com.classapp.kidssolution.LiveChat.ChatTcActivity;
import com.classapp.kidssolution.NoteBookHW.NoteBookTcActivity;
import com.classapp.kidssolution.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularClassTcActivity extends Fragment implements View.OnClickListener{

    String classIdText, classNameText, classTeacherText;
    View views;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    CircleImageView circleImageView;
    TextView classNameTextView, teacherNameTextView;
    LinearLayout takeAttendance, giveNotebook, chatWithGuardian;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_particular_class_tc, container, false);

        classIdText = getArguments().getString("IdKey");
        classNameText = getArguments().getString("NameKey");
        classTeacherText = getArguments().getString("TeacherKey");

        circleImageView = views.findViewById(R.id.backFromParticularClassPageTcId);
        circleImageView.setOnClickListener(this);
        takeAttendance = views.findViewById(R.id.takeAttendanceId);
        takeAttendance.setOnClickListener(this);
        giveNotebook = views.findViewById(R.id.giveNotebookId);
        giveNotebook.setOnClickListener(this);
        chatWithGuardian = views.findViewById(R.id.chatWithGuardianId);
        chatWithGuardian.setOnClickListener(this);

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
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.takeAttendanceId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKey", classIdText);
            bundle.putString("NameKey", classNameText);
            bundle.putString("TeacherKey", classTeacherText);

            fragment = new AttendanceTcActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.giveNotebookId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKey", classIdText);
            bundle.putString("NameKey", classNameText);
            bundle.putString("TeacherKey", classTeacherText);

            fragment = new NoteBookTcActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.chatWithGuardianId){
            fragment = new ChatTcActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }
    }
}
