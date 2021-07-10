package com.classapp.kidssolution.NoteBookHW;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.ModelClasses.StoreNoticeData;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditNotebookDetails extends AppCompatDialogFragment implements View.OnClickListener{

    TextView notebookTitle;
    EditText notebookDescription;
    Button update, delete;
    DatabaseReference databaseReference;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String getNotebookTitle, getNotebookDescription, getClassId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_notebook_details_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Update notebook")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        getClassId = getArguments().getString("classIdKey");
        getNotebookTitle = getArguments().getString("notebookTitleKey");
        getNotebookDescription = getArguments().getString("notebookDescriptionKey");

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        notebookTitle = view.findViewById(R.id.notebookTitleEditID);
        notebookTitle.setText(getNotebookTitle);
        notebookDescription = view.findViewById(R.id.notebookDescriptionEditID);
        notebookDescription.setText(getNotebookDescription);

        update = view.findViewById(R.id.updateNotebookCompleteID);
        update.setOnClickListener(this);
        delete = view.findViewById(R.id.deleteNotebookCompleteID);
        delete.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Notebook Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String notebookTitleString = notebookTitle.getText().toString();
        String notebookDescriptionString = notebookDescription.getText().toString();

        if(v.getId()==R.id.deleteNotebookCompleteID){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Are you sure you want to delete this notebook ?");
            alertDialog.setIcon(R.drawable.exit);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        databaseReference.child(getClassId).removeValue();

                        Toast t = Toast.makeText(getActivity(), "Notebook deleted", Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();

                        getDialog().dismiss();

                    } catch(Exception e){getDialog().dismiss();}
                }
            });

            alertDialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialogBuilder = alertDialog.create();
            alertDialogBuilder.show();
        }

        if(v.getId()==R.id.updateNotebookCompleteID){
            if(notebookDescriptionString.isEmpty()){
                notebookDescription.setError("Enter notice details");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Date cal = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = simpleDateFormat.format(cal);

                    storeNoticeDataMethod(notebookTitleString, notebookDescriptionString, formattedDate);
                    notebookDescription.setText("");

                    Toast.makeText(getActivity(), "Notebook updated successfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();

                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void storeNoticeDataMethod(String noticeTitle, String noticeDescription, String notebookDate) {
        String Key_User_Info = noticeTitle;
        StoreNotebookData storeNotebookData = new StoreNotebookData(noticeTitle, noticeDescription, notebookDate);
        databaseReference.child(getClassId).child(Key_User_Info).setValue(storeNotebookData);
    }
}
