package com.classapp.kidssolution.RecyclerViewAdapters;

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
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.ModelClasses.StoreNoticeData;
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
    public void onBindViewHolder(@NonNull NoticeCustomAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(storeNoticeData.get(position).getNoticeTitle());
        holder.textView2.setText(storeNoticeData.get(position).getNoticeDescription());
    }

    @Override
    public int getItemCount() {
        return storeNoticeData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.noticeTitleID);
            textView2 = itemView.findViewById(R.id.noticeDescriptionID);
        }
    }
}
