package com.school.manager.Attendance;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ClassDetails.ParticularClassTcActivity;
import com.school.manager.ModelClasses.StoreAttendanceData;
import com.school.manager.R;
import com.school.manager.RecyclerViewAdapters.AttendanceSheetAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AttendanceSheet extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    View views;
    Parcelable recyclerViewState;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<StoreAttendanceData> storeAttendanceDataArrayList;
    AttendanceSheetAdapter attendanceSheetAdapter;
    ProgressBar progressBar;
    TextView textView;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classIdText, classNameText, classTeacherText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_attendance_sheet, container, false);

        classIdText = getArguments().getString("IdKey");
        classNameText = getArguments().getString("NameKey");
        classTeacherText = getArguments().getString("TeacherKey");

        textView = views.findViewById(R.id.classNameId);
        textView.setText("Attendance Sheet of\n" + classNameText);

        progressBar = views.findViewById(R.id.atdncShtProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromAtdncShtId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.atdncShtRecyclerViewId);
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

        loadDataList();

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadDataList();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    private void loadDataList(){
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            try {
                databaseReference.child(classIdText).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        storeAttendanceDataArrayList.clear();

                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            for (DataSnapshot item1 : item.getChildren()) {
                                StoreAttendanceData storeAttendanceData = item1.getValue(StoreAttendanceData.class);
                                storeAttendanceDataArrayList.add(storeAttendanceData);
                            }
                        }

                        attendanceSheetAdapter = new AttendanceSheetAdapter(getActivity(), storeAttendanceDataArrayList);
                        recyclerView.setAdapter(attendanceSheetAdapter);
                        attendanceSheetAdapter.notifyDataSetChanged();
                        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } catch (Exception e){
                Log.i("Exception_db ", e.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        }

        else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }

        refresh(1000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromAtdncShtId){
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
