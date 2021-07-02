package com.classapp.kidssolution.ClassDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateClassDialog extends AppCompatDialogFragment implements View.OnClickListener{

    EditText task, description, date;
    Button save;
    String Phone;
    DatabaseReference databaseReferenceClasses;
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.create_class_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Create new task")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        task = view.findViewById(R.id.taskNameInputID);
        description = view.findViewById(R.id.taskDetailsInputID);
        date = view.findViewById(R.id.executionDateInputID);

        save = view.findViewById(R.id.saveTaskID);
        save.setOnClickListener(this);

        databaseReferenceClasses = FirebaseDatabase.getInstance().getReference("Classes Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        final String routineTitle = task.getText().toString();
        final String routineDesc = description.getText().toString();
        final String routineExecuteDate = date.getText().toString();

        if(v.getId()==R.id.saveTaskID){
            if(routineTitle.isEmpty() || routineDesc.isEmpty() || routineExecuteDate.isEmpty()){
                Toast.makeText(getActivity(), "Fill up all required fields", Toast.LENGTH_LONG).show();
            }
            else {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user.getDisplayName() != null) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User Information")
                                .child(user.getDisplayName()).child("phoneObj");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Phone = dataSnapshot.getValue(String.class);
                                storeRoutineDataMethod(Phone, routineTitle, routineDesc, routineExecuteDate);
                                task.setText("");
                                description.setText("");
                                date.setText("");
                                getDialog().dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }
        }
    }

    private void storeRoutineDataMethod(String Phone, String routineTitle,
                                        String routineDesc, String routineExecuteDate) {

        String Key_User_Info = Phone;
//        StoreRoutineData storeRoutineData = new StoreRoutineData(routineTitle, routineDesc, routineExecuteDate);
//        databaseReference.child(Key_User_Info).child(routineTitle).setValue(storeRoutineData);
    }
}
