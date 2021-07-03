package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;

import java.util.ArrayList;

public class ClassesCustomAdapter extends RecyclerView.Adapter<ClassesCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreClassesData> storeClassesData;

    public ClassesCustomAdapter(Context c, ArrayList<StoreClassesData> p) {
        context = c;
        storeClassesData = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.classes_custom_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView1.setText("Class Id: " + storeClassesData.get(position).getClassIdString());
        holder.textView2.setText("Class Name: " + storeClassesData.get(position).getClassNameString());
        holder.textView3.setText("Teacher: " + storeClassesData.get(position).getClassTeacherName());

        final String ClassIdTitle = storeClassesData.get(position).getClassIdString();
        final String ClassNameTitle = storeClassesData.get(position).getClassNameString();
        final String ClassTeacherTitle = storeClassesData.get(position).getClassTeacherName();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(context, ParticularClassTcActivity.class);
                it.putExtra("routine_title_key", ClassIdTitle);
                it.putExtra("routine_desc_key", ClassNameTitle);
                it.putExtra("routine_date_key", ClassTeacherTitle);
                context.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeClassesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.classIdTitleID);
            textView2 = itemView.findViewById(R.id.classNameTitleID);
            textView3 = itemView.findViewById(R.id.classTeacherTitleID);
        }
    }
}
