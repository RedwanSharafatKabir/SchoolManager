package com.kids.schoolmanager.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kids.schoolmanager.ModelClasses.StoreNoticeData;
import com.kids.schoolmanager.NoticeBoard.ParticularNoticeDetails;
import com.kids.schoolmanager.R;
import java.util.ArrayList;

public class NoticeCustomAdapterGd extends RecyclerView.Adapter<NoticeCustomAdapterGd.MyViewHolder> {

    Context context;
    ArrayList<StoreNoticeData> storeNoticeData;

    public NoticeCustomAdapterGd(Context c, ArrayList<StoreNoticeData> p) {
        context = c;
        storeNoticeData = p;
    }

    @NonNull
    @Override
    public NoticeCustomAdapterGd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeCustomAdapterGd.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_custom_adapter_gd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeCustomAdapterGd.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView1.setText(storeNoticeData.get(position).getNoticeTitle());
        holder.textView2.setText(storeNoticeData.get(position).getNoticeDescription());
        holder.textView3.setText("Date: " + storeNoticeData.get(position).getNoticeDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noticeTitle = storeNoticeData.get(position).getNoticeTitle();
                String noticeDescription = storeNoticeData.get(position).getNoticeDescription();
                String noticeDate = storeNoticeData.get(position).getNoticeDate();

                Bundle bundle = new Bundle();
                bundle.putString("noticeTitleKey", noticeTitle);
                bundle.putString("noticeDescriptionKey", noticeDescription);
                bundle.putString("noticeDateKey", noticeDate);

                ParticularNoticeDetails particularNoticeDetails = new ParticularNoticeDetails();
                particularNoticeDetails.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                particularNoticeDetails.show(activity.getSupportFragmentManager(), "Sample dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeNoticeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.noticeTitleGdID);
            textView2 = itemView.findViewById(R.id.noticeDescriptionGdID);
            textView3 = itemView.findViewById(R.id.noticeDateGdID);
        }
    }
}
