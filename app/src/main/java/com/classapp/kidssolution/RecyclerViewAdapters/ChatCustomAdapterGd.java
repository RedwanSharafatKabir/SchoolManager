package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.LiveChat.ParticularChatPageGd;
import com.classapp.kidssolution.LiveChat.ParticularChatPageTc;
import com.classapp.kidssolution.ModelClasses.StoreGuardianData;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.ModelClasses.StoreTeacherData;
import com.classapp.kidssolution.NoteBookHW.EditNotebookDetails;
import com.classapp.kidssolution.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCustomAdapterGd extends RecyclerView.Adapter<ChatCustomAdapterGd.MyViewHolder> {

    Context context;
    ArrayList<StoreTeacherData> storeTeacherData;

    public ChatCustomAdapterGd(Context c, ArrayList<StoreTeacherData> p) {
        context = c;
        storeTeacherData = p;
    }

    @NonNull
    @Override
    public ChatCustomAdapterGd.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatCustomAdapterGd.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_custom_adapter_gd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCustomAdapterGd.MyViewHolder holder, int position) {
        holder.textView1.setText(storeTeacherData.get(position).getUsername());
        holder.textView2.setText(storeTeacherData.get(position).getPhone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherName = storeTeacherData.get(position).getUsername();
                String teacherPhone = storeTeacherData.get(position).getPhone();
//                String teacherImage = storeGuardianData.get(position).getImage();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(activity, ParticularChatPageGd.class);
                intent.putExtra("teacherNameKey", teacherName);
                intent.putExtra("teacherPhoneKey", teacherPhone);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeTeacherData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            circleImageView = itemView.findViewById(R.id.teacherImageId);
            textView1 = itemView.findViewById(R.id.teacherNameId);
            textView2 = itemView.findViewById(R.id.teacherPhoneId);
        }
    }
}
