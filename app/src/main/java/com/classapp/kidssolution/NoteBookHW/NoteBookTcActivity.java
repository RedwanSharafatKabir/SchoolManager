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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.ClassDetails.CreateClassDialog;
import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.ClassesCustomAdapter;
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

public class NoteBookTcActivity extends Fragment implements View.OnClickListener {

    View views;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    ArrayList<StoreNotebookData> storeNotebookDataArrayList;
    NotebookCustomAdapter notebookCustomAdapter;
    ProgressBar progressBar;
    FloatingActionButton createNewNotebookBtn;
    TextView noHw;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classIdText, classNameText, classTeacherText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_note_book_tc, container, false);

        classIdText = getArguments().getString("IdKey");
        classNameText = getArguments().getString("NameKey");
        classTeacherText = getArguments().getString("TeacherKey");

        progressBar = views.findViewById(R.id.notebookListProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        noHw = views.findViewById(R.id.noHwId);

        circleImageView = views.findViewById(R.id.backFromNotebookTcId);
        circleImageView.setOnClickListener(this);
        createNewNotebookBtn = views.findViewById(R.id.createNewNotebookBtnId);
        createNewNotebookBtn.setOnClickListener(this);

        recyclerView = views.findViewById(R.id.notebooksRecyclerViewId);
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
                        notebookCustomAdapter.setId(classIdText) ;
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
        if(v.getId()==R.id.createNewNotebookBtnId){
            Bundle bundle = new Bundle();
            bundle.putString("IdKey", classIdText);

            CreateNotebookDialog createNotebookDialog = new CreateNotebookDialog();
            createNotebookDialog.setArguments(bundle);
            createNotebookDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }

        if(v.getId()==R.id.backFromNotebookTcId){
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
}
