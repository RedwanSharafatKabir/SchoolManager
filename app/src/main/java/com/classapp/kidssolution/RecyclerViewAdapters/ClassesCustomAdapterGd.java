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
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;

import java.util.ArrayList;

public class ClassesCustomAdapterGd extends RecyclerView.Adapter<ClassesCustomAdapterGd.MyViewHolder> {

    Context context;
    ArrayList<StoreGdClassesData> storeGdClassesData;

    public ClassesCustomAdapterGd(Context c, ArrayList<StoreGdClassesData> p) {
        context = c;
        storeGdClassesData = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.classes_custom_adapter_gd, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView1.setText("Class Id: " + storeGdClassesData.get(position).getClassIdStringGd());
        holder.textView2.setText("Class Name: " + storeGdClassesData.get(position).getClassNameStringGd());
        holder.textView3.setText("Teacher: " + storeGdClassesData.get(position).getClassTeacherNameGd());

        final String ClassIdTitle = storeGdClassesData.get(position).getClassIdStringGd();
        final String ClassNameTitle = storeGdClassesData.get(position).getClassNameStringGd();
        final String ClassTeacherTitle = storeGdClassesData.get(position).getClassTeacherNameGd();

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
        return storeGdClassesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.classGdIdTitleID);
            textView2 = itemView.findViewById(R.id.classGdNameTitleID);
            textView3 = itemView.findViewById(R.id.classGdTeacherTitleID);
        }
    }
}
