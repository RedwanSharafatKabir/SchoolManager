package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
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

import com.classapp.kidssolution.ClassDetails.ParticularClassGdActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.R;

import java.util.ArrayList;

public class ClassesCustomAdapterGd extends RecyclerView.Adapter<ClassesCustomAdapterGd.MyViewHolder> {

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
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
        holder.textView2.setText("Subject: " + storeGdClassesData.get(position).getClassNameStringGd());
        holder.textView3.setText("Teacher: " + storeGdClassesData.get(position).getClassTeacherNameGd());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ClassIdTitle = storeGdClassesData.get(position).getClassIdStringGd();
                String ClassNameTitle = storeGdClassesData.get(position).getClassNameStringGd();
                String ClassTeacherTitle = storeGdClassesData.get(position).getClassTeacherNameGd();

                Bundle bundle=new Bundle();
                bundle.putString("IdKeyGd", ClassIdTitle);
                bundle.putString("NameKeyGd", ClassNameTitle);
                bundle.putString("TeacherKeyGd", ClassTeacherTitle);

                fragment = new ParticularClassGdActivity();
                fragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                fragmentTransaction.replace(R.id.fragmentGdID, fragment).addToBackStack(null).commit();
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
