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

import com.classapp.kidssolution.About_and_Profile.AboutInstitute;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;

import java.util.ArrayList;

public class ClassesCustomAdapter extends RecyclerView.Adapter<ClassesCustomAdapter.MyViewHolder> {

    Fragment fragment;
    FragmentTransaction feedbackTransaction;
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
        holder.textView2.setText("Subject: " + storeClassesData.get(position).getClassNameString());
        holder.textView3.setText("Teacher: " + storeClassesData.get(position).getClassTeacherName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ClassIdTitle = storeClassesData.get(position).getClassIdString();
                String ClassNameTitle = storeClassesData.get(position).getClassNameString();
                String ClassTeacherTitle = storeClassesData.get(position).getClassTeacherName();

                Bundle bundle=new Bundle();
                bundle.putString("IdKey", ClassIdTitle);
                bundle.putString("NameKey", ClassNameTitle);
                bundle.putString("TeacherKey", ClassTeacherTitle);

                fragment = new ParticularClassTcActivity();
                fragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                feedbackTransaction = activity.getSupportFragmentManager().beginTransaction();
                feedbackTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                feedbackTransaction.replace(R.id.fragmentID, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeClassesData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.classIdTitleID);
            textView2 = itemView.findViewById(R.id.classNameTitleID);
            textView3 = itemView.findViewById(R.id.classTeacherTitleID);
        }
    }
}
