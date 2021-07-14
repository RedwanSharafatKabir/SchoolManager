package com.classapp.kidssolution.Attendance;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AttendanceTcActivity extends Fragment {

    View views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_attendance_tc, container, false);

//        progressBar = views.findViewById(R.id.classesListProgressbarId);
//        progressBar.setVisibility(View.VISIBLE);
//
//        noClass = views.findViewById(R.id.noClassId);
//
//        circleImageView = views.findViewById(R.id.backFromClassesPageId);
//        circleImageView.setOnClickListener(this);
//        createNewClassBtn = views.findViewById(R.id.createNewClassBtnId);
//        createNewClassBtn.setOnClickListener(this);
//
//        recyclerView = views.findViewById(R.id.classesRecyclerViewId);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
//            }
//        });
//
//        storeClassesDataArrayList = new ArrayList<StoreClassesData>();
//
//        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//        netInfo = cm.getActiveNetworkInfo();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Classes Information");
//
//        loadClassList();

        return views;
    }
}