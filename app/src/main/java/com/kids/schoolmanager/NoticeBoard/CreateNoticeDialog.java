package com.kids.schoolmanager.NoticeBoard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateNoticeDialog extends AppCompatDialogFragment implements View.OnClickListener{

    EditText noticeTitle, noticeDescription;
    Button create;
    DatabaseReference databaseReference;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.create_notice_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Create new notice")
                .setNegativeButton("Close", (dialog, which) -> {});

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        noticeTitle = view.findViewById(R.id.noticeTitleInputID);
        noticeDescription = view.findViewById(R.id.noticeDescriptionInputID);

        create = view.findViewById(R.id.createNoticeCompleteID);
        create.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Notice Information");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String noticeTitleString = noticeTitle.getText().toString();
        String noticeDescriptionString = noticeDescription.getText().toString();

        if(v.getId()==R.id.createNoticeCompleteID){
            if(noticeTitleString.isEmpty()){
                noticeTitle.setError("Enter notice title");
            }

            if(noticeDescriptionString.isEmpty()){
                noticeDescription.setError("Enter notice details");
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    Date cal = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String formattedDate = simpleDateFormat.format(cal);

                    storeNoticeDataMethod(noticeTitleString, noticeDescriptionString, formattedDate);

                    noticeTitle.setText("");
                    noticeDescription.setText("");

                    Toast.makeText(getActivity(), "Notice created successfully", Toast.LENGTH_SHORT).show();
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
