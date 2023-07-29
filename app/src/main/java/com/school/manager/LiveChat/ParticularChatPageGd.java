package com.school.manager.LiveChat;

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

import com.school.manager.AppAction.MainActivityGd;
import com.school.manager.ModelClasses.StoreChatData;
import com.school.manager.R;
import com.school.manager.RecyclerViewAdapters.MessageAdapter;
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

public class ParticularChatPageGd extends AppCompatActivity implements View.OnClickListener {

    EditText writeMessage;
    CircleImageView teacherImage;
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
        setContentView(R.layout.activity_particular_chat_gd);

        dialog = new ProgressDialog(this);
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        Intent intent = getIntent();
        nameString = intent.getStringExtra("teacherNameKey");
        receiver = intent.getStringExtra("teacherPhoneKey");
        sender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        recyclerView = findViewById(R.id.recyclerViewId2);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        nameText = findViewById(R.id.teacherParticularNameId);
        phoneText = findViewById(R.id.teacherParticularPhoneId);
        nameText.setText(nameString);
        phoneText.setText(receiver);

        teacherImage = findViewById(R.id.teacherParticularImageId);
        writeMessage = findViewById(R.id.writeMessageGdId);

        sendMessage = findViewById(R.id.sendMessageToTeacherId);
        sendMessage.setOnClickListener(this);
        backBtn = findViewById(R.id.backFromParticularChatGdId);
        backBtn.setOnClickListener(this);
        sendImage = findViewById(R.id.sendImageToTeacherId);
        sendImage.setOnClickListener(this);

        messageReference = FirebaseDatabase.getInstance().getReference("Chat Information");
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Images");
        databaseReference.child(receiver).child("avatar").addValueEventListener(new ValueEventListener() {
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

        readMessages(sender, receiver);
    }

    @Override
    public void onClick(View v) {
        message = writeMessage.getText().toString();

        if(v.getId()==R.id.sendMessageToTeacherId){
            if(!message.equals("")){
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    storeChatMethod(message, receiver, sender, "No_Image");
                    readMessages(sender, receiver);
                    writeMessage.setText("");

                } else {
                    Toast.makeText(ParticularChatPageGd.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
                }
            }
        }

        if(v.getId()==R.id.sendImageToTeacherId){
            someActivityResultLauncher.launch("image/*");
        }

        if(v.getId()==R.id.backFromParticularChatGdId){
            finish();
            Intent intent = new Intent(ParticularChatPageGd.this, MainActivityGd.class);
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
                                    Toast.makeText(ParticularChatPageGd.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ParticularChatPageGd.this, "Could not send", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ParticularChatPageGd.this, "Turn on internet connection", Toast.LENGTH_LONG).show();
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

                    messageAdapter = new MessageAdapter(ParticularChatPageGd.this, storeChatDataList);
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
        Intent intent = new Intent(ParticularChatPageGd.this, MainActivityGd.class);
        intent.putExtra("EXTRA", "openChatFragment");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        super.onBackPressed();
    }
}
