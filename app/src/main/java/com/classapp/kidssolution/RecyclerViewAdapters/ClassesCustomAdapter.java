package com.classapp.kidssolution.RecyclerViewAdapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassesCustomAdapter extends RecyclerView.Adapter<ClassesCustomAdapter.MyViewHolder> {

    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    Context context;
    ArrayList<StoreClassesData> storeClassesData;
    DatabaseReference databaseReference;

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
        String ClassIdTitle = storeClassesData.get(position).getClassIdString();
        String ClassNameTitle = storeClassesData.get(position).getClassNameString();
        String ClassTeacherTitle = storeClassesData.get(position).getClassTeacherName();

        holder.textView1.setText("Class Id: " + ClassIdTitle);
        holder.textView2.setText("Subject: " + ClassNameTitle);
        holder.textView3.setText("Teacher: " + ClassTeacherTitle);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teacherUserPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                databaseReference.child(teacherUserPhone).child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String teacherUserName = snapshot.getValue(String.class);
                        if(ClassTeacherTitle.equals(teacherUserName)){
                            Bundle bundle=new Bundle();
                            bundle.putString("IdKey", ClassIdTitle);
                            bundle.putString("NameKey", ClassNameTitle);
                            bundle.putString("TeacherKey", ClassTeacherTitle);

                            fragment = new ParticularClassTcActivity();
                            fragment.setArguments(bundle);

                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                            fragmentTransaction.replace(R.id.fragmentID, fragment).addToBackStack(null).commit();

                        } else {
                            Toast.makeText(context, "Access Denied", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
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

            databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Information");

            textView1 = itemView.findViewById(R.id.classIdTitleID);
            textView2 = itemView.findViewById(R.id.classNameTitleID);
            textView3 = itemView.findViewById(R.id.classTeacherTitleID);
        }
    }
}
