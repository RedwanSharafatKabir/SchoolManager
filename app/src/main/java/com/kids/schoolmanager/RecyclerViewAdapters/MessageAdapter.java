package com.kids.schoolmanager.RecyclerViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kids.schoolmanager.ModelClasses.StoreChatData;
import com.kids.schoolmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

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
        String messages = storeChatData.get(position).getMessage();
        String images = storeChatData.get(position).getImageUrl();

        try {
            if(messages.equals("No_Message")){
                holder.show_message.setVisibility(View.GONE);
                holder.show_image.setVisibility(View.VISIBLE);
                Picasso.get().load(images).into(holder.show_image);
            }
        } catch(Exception e){
            Log.i("Message_Error ", e.getMessage() + " " + messages);
        }

        try {
            if (images.equals("No_Image")) {
                holder.show_image.setVisibility(View.GONE);
                holder.show_message.setVisibility(View.VISIBLE);
                holder.show_message.setText(messages);
            }
        } catch(Exception e){
            Log.i("Image_Error ", e.getMessage() + " " + images);
        }

    }

    @Override
    public int getItemCount() {
        return storeChatData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView show_message;
        ImageView show_image;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.messageId);
            show_image = itemView.findViewById(R.id.imageId);
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
