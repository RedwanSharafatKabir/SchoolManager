package com.classapp.kidssolution.LiveChat;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.classapp.kidssolution.AppAction.MainActivity;
import com.classapp.kidssolution.AppAction.MainActivityGd;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularChatPageGd extends AppCompatActivity implements View.OnClickListener {

    CircleImageView teacherImage;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ImageView backBtn, callBtn;
    TextView nameText, phoneText;
    String imageUrl, nameString, phoneString;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_chat_gd);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("teacherNameKey");
        phoneString = intent.getStringExtra("teacherPhoneKey");

        nameText = findViewById(R.id.teacherParticularNameId);
        phoneText = findViewById(R.id.teacherParticularPhoneId);
        nameText.setText(nameString);
        phoneText.setText(phoneString);
        teacherImage = findViewById(R.id.teacherParticularImageId);

        backBtn = findViewById(R.id.backFromParticularChatGdId);
        backBtn.setOnClickListener(this);
        callBtn = findViewById(R.id.callTeacherId);
        callBtn.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Images");
        databaseReference.child(phoneString).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrl = dataSnapshot.getValue(String.class);
                if(imageUrl != null){
                    Picasso.get().load(imageUrl).into(teacherImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromParticularChatGdId){
            finish();
            Intent intent = new Intent(ParticularChatPageGd.this, MainActivityGd.class);
            intent.putExtra("EXTRA", "openChatFragment");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ParticularChatPageGd.this, MainActivityGd.class);
        intent.putExtra("EXTRA", "openChatFragment");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        super.onBackPressed();
    }
}
