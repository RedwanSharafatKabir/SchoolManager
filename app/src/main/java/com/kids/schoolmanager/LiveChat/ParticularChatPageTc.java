package com.kids.schoolmanager.LiveChat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kids.schoolmanager.AppAction.MainActivity;
import com.kids.schoolmanager.ModelClasses.StoreChatData;
import com.kids.schoolmanager.R;
import com.kids.schoolmanager.RecyclerViewAdapters.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ParticularChatPageTc extends AppCompatActivity implements View.OnClickListener {

    EditText writeMessage;
    CircleImageView guardianImage;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ImageView backBtn, sendMessage, sendImage;
    TextView nameText, phoneText;
    String nameString, receiver, imageUrl, message, sender, messageImageURL, image_name;
    DatabaseReference databaseReference, messageReference;
    MessageAdapter messageAdapter;
    List<StoreChatData> storeChatDataList;
    RecyclerView recyclerView;
    StorageReference storageReference;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular_chat_tc);

        dialog = new ProgressDialog(this);
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
        sendImage = findViewById(R.id.sendImageToGuardianId);
        sendImage.setOnClickListener(this);

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
            if(!message.equals("") ){
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    storeChatMethod(message, receiver, sender, "No_Image");
                    readMessages(sender, receiver);
                    writeMessage.setText("");

                } else {
                    Toast.makeText(ParticularChatPageTc.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }

        if(v.getId()==R.id.sendImageToGuardianId){
            someActivityResultLauncher.launch("image/*");
        }

        if(v.getId()==R.id.backFromParticularChatTcId){
            finish();
            Intent intent = new Intent(ParticularChatPageTc.this, MainActivity.class);
            intent.putExtra("EXTRA", "openChatFragment");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    ActivityResultLauncher<String> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        Uri uriProfileImage = result;
                        writeMessage.setText(uriProfileImage.toString());

                        sendMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                    uploadImageToFirebase(uriProfileImage);

                                } else {
                                    Toast.makeText(ParticularChatPageTc.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });

    private void uploadImageToFirebase(Uri uriProfileImage){
        dialog.setMessage("Sending.....");
        dialog.show();

        image_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + System.currentTimeMillis();
        storageReference = FirebaseStorage.getInstance()
                .getReference("chat images/" + image_name + ".jpg");

        if(uriProfileImage!=null){
            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            messageImageURL = uri.toString();
                            saveImageInfo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(ParticularChatPageTc.this, "Could not send", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {}
            });
        }
    }

    private void saveImageInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null && messageImageURL!=null){
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(messageImageURL)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {}
            });

            storeChatMethod("No_Message", receiver, sender, messageImageURL);
            readMessages(sender, receiver);

            writeMessage.setText("");
            dialog.dismiss();

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message = writeMessage.getText().toString();
                    if(!message.equals("") ){
                        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                            storeChatMethod(message, receiver, sender, "No_Image");
                            readMessages(sender, receiver);
                            writeMessage.setText("");

                        } else {
                            Toast.makeText(ParticularChatPageTc.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
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

    private void storeChatMethod(String message, String receiver, String sender, String noImage){
        StoreChatData storeChatData = new StoreChatData(message, receiver, sender, noImage);
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
