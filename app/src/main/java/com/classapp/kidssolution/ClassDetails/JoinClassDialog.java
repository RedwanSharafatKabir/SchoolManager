package com.classapp.kidssolution.ClassDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JoinClassDialog extends AppCompatDialogFragment implements View.OnClickListener{

    EditText classId;
    Button join;
    DatabaseReference databaseReferenceJoinClass, databaseReferenceClasses, attendanceReference, guardianReference;
    View view;
    Date date = null;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classNameStringGd, alreadyExistedClassId, classTeacherNameGd, present = "0", fixedDate, username, fixedDate2;
    SimpleDateFormat simpleDateFormat1, simpleDateFormat2;
    String userPhone, finalDay;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.join_class_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Join a new class")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        Date cal = Calendar.getInstance().getTime();
        simpleDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy");
        simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        fixedDate = simpleDateFormat1.format(cal);
        fixedDate2 = simpleDateFormat2.format(cal);

        try {
            date = simpleDateFormat2.parse(fixedDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dayFormat = new SimpleDateFormat("EEEE");
        finalDay = dayFormat.format(date);

        classId = view.findViewById(R.id.joinClassIdInputID);

        join = view.findViewById(R.id.joinClassCompleteID);
        join.setOnClickListener(this);

        databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("Classes Information");
        databaseReferenceJoinClass = FirebaseDatabase.getInstance().getReference("Classes Information Guardian");
        attendanceReference = FirebaseDatabase.getInstance().getReference("Attendance Information");
        guardianReference = FirebaseDatabase.getInstance().getReference("Guardian Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String classIdStringGd = classId.getText().toString();

        if(v.getId()==R.id.joinClassCompleteID){
            if(classIdStringGd.isEmpty()){
                classId.setError("Enter class Id");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        if (user.getDisplayName() != null) {
                            DatabaseReference ref = databaseReferenceClasses.child(classIdStringGd).child("classIdString");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        alreadyExistedClassId = dataSnapshot.getValue(String.class);

                                        if(!alreadyExistedClassId.equals(null)) {
                                            DatabaseReference ref = databaseReferenceClasses.child(classIdStringGd).child("classNameString");
                                            ref.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    classNameStringGd = dataSnapshot.getValue(String.class);

                                                    DatabaseReference ref = databaseReferenceClasses.child(classIdStringGd).child("classTeacherName");
                                                    ref.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            classTeacherNameGd = dataSnapshot.getValue(String.class);

                                                            DatabaseReference ref = guardianReference.child(userPhone).child("username");
                                                            ref.addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    username = dataSnapshot.getValue(String.class);
                                                                    storeJoinedClassesMethod(classNameStringGd, classIdStringGd, classTeacherNameGd,
                                                                            present, fixedDate, username, finalDay);

                                                                    classId.setText("");
                                                                    Toast.makeText(getActivity(), "Joined successfully", Toast.LENGTH_SHORT).show();
                                                                    getDialog().dismiss();
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                            });
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                                            });
                                        }

                                    } catch (Exception e){
                                        classId.setError("Invalid Class Id");
                                        Log.i("Database error", "Id does not exist");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void storeJoinedClassesMethod(String classNameStringGd, String classIdStringGd, String classTeacherNameGd,
                                          String present, String fixedDate, String username, String finalDay) {

        StoreGdClassesData storeGdClassesData = new StoreGdClassesData(classNameStringGd, classIdStringGd, classTeacherNameGd, userPhone, username);
        databaseReferenceJoinClass.child(userPhone).child(classIdStringGd).setValue(storeGdClassesData);

        StoreAttendanceData storeAttendanceData = new StoreAttendanceData(present, username, finalDay, fixedDate, false, userPhone);
        attendanceReference.child(classIdStringGd).child(fixedDate).child(userPhone).setValue(storeAttendanceData);
    }
}
