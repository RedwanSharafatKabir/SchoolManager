package com.classapp.kidssolution.Attendance;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.BackFromFragment.BackListenerFragment;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.AttendanceCustomAdapterTc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceTcActivity extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    View views;
    Parcelable recyclerViewState;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<StoreAttendanceData> storeAttendanceDataArrayList;
    AttendanceCustomAdapterTc attendanceCustomAdapterTc;
    ProgressBar progressBar;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    SimpleDateFormat simpleDateFormat;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classIdText, classNameText, classTeacherText, currentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_attendance_tc, container, false);

        classIdText = getArguments().getString("IdKey");
        classNameText = getArguments().getString("NameKey");
        classTeacherText = getArguments().getString("TeacherKey");

        Date cal = Calendar.getInstance().getTime();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        currentDate = simpleDateFormat.format(cal);

        progressBar = views.findViewById(R.id.atdncProgressbarTcId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromAtdncTcId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.attendanceRecyclerViewTcId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeAttendanceDataArrayList = new ArrayList<StoreAttendanceData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Attendance Information");

        loadPresentStudentList();

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadPresentStudentList();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    private void loadPresentStudentList(){
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            databaseReference.child(classIdText).child(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    try {
                        Log.i("User_data ", snapshot.getValue().toString());
                        databaseReference.child(classIdText).child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                storeAttendanceDataArrayList.clear();
                                for (DataSnapshot item : dataSnapshot.getChildren()) {
                                    StoreAttendanceData storeAttendanceData = item.getValue(StoreAttendanceData.class);
                                    storeAttendanceDataArrayList.add(storeAttendanceData);
                                }

                                attendanceCustomAdapterTc = new AttendanceCustomAdapterTc(getActivity(), storeAttendanceDataArrayList, classIdText);
                                recyclerView.setAdapter(attendanceCustomAdapterTc);
                                attendanceCustomAdapterTc.notifyDataSetChanged();
                                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    } catch (Exception e){
                        Log.i("Exception_CurrentDate ", e.getMessage());
                        getOtherDatesData();
                    }

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
        }

        refresh(1000);
    }

    private void getOtherDatesData(){
        databaseReference.child(classIdText).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot item: snapshot.getChildren()) {
                    databaseReference.child(classIdText).child(item.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            storeAttendanceDataArrayList.clear();
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                StoreAttendanceData storeAttendanceData = item.getValue(StoreAttendanceData.class);
                                storeAttendanceDataArrayList.add(storeAttendanceData);
                            }

                            attendanceCustomAdapterTc = new AttendanceCustomAdapterTc(getActivity(), storeAttendanceDataArrayList, classIdText);
                            recyclerView.setAdapter(attendanceCustomAdapterTc);
                            attendanceCustomAdapterTc.notifyDataSetChanged();
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromAtdncTcId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKey", classIdText);
            bundle.putString("NameKey", classNameText);
            bundle.putString("TeacherKey", classTeacherText);

            fragment = new ParticularClassTcActivity();
            fragment.setArguments(bundle);
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
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
        Bundle bundle = new Bundle();
        bundle.putString("IdKey", classIdText);
        bundle.putString("NameKey", classNameText);
        bundle.putString("TeacherKey", classTeacherText);

        fragment = new ParticularClassTcActivity();
        fragment.setArguments(bundle);
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment);
        fragmentTransaction.commit();
    }
}
