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
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.NoteBookHW.EditNotebookDetails;
import com.classapp.kidssolution.NoticeBoard.EditNoticeDetails;
import com.classapp.kidssolution.R;
import java.util.ArrayList;

public class NotebookCustomAdapter extends RecyclerView.Adapter<NotebookCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreNotebookData> storeNotebookData;
    String classId;

    public NotebookCustomAdapter(Context c, ArrayList<StoreNotebookData> p) {
        context = c;
        storeNotebookData = p;
    }

    @NonNull
    @Override
    public NotebookCustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotebookCustomAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.notebook_custom_adapter, parent, false));
    }

    public void setId(String classId) {
        this.classId = classId;
    }

    @Override
    public void onBindViewHolder(@NonNull NotebookCustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView1.setText(storeNotebookData.get(position).getNotebookTitle());
        holder.textView2.setText(storeNotebookData.get(position).getNotebookDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notebookTitle = storeNotebookData.get(position).getNotebookTitle();
                String notebookDescription = storeNotebookData.get(position).getNotebookDescription();

                Bundle bundle = new Bundle();
                bundle.putString("classIdKey", classId);
                bundle.putString("notebookTitleKey", notebookTitle);
                bundle.putString("notebookDescriptionKey", notebookDescription);

                EditNotebookDetails editNotebookDetails = new EditNotebookDetails();
                editNotebookDetails.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                editNotebookDetails.show(activity.getSupportFragmentManager(), "Sample dialog");
            }
        });
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
