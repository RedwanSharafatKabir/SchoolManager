package com.kids.schoolmanager.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.kids.schoolmanager.LiveChat.ParticularChatPageTc;
import com.kids.schoolmanager.ModelClasses.StoreGdClassesData;
import com.kids.schoolmanager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatCustomAdapterTc extends RecyclerView.Adapter<ChatCustomAdapterTc.MyViewHolder> {

    Context context;
    ArrayList<StoreGdClassesData> storeGdClassesData;
    DatabaseReference databaseReference;
    String guardianImageUrl;

    public ChatCustomAdapterTc(Context c, ArrayList<StoreGdClassesData> p) {
        context = c;
        storeGdClassesData = p;
    }

    @NonNull
    @Override
    public ChatCustomAdapterTc.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatCustomAdapterTc.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_custom_adapter_tc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCustomAdapterTc.MyViewHolder holder, int position) {
        String guardianName = storeGdClassesData.get(position).getGuardianName();
        String guardianPhone = storeGdClassesData.get(position).getGuardianPhone();

        holder.textView1.setText(guardianName);
        holder.textView2.setText(guardianPhone);

        try {
            databaseReference.child(guardianPhone).child("avatar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    guardianImageUrl = dataSnapshot.getValue(String.class);
                    if (guardianImageUrl != null) {
                        Picasso.get().load(guardianImageUrl).into(holder.circleImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        } catch (Exception e){
            Log.i("Error ", e.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
            AppCompatActivity activity = (AppCompatActivity) v.getContext();
            Intent intent = new Intent(activity, ParticularChatPageTc.class);
            intent.putExtra("guardianNameKey", guardianName);
            intent.putExtra("guardianPhoneKey", guardianPhone);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public int getItemCount() {
        return storeGdClassesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            databaseReference = FirebaseDatabase.getInstance().getReference("Guardian Images");
            circleImageView = itemView.findViewById(R.id.guardianImageId);
            textView1 = itemView.findViewById(R.id.guardianNameId);
            textView2 = itemView.findViewById(R.id.guardianPhoneId);
        }
    }
}
