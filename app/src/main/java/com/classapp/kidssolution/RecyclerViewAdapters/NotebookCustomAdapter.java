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
import com.classapp.kidssolution.R;
import java.util.ArrayList;

public class NotebookCustomAdapter extends RecyclerView.Adapter<NotebookCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreNotebookData> storeNotebookData;

    public NotebookCustomAdapter(Context c, ArrayList<StoreNotebookData> p) {
        context = c;
        storeNotebookData = p;
    }

    @NonNull
    @Override
    public NotebookCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotebookCustomAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notebook_custom_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookCustomAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(storeNotebookData.get(position).getNotebookTitle());
        holder.textView2.setText(storeNotebookData.get(position).getNotebookDescription());
    }

    @Override
    public int getItemCount() {
        return storeNotebookData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.notebookTitleID);
            textView2 = itemView.findViewById(R.id.notebookDescriptionID);
        }
    }
}
