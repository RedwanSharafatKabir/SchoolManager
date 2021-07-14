package com.classapp.kidssolution.Attendance;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.classapp.kidssolution.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AttendanceGdActivity extends Fragment implements View.OnClickListener{

    View views;
    TextView attendanceDate1, attendanceDate2, attendanceDate3, attendanceDate4, attendanceDate5, attendanceDate6, attendanceDate7;
    TextView attendanceDay1, attendanceDay2, attendanceDay3, attendanceDay4, attendanceDay5, attendanceDay6, attendanceDay7;
    TextView submitAttendance;
    CheckBox attendance1, attendance2, attendance3, attendance4, attendance5, attendance6, attendance7;
    String prevDate, currentDate;
    Date date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_attendance_gd, container, false);

        attendanceDate1 = views.findViewById(R.id.attendanceDateID1);
        attendanceDate2 = views.findViewById(R.id.attendanceDateID2);
        attendanceDate3 = views.findViewById(R.id.attendanceDateID3);
        attendanceDate4 = views.findViewById(R.id.attendanceDateID4);
        attendanceDate5 = views.findViewById(R.id.attendanceDateID5);
        attendanceDate6 = views.findViewById(R.id.attendanceDateID6);
        attendanceDate7 = views.findViewById(R.id.attendanceDateID7);

        attendanceDay1 = views.findViewById(R.id.attendanceDayID1);
        attendanceDay2 = views.findViewById(R.id.attendanceDayID2);
        attendanceDay3 = views.findViewById(R.id.attendanceDayID3);
        attendanceDay4 = views.findViewById(R.id.attendanceDayID4);
        attendanceDay5 = views.findViewById(R.id.attendanceDayID5);
        attendanceDay6 = views.findViewById(R.id.attendanceDayID6);
        attendanceDay7 = views.findViewById(R.id.attendanceDayID7);

        Date cal = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

        currentDate = simpleDateFormat1.format(cal);

        for(int i=0; i>=-6; i--) {
            try {
                date = simpleDateFormat1.parse(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, i);
            prevDate = simpleDateFormat1.format(calendar.getTime());

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            calendar2.add(Calendar.DATE, i);
            String input_date = simpleDateFormat2.format(calendar2.getTime());

            date = null;
            try {
                date = simpleDateFormat2.parse(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            DateFormat dayFormat = new SimpleDateFormat("EEEE");
            String finalDay = dayFormat.format(date);

            if(i==0){
                attendanceDate7.setText(prevDate);
                attendanceDay7.setText(finalDay);
            } else if(i==-1){
                attendanceDate6.setText(prevDate);
                attendanceDay6.setText(finalDay);
            } else if(i==-2){
                attendanceDate5.setText(prevDate);
                attendanceDay5.setText(finalDay);
            } else if(i==-3){
                attendanceDate4.setText(prevDate);
                attendanceDay4.setText(finalDay);
            } else if(i==-4){
                attendanceDate3.setText(prevDate);
                attendanceDay3.setText(finalDay);
            } else if(i==-5){
                attendanceDate2.setText(prevDate);
                attendanceDay2.setText(finalDay);
            } else if(i==-6){
                attendanceDate1.setText(prevDate);
                attendanceDay1.setText(finalDay);
            }
        }

        attendance1 = views.findViewById(R.id.attendanceCheckId1);
        attendance2 = views.findViewById(R.id.attendanceCheckId2);
        attendance3 = views.findViewById(R.id.attendanceCheckId3);
        attendance4 = views.findViewById(R.id.attendanceCheckId4);
        attendance5 = views.findViewById(R.id.attendanceCheckId5);
        attendance6 = views.findViewById(R.id.attendanceCheckId6);
        attendance7 = views.findViewById(R.id.attendanceCheckId7);

        submitAttendance = views.findViewById(R.id.attendanceDateID1);
        submitAttendance.setOnClickListener(this);

        return views;
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submitAttendanceID){

        }
    }
}
