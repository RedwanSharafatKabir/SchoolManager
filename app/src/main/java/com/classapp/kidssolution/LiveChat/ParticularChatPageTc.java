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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.classapp.kidssolution.AppAction.MainActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularChatPageTc extends AppCompatActivity implements View.OnClickListener {

    CircleImageView guardianImage;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ImageView backBtn, callBtn;
    TextView nameText, phoneText;
    String imageUrl, nameString, phoneString;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_chat_tc);

        Intent intent = getIntent();
        nameString = intent.getStringExtra("guardianNameKey");
        phoneString = intent.getStringExtra("guardianPhoneKey");
//        imageUrl = getArguments().getString("guardianImage");

        nameText = findViewById(R.id.guardianParticularNameId);
        phoneText = findViewById(R.id.guardianParticularPhoneId);
        nameText.setText(nameString);
        phoneText.setText(phoneString);
        guardianImage = findViewById(R.id.guardianParticularImageId);

        backBtn = findViewById(R.id.backFromParticularChatTcId);
        backBtn.setOnClickListener(this);
        callBtn = findViewById(R.id.callGuardianId);
        callBtn.setOnClickListener(this);
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
