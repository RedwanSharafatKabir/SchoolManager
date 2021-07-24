package com.classapp.kidssolution.LiveChat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.ModelClasses.StoreGuardianData;
import com.classapp.kidssolution.ModelClasses.StoreTeacherData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.ChatCustomAdapterGd;
import com.classapp.kidssolution.RecyclerViewAdapters.ChatCustomAdapterTc;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatGdActivity extends Fragment implements View.OnClickListener{

    Parcelable recyclerViewState;
    View views;
    TextView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreTeacherData> storeTeacherDataArrayList;
    ChatCustomAdapterGd chatCustomAdapterGd;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_chat_gd, container, false);

        progressBar = views.findViewById(R.id.chatListProgressbarGdId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromChatGdId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.chatRecyclerViewGdId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeTeacherDataArrayList = new ArrayList<StoreTeacherData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Information");

        loadTeacherList();

        return views;
    }

    private void loadTeacherList(){
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storeTeacherDataArrayList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        StoreTeacherData storeTeacherData = item.getValue(StoreTeacherData.class);
                        storeTeacherDataArrayList.add(storeTeacherData);
                    }

                    chatCustomAdapterGd = new ChatCustomAdapterGd(getActivity(), storeTeacherDataArrayList);
                    recyclerView.setAdapter(chatCustomAdapterGd);
                    chatCustomAdapterGd.notifyDataSetChanged();
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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromChatGdId){
            fragment = new GuardianMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment);
            fragmentTransaction.commit();
        }
    }
}
