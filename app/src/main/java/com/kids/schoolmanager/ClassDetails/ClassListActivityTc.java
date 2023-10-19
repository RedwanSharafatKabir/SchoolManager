package com.kids.schoolmanager.ClassDetails;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kids.schoolmanager.AppAction.TeacherMainActivity;
import com.kids.schoolmanager.BackFromFragment.BackListenerFragment;
import com.kids.schoolmanager.ModelClasses.StoreClassesData;
import com.kids.schoolmanager.R;
import com.kids.schoolmanager.RecyclerViewAdapters.ClassesCustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassListActivityTc extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    Parcelable recyclerViewState;
    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreClassesData> storeClassesDataArrayList;
    ClassesCustomAdapter classesCustomAdapter;
    LinearLayout createNewClassBtn;
    ProgressBar progressBar;
    DatabaseReference databaseReference, databaseReference2;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String userPhone, teacherUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_class_list_tc, container, false);

        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        progressBar = views.findViewById(R.id.classesListProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromClassesPageId);
        circleImageView.setOnClickListener(this);
        createNewClassBtn = views.findViewById(R.id.createNewClassBtnId);
        createNewClassBtn.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.classesRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeClassesDataArrayList = new ArrayList<StoreClassesData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Classes Information");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Teacher Information");

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

    private void loadClassList() {
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            databaseReference2.child(userPhone).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    teacherUserName = snapshot.getValue(String.class);

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            storeClassesDataArrayList.clear();
                            for (DataSnapshot item : dataSnapshot.getChildren()) {
                                try {
                                    String teacherName = item.child("classTeacherName").getValue().toString();

                                    if (teacherName.equals(teacherUserName)) {
                                        StoreClassesData storeClassesData = item.getValue(StoreClassesData.class);
                                        storeClassesDataArrayList.add(storeClassesData);
                                    }
                                } catch (Exception e) {
                                    Log.i("Error ", e.getMessage());
                                }
                            }

                            classesCustomAdapter = new ClassesCustomAdapter(getActivity(), storeClassesDataArrayList);
                            recyclerView.setAdapter(classesCustomAdapter);
                            classesCustomAdapter.notifyDataSetChanged();
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

        } else {
            Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
        }

        refresh(1000);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.createNewClassBtnId){
            CreateClassDialog createClassDialog = new CreateClassDialog();
            createClassDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }

        if(v.getId()==R.id.backFromClassesPageId){
            fragment = new TeacherMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
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
        fragment = new TeacherMainActivity();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}
