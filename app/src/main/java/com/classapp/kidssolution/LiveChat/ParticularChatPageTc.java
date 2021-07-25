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
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularChatPageTc extends AppCompatActivity implements View.OnClickListener {

    CircleImageView guardianImage;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ImageView backBtn, callBtn;
    TextView nameText, phoneText;
    String nameString, phoneString, imageUrl;
    DatabaseReference databaseReference;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_chat_tc);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("guardianNameKey");
        phoneString = intent.getStringExtra("guardianPhoneKey");

        nameText = findViewById(R.id.guardianParticularNameId);
        phoneText = findViewById(R.id.guardianParticularPhoneId);
        nameText.setText(nameString);
        phoneText.setText(phoneString);
        guardianImage = findViewById(R.id.guardianParticularImageId);

        backBtn = findViewById(R.id.backFromParticularChatTcId);
        backBtn.setOnClickListener(this);
        callBtn = findViewById(R.id.callGuardianId);
        callBtn.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Guardian Images");
        databaseReference.child(phoneString).child("avatar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                imageUrl = dataSnapshot.getValue(String.class);
                if(imageUrl != null){
                    Picasso.get().load(imageUrl).into(guardianImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromParticularChatTcId){
            finish();
            Intent intent = new Intent(ParticularChatPageTc.this, MainActivity.class);
            intent.putExtra("EXTRA", "openChatFragment");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(ParticularChatPageTc.this, MainActivity.class);
        intent.putExtra("EXTRA", "openChatFragment");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        super.onBackPressed();
    }
}
