package com.school.manager.About_and_Profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.school.manager.AppAction.GuardianMainActivity;
import com.school.manager.AppAction.SplashScreen;
import com.school.manager.Authentication.ResetPassword;
import com.school.manager.BackFromFragment.BackListenerFragment;
import com.school.manager.ModelClasses.StoreGuardianImage;
import com.school.manager.R;
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

public class ProfileGdActivity extends Fragment implements View.OnClickListener, BackListenerFragment {

    public static BackListenerFragment backBtnListener;
    LinearLayout editPass;
    View views, parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Snackbar snackbar;
    DatabaseReference databaseReference, imageDatabaseReference;
    StorageReference storageReference;
    CircleImageView circleImageView, backBtn, editProfilePic;
    TextView nameText, emailText, phoneText;
    String userPhone, image_name, profileImageUrl, userEmail;
    ProgressBar progressBar;
    Fragment fragment;
    Button logoutBtn;
    FirebaseAuth mAuth;
    FragmentTransaction fragmentTransaction;
    private Uri uriProfileImage;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_profile_gd, container, false);

        dialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();

        circleImageView = views.findViewById(R.id.profilePicGdID);
        circleImageView.setOnClickListener(this);
        backBtn = views.findViewById(R.id.backFromProfileGdId);
        backBtn.setOnClickListener(this);
        logoutBtn = views.findViewById(R.id.logoutGdID);
        logoutBtn.setOnClickListener(this);
        editPass = views.findViewById(R.id.changePasswordGdID);
        editPass.setOnClickListener(this);
        editProfilePic = views.findViewById(R.id.uploadProfilePicGdID);
        editProfilePic.setOnClickListener(this);

        nameText = views.findViewById(R.id.profileNameGdID);
        emailText = views.findViewById(R.id.profileEmailGdID);
        phoneText = views.findViewById(R.id.profilePhoneGdID);

        progressBar = views.findViewById(R.id.profileProgressbarGdId);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("Guardian Information");
        imageDatabaseReference = FirebaseDatabase.getInstance().getReference("Guardian Images");

        parentLayout = views.findViewById(android.R.id.content);
        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getGuardianData();
        } else {
            snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();
        }

        return views;
    }

    private void getGuardianData(){
        userPhone = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

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
                userEmail = dataSnapshot.getValue(String.class);
                emailText.setText(" " + userEmail);
                phoneText.setText(" " + userPhone);

                imageDatabaseReference.child(userPhone).child("avatar").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            Picasso.get().load(snapshot.getValue().toString()).into(circleImageView);
                            progressBar.setVisibility(View.GONE);

                        } catch (Exception e){
                            Log.i("Error", e.getMessage());
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        fragment = new GuardianMainActivity();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.backFromProfileGdId){
            fragment = new GuardianMainActivity();
            fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
            fragmentTransaction.replace(R.id.fragmentGdID, fragment, "MY_FRAGMENT");
            fragmentTransaction.commit();
        }

        if(v.getId()==R.id.logoutGdID){
            logoutAppGd();
        }

        if(v.getId()==R.id.uploadProfilePicGdID){
            someActivityResultLauncher.launch("image/*");
        }

        if(v.getId()==R.id.changePasswordGdID){
            Bundle armgs = new Bundle();
            armgs.putString("email_key", userEmail);

            ResetPassword resetPassword = new ResetPassword();
            resetPassword.setArguments(armgs);
            resetPassword.show(getActivity().getSupportFragmentManager(), "Sample dialog");
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

        image_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
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
                            Toast.makeText(getActivity(), "Could not upload", Toast.LENGTH_LONG).show();
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
            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeImageMethod(String profileImageUrl){
        StoreGuardianImage storeGuardianImage = new StoreGuardianImage(profileImageUrl);
        imageDatabaseReference.child(userPhone).setValue(storeGuardianImage);
    }

    private void logoutAppGd(){
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

                mAuth.signOut();
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
            FileOutputStream fileOutputStream = getActivity().openFileOutput("Guardian_Info.txt", Context.MODE_PRIVATE);
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
