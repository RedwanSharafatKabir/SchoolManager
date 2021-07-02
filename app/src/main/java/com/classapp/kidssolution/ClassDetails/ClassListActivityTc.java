package com.classapp.kidssolution.ClassDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.classapp.kidssolution.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassListActivityTc extends Fragment implements View.OnClickListener{

    View views;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    CircleImageView circleImageView;
    RecyclerView recyclerView;
    LinearLayout createNewClassBtn;
    String userPhone;
    ProgressBar progressBar;
    DatabaseReference databaseReferenceClasses;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_class_list_tc, container, false);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        progressBar = views.findViewById(R.id.classesListProgressbarId);
        progressBar.setVisibility(View.VISIBLE);

        circleImageView = views.findViewById(R.id.backFromClassesPageId);
        circleImageView.setOnClickListener(this);
        createNewClassBtn = views.findViewById(R.id.createNewClassBtnId);
        createNewClassBtn.setOnClickListener(this);
        recyclerView = views.findViewById(R.id.classesRecyclerViewId);

        databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("Classes Information");

        return views;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.createNewClassBtnId){
            CreateClassDialog createClassDialog = new CreateClassDialog();
            createClassDialog.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }
    }
}
