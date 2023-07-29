package com.school.manager.ClassDetails;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.school.manager.AppAction.GuardianMainActivity;
import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ModelClasses.StoreGdClassesData;
import com.school.manager.R;
import com.school.manager.RecyclerViewAdapters.ClassesCustomAdapterGd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassListActivityGd extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    String userPhone;
    Parcelable recyclerViewState;
    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreGdClassesData> storeGdClassesDataArrayList;
    ClassesCustomAdapterGd classesCustomAdapterGd;
    ProgressBar progressBar;
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

        circleImageView = views.findViewById(R.id.backFromClassesPageGdId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.classesRecyclerViewGdId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeGdClassesDataArrayList = new ArrayList<StoreGdClassesData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes Information Guardian");
        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        loadClassList();

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
                        String temp = item.getKey();

                        if(temp.equals(userPhone)){
                            for (DataSnapshot item1 : item.getChildren()) {
                                StoreGdClassesData storeGdClassesData = item1.getValue(StoreGdClassesData.class);
                                storeGdClassesDataArrayList.add(storeGdClassesData);
                            }
                        }
                    }

                    classesCustomAdapterGd = new ClassesCustomAdapterGd(getActivity(), storeGdClassesDataArrayList);
                    recyclerView.setAdapter(classesCustomAdapterGd);
                    classesCustomAdapterGd.notifyDataSetChanged();
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
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
            fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.joinNewClassBtnId){
            JoinClassDialog joinClassDialog = new JoinClassDialog();
            joinClassDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
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
        fragment = new GuardianMainActivity();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}
