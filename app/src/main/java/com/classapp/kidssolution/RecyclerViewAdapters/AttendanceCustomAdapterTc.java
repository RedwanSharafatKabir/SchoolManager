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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendanceCustomAdapterTc extends RecyclerView.Adapter<AttendanceCustomAdapterTc.MyViewHolder> {

    public Context context;
    public ArrayList<StoreAttendanceData> storeAttendanceData;
    public String classIdText, currentDate, currentDay, username, userPhone, checkDate, checkDate2, checkDay;
    DatabaseReference databaseReference;
    Boolean ifChecked;
    SimpleDateFormat simpleDateFormat, simpleDateFormat2;
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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String presentStatus = "1", absentStatus = "0";
        Date date = null;
        Date cal = Calendar.getInstance().getTime();

        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        checkDate = simpleDateFormat.format(cal);
        checkDate2 = simpleDateFormat2.format(cal);

        try {
            date = simpleDateFormat2.parse(checkDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat dayFormat = new SimpleDateFormat("EEEE");
        checkDay = dayFormat.format(date);

        currentDate = storeAttendanceData.get(position).getFixedDate();

        if(checkDate.equals(currentDate)){
            count = 1;
            username = storeAttendanceData.get(position).getUsername();
            currentDay = storeAttendanceData.get(position).getFinalDay();
            ifChecked = storeAttendanceData.get(position).getIfChecked();
            userPhone = storeAttendanceData.get(position).getUserPhone();

            holder.textView1.setText("Student Name: " + username);
            holder.textView2.setText(currentDate);
            holder.textView3.setText(currentDay);
            holder.aSwitch.setChecked(ifChecked);

            holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(holder.aSwitch.isChecked()){
                        Toast.makeText(context, "Checked", Toast.LENGTH_SHORT).show();
                        storeAttendanceStatus(presentStatus, username, currentDay, currentDate, true, userPhone);
                    }
                    if(!holder.aSwitch.isChecked()){
                        Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                        storeAttendanceStatus(absentStatus, username, currentDay, currentDate, false, userPhone);
                    }
                }
            });

            return;
        }

        username = storeAttendanceData.get(position).getUsername();
        userPhone = storeAttendanceData.get(position).getUserPhone();

        holder.textView1.setText("Student Name: " + username);
        holder.textView2.setText(checkDate);
        holder.textView3.setText(checkDay);

        refresh(1000, absentStatus, username, checkDay, checkDate, false, userPhone);
    }

    private void storeAttendanceStatus(String present, String username, String finalDay, String fixedDate, Boolean ifChecked, String userPhone){
        StoreAttendanceData storeAttendanceData = new StoreAttendanceData(present, username, finalDay, fixedDate, ifChecked, userPhone);
        databaseReference.child(classIdText).child(fixedDate).child(userPhone).setValue(storeAttendanceData);

        refresh(1000, present, username, finalDay, fixedDate, ifChecked, userPhone);
    }

    private void refresh(int milliSecond, String present, String username, String finalDay, String fixedDate, Boolean ifChecked, String userPhone){
        if(count==0){
            final Handler handler = new Handler(Looper.getMainLooper());
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    storeAttendanceStatus(present, username, finalDay, fixedDate, ifChecked, userPhone);
                }
            };

            handler.postDelayed(runnable, milliSecond);
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
