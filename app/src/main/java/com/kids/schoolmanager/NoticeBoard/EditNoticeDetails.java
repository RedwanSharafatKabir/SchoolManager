package com.kids.schoolmanager.NoticeBoard;

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

import androidx.appcompat.app.AppCompatDialogFragment;

import com.kids.schoolmanager.ModelClasses.StoreNoticeData;
import com.kids.schoolmanager.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditNoticeDetails extends AppCompatDialogFragment implements View.OnClickListener{

    TextView noticeTitle;
    EditText noticeDescription;
    Button update, delete;
    DatabaseReference databaseReference;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String getNoticeTitle, getNoticeDescription, getNoticeDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.edit_notice_details_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Update your notice")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        getNoticeTitle = getArguments().getString("noticeTitleKey");
        getNoticeDescription = getArguments().getString("noticeDescriptionKey");
        getNoticeDate = getArguments().getString("noticeDateKey");

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        noticeTitle = view.findViewById(R.id.noticeTitleEditID);
        noticeTitle.setText(getNoticeTitle);
        noticeDescription = view.findViewById(R.id.noticeDescriptionEditID);
        noticeDescription.setText(getNoticeDescription);

        update = view.findViewById(R.id.updateNoticeCompleteID);
        update.setOnClickListener(this);
        delete = view.findViewById(R.id.deleteNoticeCompleteID);
        delete.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Notice Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String noticeTitleString = noticeTitle.getText().toString();
        String noticeDescriptionString = noticeDescription.getText().toString();

        if(v.getId()==R.id.deleteNoticeCompleteID){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Are you sure you want to delete this notice ?");
            alertDialog.setIcon(R.drawable.exit);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        databaseReference.child(getNoticeTitle).removeValue();

                        Toast t = Toast.makeText(getActivity(), "Notice deleted", Toast.LENGTH_LONG);
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

        if(v.getId()==R.id.updateNoticeCompleteID){
            if(noticeDescriptionString.isEmpty()){
                noticeDescription.setError("Enter notice details");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Date cal = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = simpleDateFormat.format(cal);

                    storeNoticeDataMethod(noticeTitleString, noticeDescriptionString, formattedDate);
                    noticeDescription.setText("");

                    Toast.makeText(getActivity(), "Notice updated successfully", Toast.LENGTH_SHORT).show();
                    getDialog().dismiss();

                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void storeNoticeDataMethod(String noticeTitle, String noticeDescription, String noticeDate) {
        String Key_User_Info = noticeTitle;
        StoreNoticeData storeNoticeData = new StoreNoticeData(noticeTitle, noticeDescription, noticeDate);
        databaseReference.child(Key_User_Info).setValue(storeNoticeData);
    }
}
