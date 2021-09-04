package com.classapp.kidssolution.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.classapp.kidssolution.ModelClasses.StoreAttendanceData;
import com.classapp.kidssolution.R;
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

public class AttendanceSheetAdapter extends RecyclerView.Adapter<AttendanceSheetAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<StoreAttendanceData> storeAttendanceData;

    public AttendanceSheetAdapter(Context c, ArrayList<StoreAttendanceData> p) {
        context = c;
        storeAttendanceData = p;
    }

    @NonNull
    @Override
    public AttendanceSheetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttendanceSheetAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.attendance_sheet_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceSheetAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = storeAttendanceData.get(position).getUsername();
        String date = storeAttendanceData.get(position).getFixedDate();
        String present = storeAttendanceData.get(position).getPresent();
        String status = "";

        if(present.equals("1")){
            status = "Present";
        } else if(present.equals("0")){
            status = "Absent";
        }

        holder.textView1.setText(date);
        holder.textView2.setText(name);
        holder.textView3.setText(status);

    }

    @Override
    public int getItemCount() {
        return storeAttendanceData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView1, textView2, textView3;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView1 = itemView.findViewById(R.id.dateId);
            textView2 = itemView.findViewById(R.id.stdnNameId);
            textView3 = itemView.findViewById(R.id.atdncStsId);
        }
    }
}
