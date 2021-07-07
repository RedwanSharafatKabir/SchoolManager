package com.classapp.kidssolution.ClassDetails;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.ClassesCustomAdapter;
import com.classapp.kidssolution.RecyclerViewAdapters.ClassesCustomAdapterGd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassListActivityGd extends Fragment implements View.OnClickListener{

    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreGdClassesData> storeGdClassesDataArrayList;
    ClassesCustomAdapterGd classesCustomAdapterGd;
    ProgressBar progressBar;
    TextView noClass;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    LinearLayout joinNewClassBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_class_list_gd, container, false);

        joinNewClassBtn = views.findViewById(R.id.joinNewClassBtnId);
        joinNewClassBtn.setOnClickListener(this);
        progressBar = views.findViewById(R.id.classesListProgressbarGdId);
        progressBar.setVisibility(View.VISIBLE);

        noClass = views.findViewById(R.id.noClassIdGd);
        noClass.setVisibility(View.GONE);

        circleImageView = views.findViewById(R.id.backFromClassesPageGdId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.classesRecyclerViewGdId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeGdClassesDataArrayList = new ArrayList<StoreGdClassesData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes Information Guardian");

        loadClassList();

        if(progressBar.getVisibility()==View.VISIBLE){
            noClass.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadClassList();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    public void loadClassList() {
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            // Retrieve unknown key
//            Query query = databaseReference.orderByKey();
//            query.addListenerForSingleValueEvent(new ValueEventListener() {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storeGdClassesDataArrayList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        StoreGdClassesData storeGdClassesData = item.getValue(StoreGdClassesData.class);
                        storeGdClassesDataArrayList.add(storeGdClassesData);

                        Collections.reverse(storeGdClassesDataArrayList);
                        classesCustomAdapterGd = new ClassesCustomAdapterGd(getActivity(), storeGdClassesDataArrayList);
                        recyclerView.setAdapter(classesCustomAdapterGd);
                        classesCustomAdapterGd.notifyDataSetChanged();

                        noClass.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    noClass.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
        }

       refresh(1000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromClassesPageGdId){
            fragment = new GuardianMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.joinNewClassBtnId){
            JoinClassDialog joinClassDialog = new JoinClassDialog();
            joinClassDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }
    }
}
