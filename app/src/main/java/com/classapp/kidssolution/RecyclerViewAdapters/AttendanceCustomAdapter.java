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
import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;

import java.util.ArrayList;

public class AttendanceCustomAdapter extends RecyclerView.Adapter<AttendanceCustomAdapter.MyViewHolder> {

    Context context;
    ArrayList<StoreAttendanceData> storeAttendanceData;

    public AttendanceCustomAdapter(Context c, ArrayList<StoreAttendanceData> p) {
        context = c;
        storeAttendanceData = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_custom_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView1.setText("Student name: " + storeAttendanceData.get(position).getUsername());
        holder.textView2.setText("Status: Present");
    }

    @Override
    public int getItemCount() {
        return storeAttendanceData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.studentNameID);
            textView2 = itemView.findViewById(R.id.presentStatusID);
        }
    }
}
