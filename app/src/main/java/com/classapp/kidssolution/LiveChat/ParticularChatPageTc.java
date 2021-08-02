package com.classapp.kidssolution.LiveChat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.classapp.kidssolution.AppAction.MainActivity;
import com.classapp.kidssolution.ClassDetails.ParticularClassTcActivity;
import com.classapp.kidssolution.ModelClasses.StoreChatData;
import com.classapp.kidssolution.ModelClasses.StoreGdClassesData;
import com.classapp.kidssolution.ModelClasses.StoreNotebookData;
import com.classapp.kidssolution.R;
import com.classapp.kidssolution.RecyclerViewAdapters.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularChatPageTc extends AppCompatActivity implements View.OnClickListener {

    EditText writeMessage;
    CircleImageView guardianImage;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ImageView backBtn, sendMessage;
    TextView nameText, phoneText;
    String nameString, receiver, imageUrl, message, sender;
    DatabaseReference databaseReference, messageReference;
    MessageAdapter messageAdapter;
    List<StoreChatData> storeChatDataList;
    RecyclerView recyclerView;
    Parcelable recyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_chat_tc);

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        Intent intent = getIntent();
        nameString = intent.getStringExtra("guardianNameKey");
        receiver = intent.getStringExtra("guardianPhoneKey");
        sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        recyclerView = findViewById(R.id.recyclerViewId1);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
//            }
//        });

        nameText = findViewById(R.id.guardianParticularNameId);
        phoneText = findViewById(R.id.guardianParticularPhoneId);
        nameText.setText(nameString);
        phoneText.setText(receiver);

        guardianImage = findViewById(R.id.guardianParticularImageId);
        writeMessage = findViewById(R.id.writeMessageTcId);

        sendMessage = findViewById(R.id.sendMessageToGuardianId);
        sendMessage.setOnClickListener(this);
        backBtn = findViewById(R.id.backFromParticularChatTcId);
        backBtn.setOnClickListener(this);

        messageReference = FirebaseDatabase.getInstance().getReference("Chat Information");
        databaseReference = FirebaseDatabase.getInstance().getReference("Guardian Images");
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            databaseReference.child(receiver).child("avatar").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imageUrl = dataSnapshot.getValue(String.class);
                    if (imageUrl != null) {
                        Picasso.get().load(imageUrl).into(guardianImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
        } else {
            Toast.makeText(ParticularChatPageTc.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
        }

        readMessages(sender, receiver);
    }

    @Override
    public void onClick(View v) {
        message = writeMessage.getText().toString();

        if(v.getId()==R.id.sendMessageToGuardianId){
            if(!message.equals("")){
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    storeChatMethod(message, receiver, sender);
                    readMessages(sender, receiver);
                    writeMessage.setText("");

                } else {
                    Toast.makeText(ParticularChatPageTc.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }

        if(v.getId()==R.id.backFromParticularChatTcId){
            finish();
            Intent intent = new Intent(ParticularChatPageTc.this, MainActivity.class);
            intent.putExtra("EXTRA", "openChatFragment");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    private void readMessages(String myId, String userId){
        storeChatDataList = new ArrayList<StoreChatData>();
        messageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                storeChatDataList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    StoreChatData storeChatData = dataSnapshot.getValue(StoreChatData.class);
                    if(storeChatData.getReceiver().equals(myId) && storeChatData.getSender().equals(userId) ||
                            storeChatData.getReceiver().equals(userId) && storeChatData.getSender().equals(myId)){

                        storeChatDataList.add(storeChatData);
                    }

                    messageAdapter = new MessageAdapter(ParticularChatPageTc.this, storeChatDataList);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void storeChatMethod(String message, String receiver, String sender){
        StoreChatData storeChatData = new StoreChatData(message, receiver, sender);
        messageReference.push().setValue(storeChatData);
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
