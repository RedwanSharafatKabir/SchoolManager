package com.school.manager.LiveChat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.school.manager.AppAction.TeacherMainActivity;
import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ModelClasses.StoreGdClassesData;
import com.school.manager.R;
import com.school.manager.RecyclerViewAdapters.ChatCustomAdapterTc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatTcActivity extends Fragment implements View.OnClickListener, BackListenerFragment {

    View views;
    public static BackListenerFragment backBtnListener;
    Parcelable recyclerViewState;
    TextView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreGdClassesData> storeGdClassesDataArrayList;
    ChatCustomAdapterTc chatCustomAdapterTc;
    ProgressBar progressBar;
    DatabaseReference databaseReference1, databaseReference2;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String guardianPhone, userPhone, teacherName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_chat_tc, container, false);

        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        progressBar = views.findViewById(R.id.chatListProgressbarTcId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromChatTcId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.chatRecyclerViewTcId);
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
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Teacher Information");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("Classes Information Guardian");

        loadGuardianList();

        return views;
    }

    private void loadGuardianList(){
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            databaseReference1.child(userPhone).child("username").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    teacherName = snapshot.getValue(String.class);

                    databaseReference2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            storeGdClassesDataArrayList.clear();

                            for (DataSnapshot item : snapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot : item.getChildren()) {

                                    try {
                                        String teacherUserName = dataSnapshot.child("classTeacherNameGd").getValue().toString();

                                        if(teacherName.equals(teacherUserName)){
                                            StoreGdClassesData storeGdClassesData = dataSnapshot.getValue(StoreGdClassesData.class);
                                            storeGdClassesDataArrayList.add(storeGdClassesData);
                                        }

                                    } catch (Exception e) {
                                        Log.i("Error_Class_Data ", e.getMessage());
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }

                                chatCustomAdapterTc = new ChatCustomAdapterTc(getActivity(), storeGdClassesDataArrayList);
                                recyclerView.setAdapter(chatCustomAdapterTc);
                                chatCustomAdapterTc.notifyDataSetChanged();
                                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromChatTcId){
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
