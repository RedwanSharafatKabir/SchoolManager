package com.kids.schoolmanager.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kids.schoolmanager.LiveChat.ParticularChatPageGd;
import com.kids.schoolmanager.ModelClasses.StoreTeacherData;
import com.kids.schoolmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCustomAdapterGd extends RecyclerView.Adapter<ChatCustomAdapterGd.MyViewHolder> {

    Context context;
    ArrayList<StoreTeacherData> storeTeacherData;
    DatabaseReference databaseReference;
    String teacherImageUrl;

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
        String teacherName = storeTeacherData.get(position).getUsername();
        String teacherPhone = storeTeacherData.get(position).getPhone();

        holder.textView1.setText(teacherName);
        holder.textView2.setText(teacherPhone);

        databaseReference.child(teacherPhone).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teacherImageUrl = dataSnapshot.getValue(String.class);
                if(teacherImageUrl != null){
                    Picasso.get().load(teacherImageUrl).into(holder.circleImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Images");
            circleImageView = itemView.findViewById(R.id.teacherImageId);
            textView1 = itemView.findViewById(R.id.teacherNameId);
            textView2 = itemView.findViewById(R.id.teacherPhoneId);
        }
    }
}
