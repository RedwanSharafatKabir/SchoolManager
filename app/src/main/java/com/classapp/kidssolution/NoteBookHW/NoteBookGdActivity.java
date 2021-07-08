package com.classapp.kidssolution.NoteBookHW;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.NotebookCustomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import de.hdodenhof.circleimageview.CircleImageView;

public class NoteBookGdActivity extends Fragment implements View.OnClickListener {

    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreNotebookData> storeNotebookDataArrayList;
    NotebookCustomAdapter notebookCustomAdapter;
    ProgressBar progressBar;
    TextView noHw;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classIdText, classNameText, classTeacherText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_note_book_gd, container, false);

        classIdText = getArguments().getString("IdKeyGd");
        classNameText = getArguments().getString("NameKeyGd");
        classTeacherText = getArguments().getString("TeacherKeyGd");

        progressBar = views.findViewById(R.id.notebookListProgressbarGdId);
        progressBar.setVisibility(View.VISIBLE);

        noHw = views.findViewById(R.id.noHwGdId);

        circleImageView = views.findViewById(R.id.backFromNotebookGdId);
        circleImageView.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.notebooksRecyclerViewGdId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        storeNotebookDataArrayList = new ArrayList<StoreNotebookData>();

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("Notebook Information");

        loadNotebookList();

        return views;
    }

    private void refresh(int milliSecond){
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loadNotebookList();
            }
        };

        handler.postDelayed(runnable, milliSecond);
    }

    private void loadNotebookList() {
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            // Retrieve unknown key
//            Query query = databaseReference.orderByKey();
//            query.addListenerForSingleValueEvent(new ValueEventListener() {

            databaseReference.child(classIdText).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    storeNotebookDataArrayList.clear();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        StoreNotebookData storeNotebookData = item.getValue(StoreNotebookData.class);
                        storeNotebookDataArrayList.add(storeNotebookData);

                        Collections.reverse(storeNotebookDataArrayList);
                        notebookCustomAdapter = new NotebookCustomAdapter(getActivity(), storeNotebookDataArrayList);
                        recyclerView.setAdapter(notebookCustomAdapter);
                        notebookCustomAdapter.notifyDataSetChanged();

                        noHw.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    noHw.setVisibility(View.VISIBLE);
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
        if(v.getId()==R.id.backFromNotebookGdId){
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
