package com.classapp.kidssolution.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.ClassDetails.CreateClassDialog;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.ModelClasses.StoreNoticeData;
import com.classapp.kidssolution.NoteBookHW.NoteBookTcActivity;
import com.classapp.kidssolution.NoticeBoard.EditNoticeDetails;
import com.classapp.kidssolution.R;
import java.util.ArrayList;

public class NoticeCustomAdapter extends RecyclerView.Adapter<NoticeCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreNoticeData> storeNoticeData;

    public NoticeCustomAdapter(Context c, ArrayList<StoreNoticeData> p) {
        context = c;
        storeNoticeData = p;
    }

    @NonNull
    @Override
    public NoticeCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoticeCustomAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notice_custom_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeCustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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

                EditNoticeDetails editNoticeDetails = new EditNoticeDetails();
                editNoticeDetails.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                editNoticeDetails.show(activity.getSupportFragmentManager(), "Sample dialog");
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
            textView1 = itemView.findViewById(R.id.noticeTitleID);
            textView2 = itemView.findViewById(R.id.noticeDescriptionID);
            textView3 = itemView.findViewById(R.id.noticeDateID);
        }
    }
}
