package com.classapp.kidssolution.Attendance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.ClassesCustomAdapterGd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceGdActivity extends Fragment implements View.OnClickListener{

    View views;
    Date date = null;
    DatabaseReference databaseReference;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Fragment fragment;
    CircleImageView circleImageView;
    FragmentTransaction fragmentTransaction;
    TextView attendanceDate1, attendanceDate2, attendanceDate3, attendanceDate4, attendanceDate5, attendanceDate6, attendanceDate7;
    TextView attendanceDay1, attendanceDay2, attendanceDay3, attendanceDay4, attendanceDay5, attendanceDay6, attendanceDay7;
    ImageView attendanceCheck1, attendanceCheck2, attendanceCheck3, attendanceCheck4, attendanceCheck5, attendanceCheck6, attendanceCheck7;
    String prevDate, currentDate, classIdText, classNameText, classTeacherText, userPhoneNumber;
    String date1, date2, date3, date4, date5, date6, date7, present;
    SimpleDateFormat simpleDateFormat1, simpleDateFormat2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_attendance_gd, container, false);

        classIdText = getArguments().getString("IdKeyGd");
        classNameText = getArguments().getString("NameKeyGd");
        classTeacherText = getArguments().getString("TeacherKeyGd");

        circleImageView = views.findViewById(R.id.backFromAttendanceGdId);
        circleImageView.setOnClickListener(this);

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

        attendanceCheck1 = views.findViewById(R.id.presentOrAbsentId1);
        attendanceCheck1.setImageResource(R.drawable.absent);
        attendanceCheck2 = views.findViewById(R.id.presentOrAbsentId2);
        attendanceCheck2.setImageResource(R.drawable.absent);
        attendanceCheck3 = views.findViewById(R.id.presentOrAbsentId3);
        attendanceCheck3.setImageResource(R.drawable.absent);
        attendanceCheck4 = views.findViewById(R.id.presentOrAbsentId4);
        attendanceCheck4.setImageResource(R.drawable.absent);
        attendanceCheck5 = views.findViewById(R.id.presentOrAbsentId5);
        attendanceCheck5.setImageResource(R.drawable.absent);
        attendanceCheck6 = views.findViewById(R.id.presentOrAbsentId6);
        attendanceCheck6.setImageResource(R.drawable.absent);
        attendanceCheck7 = views.findViewById(R.id.presentOrAbsentId7);
        attendanceCheck7.setImageResource(R.drawable.absent);

        Date cal = Calendar.getInstance().getTime();
        simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = simpleDateFormat1.format(cal);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance Information");
        userPhoneNumber = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        checkAtdnc();
        setDateDays();

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkAtdnc();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    private void setDateDays(){
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
                date7 = prevDate;

            } else if(i==-1){
                attendanceDate6.setText(prevDate);
                attendanceDay6.setText(finalDay);
                date6 = prevDate;

            } else if(i==-2){
                attendanceDate5.setText(prevDate);
                attendanceDay5.setText(finalDay);
                date5 = prevDate;

            } else if(i==-3){
                attendanceDate4.setText(prevDate);
                attendanceDay4.setText(finalDay);
                date4 = prevDate;

            } else if(i==-4){
                attendanceDate3.setText(prevDate);
                attendanceDay3.setText(finalDay);
                date3 = prevDate;

            } else if(i==-5){
                attendanceDate2.setText(prevDate);
                attendanceDay2.setText(finalDay);
                date2 = prevDate;

            } else if(i==-6){
                attendanceDate1.setText(prevDate);
                attendanceDay1.setText(finalDay);
                date1 = prevDate;

            }
        }
    }

    private void checkAtdnc() {
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            Query query = databaseReference.child(classIdText).orderByKey();
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot item: dataSnapshot.getChildren()){
                        if (!dataSnapshot.hasChildren()) {
                            return;
                        }

                        String classDate = item.getKey();

                        if(classDate.equals(date7)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck7.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck7.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date6)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck6.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck6.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date5)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck5.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck5.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date4)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck4.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck4.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date3)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck3.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck3.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date2)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck2.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck2.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }

                        if(classDate.equals(date1)){
                            for(DataSnapshot childSnapShot :item.getChildren()){
                                if (!dataSnapshot.hasChildren()) {
                                    return;
                                }

                                String userPhone = childSnapShot.getKey();

                                if(userPhone.equals(userPhoneNumber)){
                                    String userPresent = childSnapShot.child("present").getValue(String.class);
                                    if(userPresent.equals("1")){
                                        attendanceCheck1.setImageResource(R.drawable.present);
                                    } else {
                                        attendanceCheck1.setImageResource(R.drawable.absent);
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            refresh(1000);
        } else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
// <<<<<<< HEAD
// =======
        if(v.getId()==R.id.submitAttendanceID){
            FirebaseDatabase.getInstance().getReference("Guardian Information").child(userPhoneNumber).child("username")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            userName = snapshot.getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });

            if(attendanceCheck1.isChecked()){
                present1 = "1";
                storeAttendanceDataMethod(present1, date1, userName);
            }
            if(attendanceCheck2.isChecked()){
                present2 = "1";
                storeAttendanceDataMethod(present2, date2, userName);
            }
            if(attendanceCheck3.isChecked()){
                present3 = "1";
                storeAttendanceDataMethod(present3, date3, userName);
            }
            if(attendanceCheck4.isChecked()){
                present4 = "1";
                storeAttendanceDataMethod(present4, date4, userName);
            }
            if(attendanceCheck5.isChecked()){
                present5 = "1";
                storeAttendanceDataMethod(present5, date5, userName);
            }
            if(attendanceCheck6.isChecked()){
                present6 = "1";
                storeAttendanceDataMethod(present6, date6, userName);
            }
            if(attendanceCheck7.isChecked()){
                present7 = "1";
                storeAttendanceDataMethod(present7, date7, userName);
            }
            
            Toast.makeText(getActivity(), "Attendance Submitted", Toast.LENGTH_SHORT).show();
        }

// >>>>>>> 58d923ef910df9b18a2bf095d7ee350e512908cc
        if(v.getId()==R.id.backFromAttendanceGdId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKeyGd", classIdText);
            bundle.putString("NameKeyGd", classNameText);
            bundle.putString("TeacherKeyGd", classTeacherText);

            fragment = new ParticularClassGdActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }
    }
}
