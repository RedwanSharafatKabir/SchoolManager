package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.AppAction.SplashScreen;
import com.classapp.kidssolution.Authentication.SigninGdActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.LiveChat.ParticularChatPageGd;
import com.classapp.kidssolution.LiveChat.ParticularChatPageTc;
import com.classapp.kidssolution.ModelClasses.StoreChatData;
import com.classapp.kidssolution.ModelClasses.StoreGuardianData;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.NoteBookHW.EditNotebookDetails;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    Context context;
    List<StoreChatData> storeChatData;
    FirebaseUser user;

    public MessageAdapter(Context c, List<StoreChatData> p) {
        context = c;
        storeChatData = p;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.message_right, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.message_left, parent, false);
            return new MessageAdapter.MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHolder holder, int position) {
        StoreChatData storeChatData1 = storeChatData.get(position);
        holder.show_message.setText(storeChatData1.getMessage());
    }

    @Override
    public int getItemCount() {
        return storeChatData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.messageId);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(storeChatData.get(position).getSender().equals(user.getDisplayName())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
