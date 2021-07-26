package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AttendanceCustomAdapterTc extends RecyclerView.Adapter<AttendanceCustomAdapterTc.MyViewHolder> {

    public Context context;
    public ArrayList<StoreAttendanceData> storeAttendanceData;
    public String classIdText, currentDate, currentDay, username, userPhone;
    DatabaseReference databaseReference;
    Boolean ifChecked;

    public AttendanceCustomAdapterTc(Context c, ArrayList<StoreAttendanceData> p, String classIdText) {
        context = c;
        storeAttendanceData = p;
        this.classIdText = classIdText;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_custom_adapter_tc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        username = storeAttendanceData.get(position).getUsername();
        currentDate = storeAttendanceData.get(position).getFixedDate();
        currentDay = storeAttendanceData.get(position).getFinalDay();
        ifChecked = storeAttendanceData.get(position).getIfChecked();
        userPhone = storeAttendanceData.get(position).getUserPhone();

        holder.textView1.setText("Student Name: " + username);
        holder.textView2.setText(currentDate);
        holder.textView3.setText(currentDay);
        holder.aSwitch.setChecked(ifChecked);

        String presentStatus = "1", absentStatus = "0";
//        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(holder.aSwitch.isChecked()){
//                    Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
//                    storeAttendanceStatus(presentStatus, username, currentDay, currentDate, userPhone, true);
//
//                } else if(!holder.aSwitch.isChecked()){
//                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
//                    storeAttendanceStatus(absentStatus, username, currentDay, currentDate, userPhone, false);
//                }
//            }
//        });
    }

    private void storeAttendanceStatus(String present, String username, String finalDay, String fixedDate, String userPhone, Boolean ifChecked){
        StoreAttendanceData storeAttendanceData = new StoreAttendanceData(present, username, finalDay, fixedDate, ifChecked, userPhone);
        databaseReference.child(classIdText).child(fixedDate).child(userPhone).setValue(storeAttendanceData);
    }

    @Override
    public int getItemCount() {
        return storeAttendanceData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2,textView3;
        CheckBox aSwitch;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.studentNameID);
            textView2 = itemView.findViewById(R.id.attendanceDateID);
            textView3 = itemView.findViewById(R.id.attendanceDayID);

            databaseReference = FirebaseDatabase.getInstance().getReference("Attendance Information");
            aSwitch = itemView.findViewById(R.id.attendanceCheckId);
        }
    }
}
