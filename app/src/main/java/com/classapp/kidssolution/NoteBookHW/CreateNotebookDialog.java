package com.classapp.kidssolution.NoteBookHW;

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

import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateNotebookDialog extends AppCompatDialogFragment implements View.OnClickListener{

    EditText notebookTitle, notebookDescription;
    Button create;
    DatabaseReference databaseReference;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String classIdText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.create_notebook_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Create new notebook")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        classIdText = getArguments().getString("IdKey");
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        notebookTitle = view.findViewById(R.id.notebookTitleInputID);
        notebookDescription = view.findViewById(R.id.notebookDescriptionInputID);

        create = view.findViewById(R.id.createNotebookCompleteID);
        create.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Notebook Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String notebookTitleString = notebookTitle.getText().toString();
        String notebookDescriptionString = notebookDescription.getText().toString();

        if(v.getId()==R.id.createNotebookCompleteID){
            if(notebookTitleString.isEmpty()){
                notebookTitle.setError("Enter notebook title");
            }

            if(notebookDescriptionString.isEmpty()){
                notebookDescription.setError("Enter task details");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    storeNotebookDataMethod(notebookTitleString, notebookDescriptionString);

                    notebookTitle.setText("");
                    notebookDescription.setText("");

                    Toast.makeText(getActivity(), "Notebook created successfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();

                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void storeNotebookDataMethod(String notebookTitleString, String notebookDescriptionString) {
        String Key_User_Info = notebookTitleString;
        StoreNotebookData storeNotebookData = new StoreNotebookData(notebookTitleString, notebookDescriptionString);
        databaseReference.child(classIdText).child(Key_User_Info).setValue(storeNotebookData);
    }
}
