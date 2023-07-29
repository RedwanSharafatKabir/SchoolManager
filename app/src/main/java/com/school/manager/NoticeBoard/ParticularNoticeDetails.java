package com.school.manager.NoticeBoard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.school.manager.R;

public class ParticularNoticeDetails extends AppCompatDialogFragment{

    TextView noticeTitle, noticeDescription, noticeDate;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String getNoticeTitle, getNoticeDescription, getNoticeDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.particular_notice_details_dialog, null);
        builder.setView(view).setCancelable(false).setTitle("Notice details")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });

        getNoticeTitle = getArguments().getString("noticeTitleKey");
        getNoticeDescription = getArguments().getString("noticeDescriptionKey");
        getNoticeDate = getArguments().getString("noticeDateKey");

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        noticeTitle = view.findViewById(R.id.noticeTitleDetailsID);
        noticeTitle.setText(getNoticeTitle);
        noticeDescription = view.findViewById(R.id.noticeDescriptionDetailsID);
        noticeDescription.setText(getNoticeDescription);
        noticeDate = view.findViewById(R.id.noticeDateDetailsID);
        noticeDate.setText(getNoticeDate);

        return builder.create();
    }
}
