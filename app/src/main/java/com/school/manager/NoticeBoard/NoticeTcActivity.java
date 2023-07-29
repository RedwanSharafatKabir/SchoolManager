package com.school.manager.NoticeBoard;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.school.manager.AppAction.TeacherMainActivity;
import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ModelClasses.StoreNoticeData;
import com.school.manager.R;
import com.school.manager.RecyclerViewAdapters.NoticeCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NoticeTcActivity extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    Parcelable recyclerViewState;
    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreNoticeData> storeNoticeDataArrayList;
    NoticeCustomAdapter noticeCustomAdapter;
    ProgressBar progressBar;
    FloatingActionButton createNewNotebookBtn;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_notice_tc, container, false);

        progressBar = views.findViewById(R.id.noticeListProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromNoticeTcId);
        circleImageView.setOnClickListener(this);
        createNewNotebookBtn = views.findViewById(R.id.createNewNoticeBtnId);
        createNewNotebookBtn.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.noticeRecyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        storeNoticeDataArrayList = new ArrayList<StoreNoticeData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Notice Information");

        loadNoticeList();

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadNoticeList();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    private void loadNoticeList() {
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            // Retrieve unknown key
//            Query query = databaseReference.orderByKey();
//            query.addListenerForSingleValueEvent(new ValueEventListener() {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storeNoticeDataArrayList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        StoreNoticeData StoreNoticeData = item.getValue(StoreNoticeData.class);
                        storeNoticeDataArrayList.add(StoreNoticeData);
                    }

                    noticeCustomAdapter = new NoticeCustomAdapter(getActivity(), storeNoticeDataArrayList);
                    recyclerView.setAdapter(noticeCustomAdapter);
                    noticeCustomAdapter.notifyDataSetChanged();
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
        if(v.getId()==R.id.createNewNoticeBtnId){
            CreateNoticeDialog createNoticeDialog = new CreateNoticeDialog();
            createNoticeDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }

        if(v.getId()==R.id.backFromNoticeTcId){
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
