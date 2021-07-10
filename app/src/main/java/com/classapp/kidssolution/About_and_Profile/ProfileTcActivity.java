package com.classapp.kidssolution.About_and_Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.AppAction.SplashScreen;
import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileTcActivity extends Fragment implements View.OnClickListener{


    View views, parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Snackbar snackbar;
    Button logoutBtn;
    DatabaseReference databaseReference;
    CircleImageView circleImageView, backBtn, editProfilePic;
    TextView nameText, emailText, phoneText;
    String userPhone;
    ProgressBar progressBar;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_profile_tc, container, false);

        circleImageView = views.findViewById(R.id.profilePicTcID);
        circleImageView.setOnClickListener(this);
        backBtn = views.findViewById(R.id.backFromProfileTcId);
        backBtn.setOnClickListener(this);
        logoutBtn = views.findViewById(R.id.logoutTcID);
        logoutBtn.setOnClickListener(this);

        editProfilePic = views.findViewById(R.id.uploadProfilePicTcID);
        editProfilePic.setOnClickListener(this);
        nameText = views.findViewById(R.id.profileNameTcID);
        emailText = views.findViewById(R.id.profileEmailTcID);
        phoneText = views.findViewById(R.id.profilePhoneTcID);

        progressBar = views.findViewById(R.id.profileProgressbarTcId);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Information");

        parentLayout = views.findViewById(android.R.id.content);
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getTeacherData();
        } else {
            snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();
        }

        return views;
    }

    private void getTeacherData(){
        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        databaseReference.child(userPhone).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(" " + dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        databaseReference.child(userPhone).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailText.setText(" " + dataSnapshot.getValue(String.class));
                phoneText.setText(" " + userPhone);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromProfileTcId){
            fragment = new TeacherMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.logoutTcID){
            logoutAppTc();
        }
    }

    private void logoutAppTc(){
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Logout !");
        alertDialogBuilder.setMessage("Are you sure you want to logout ?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nullValue = "";
                setNullDataMethod(nullValue);

                mAuth.getInstance().signOut();
                getActivity().finish();
                Intent it = new Intent(getActivity(), SplashScreen.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setNullDataMethod(String nullValue){
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput("Teacher_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(nullValue.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
