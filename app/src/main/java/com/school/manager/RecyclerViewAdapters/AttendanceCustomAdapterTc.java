package com.school.manager.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.school.manager.ModelClasses.StoreAttendanceData;
import com.school.manager.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendanceCustomAdapterTc extends RecyclerView.Adapter<AttendanceCustomAdapterTc.MyViewHolder> {

    public Context context;
    public ArrayList<StoreAttendanceData> storeAttendanceData;
    public String classIdText;
    DatabaseReference databaseReference;
    int count = 0;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String presentStatus = "1", absentStatus = "0";
        Date date = null;
        Date cal = Calendar.getInstance().getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        String checkDate = simpleDateFormat.format(cal);
        String checkDate2 = simpleDateFormat2.format(cal);

        try {
            date = simpleDateFormat2.parse(checkDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dayFormat = new SimpleDateFormat("EEEE");
        String checkDay = dayFormat.format(date);

        String currentDate = storeAttendanceData.get(position).getFixedDate();

        if(checkDate.equals(currentDate)){
            count = 1;
            String username = storeAttendanceData.get(position).getUsername();
            String currentDay = storeAttendanceData.get(position).getFinalDay();
            Boolean ifChecked = storeAttendanceData.get(position).getIfChecked();
            String userPhone = storeAttendanceData.get(position).getUserPhone();

            holder.textView1.setText("Student Name: " + username);
            holder.textView2.setText(currentDate);
            holder.textView3.setText(currentDay);
            holder.aSwitch.setChecked(ifChecked);

            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(holder.aSwitch.isChecked()){
                        storeAttendanceStatus(presentStatus, username, currentDay, currentDate, true, userPhone);
                    }
                    if(!holder.aSwitch.isChecked()){
                        storeAttendanceStatus(absentStatus, username, currentDay, currentDate, false, userPhone);
                    }
                }
            });

            return;
        }

        if(count==0){
            databaseReference.child(classIdText).child(checkDate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        Log.i("User_phone ", snapshot.getValue().toString());

                    } catch (Exception e){
                        count = 1;
                        String username = storeAttendanceData.get(position).getUsername();
                        String userPhone = storeAttendanceData.get(position).getUserPhone();
                        storeAttendanceStatus(absentStatus, username, checkDay, checkDate, false, userPhone);

                        Log.i("Exception_On_PhoneKey ", e.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }

    private void storeAttendanceStatus(String present, String username, String finalDay, String fixedDate, Boolean ifChecked, String userPhone){
        if(count==1){
            StoreAttendanceData storeAttendanceData = new StoreAttendanceData(present, username, finalDay, fixedDate, ifChecked, userPhone);
            databaseReference.child(classIdText).child(fixedDate).child(userPhone).setValue(storeAttendanceData);
            Log.i("Count ", String.valueOf(count));
        }
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
