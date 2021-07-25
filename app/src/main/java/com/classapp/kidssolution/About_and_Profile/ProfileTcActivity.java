package com.classapp.kidssolution.About_and_Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.classapp.kidssolution.AppAction.GuardianMainActivity;
import com.classapp.kidssolution.AppAction.SplashScreen;
import com.classapp.kidssolution.AppAction.TeacherMainActivity;
import com.classapp.kidssolution.ModelClasses.StoreTeacherImage;
import com.classapp.kidssolution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileTcActivity extends Fragment implements View.OnClickListener{

    View views, parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Snackbar snackbar;
    Button logoutBtn;
    ProgressDialog dialog;
    DatabaseReference databaseReference, imageDatabaseReference;
    StorageReference storageReference;
    CircleImageView circleImageView, backBtn, editProfilePic;
    TextView nameText, emailText, phoneText;
    String userPhone, image_name, profileImageUrl;
    ProgressBar progressBar;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth mAuth;
    private Uri uriProfileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_profile_tc, container, false);

        dialog = new ProgressDialog(getActivity());

        circleImageView = views.findViewById(R.id.profilePicTcID);
        circleImageView.setOnClickListener(this);
        backBtn = views.findViewById(R.id.backFromProfileTcId);
        backBtn.setOnClickListener(this);
        logoutBtn = views.findViewById(R.id.logoutTcID);
        logoutBtn.setOnClickListener(this);

        editProfilePic = views.findViewById(R.id.uploadProfilePicTcID);
        editProfilePic.setOnClickListener(this);
        nameText = views.findViewById(R.id.profileNameTcID);
        emailText = views.findViewById(R.id.profileEmailTcID);
        phoneText = views.findViewById(R.id.profilePhoneTcID);

        progressBar = views.findViewById(R.id.profileProgressbarTcId);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Teacher Information");
        imageDatabaseReference = FirebaseDatabase.getInstance().getReference("Teacher Images");

        parentLayout = views.findViewById(android.R.id.content);
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getTeacherData();
        } else {
            snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();
        }

        return views;
    }

    private void getTeacherData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userPhone = user.getDisplayName();

        databaseReference.child(userPhone).child("username").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameText.setText(" " + dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        databaseReference.child(userPhone).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emailText.setText(" " + dataSnapshot.getValue(String.class));
                phoneText.setText(" " + userPhone);
                if (user.getPhotoUrl() != null) {
                    Picasso.get().load(user.getPhotoUrl().toString()).into(circleImageView);
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromProfileTcId){
            fragment = new TeacherMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentID, fragment);
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.logoutTcID){
            logoutAppTc();
        }

        if(v.getId()==R.id.uploadProfilePicTcID){
            someActivityResultLauncher.launch("image/*");
        }
    }

    ActivityResultLauncher<String> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        circleImageView.setImageURI(result);
                        uriProfileImage = result;
                        uploadImageToFirebase();
                    }
                }
            });

    private void uploadImageToFirebase(){
        dialog.setMessage("Uploading.....");
        dialog.show();

        image_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName() + System.currentTimeMillis();
        storageReference = FirebaseStorage.getInstance()
                .getReference("profile images/" + image_name + ".jpg");

        if(uriProfileImage!=null){
            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageUrl = uri.toString();
                            saveUserInfo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {}
            });
        }
    }

    private void saveUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null && profileImageUrl!=null){
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(profileImageUrl)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {}
            });

            storeImageMethod(profileImageUrl);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeImageMethod(String profileImageUrl){
        StoreTeacherImage storeTeacherImage = new StoreTeacherImage(profileImageUrl);
        imageDatabaseReference.child(userPhone).setValue(storeTeacherImage);
    }

    private void logoutAppTc(){
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Logout !");
        alertDialogBuilder.setMessage("Are you sure you want to logout ?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nullValue = "";
                setNullDataMethod(nullValue);

                mAuth.getInstance().signOut();
                getActivity().finish();
                Intent it = new Intent(getActivity(), SplashScreen.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setNullDataMethod(String nullValue){
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput("Teacher_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(nullValue.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
