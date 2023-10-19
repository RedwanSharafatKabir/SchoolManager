package com.kids.schoolmanager.ClassDetails;

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

import com.kids.schoolmanager.ModelClasses.StoreClassesData;
import com.kids.schoolmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateClassDialog extends AppCompatDialogFragment implements View.OnClickListener{

    EditText className, classId;
    Button create;
    DatabaseReference databaseReferenceClasses;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String alreadyExistedClassId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.create_class_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Create new class")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        className = view.findViewById(R.id.classNameInputID);
        classId = view.findViewById(R.id.classIdInputID);

        create = view.findViewById(R.id.createClassCompleteID);
        create.setOnClickListener(this);

        databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("Classes Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String classNameString = className.getText().toString();
        String classIdString = classId.getText().toString();

        if(v.getId()==R.id.createClassCompleteID){
            if(classNameString.isEmpty()){
                className.setError("Enter class name");
            }

            if(classIdString.isEmpty()){
                classId.setError("Enter class Id");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        if (user.getDisplayName() != null) {
                            DatabaseReference ref = databaseReferenceClasses.child(classIdString);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        alreadyExistedClassId = dataSnapshot.getValue(String.class);

                                        String teacherPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                                        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("Teacher Information").child(teacherPhone);
                                        ref2.child("username").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                try {
                                                    String teacherName = dataSnapshot.getValue(String.class);
                                                    storeClassesDataMethod(classNameString, classIdString, teacherName);

                                                    className.setText("");
                                                    classId.setText("");
                                                    Toast.makeText(getActivity(), "Class created successfully", Toast.LENGTH_SHORT).show();
                                                    getDialog().dismiss();
                                                } catch (Exception e){
                                                    classId.setError("Class Id already exists");
                                                    Log.i("Database error", "Already exist");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                                        });

                                    } catch (Exception e){
                                        classId.setError("Class Id already exists");
                                        Log.i("Database error", "Already exist");
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

    private void storeClassesDataMethod(String classNameString, String classIdString, String teacherName) {
        String Key_User_Info = classIdString;
        StoreClassesData storeClassesData = new StoreClassesData(classNameString, classIdString, teacherName);
        databaseReferenceClasses.child(Key_User_Info).setValue(storeClassesData);
    }
}
