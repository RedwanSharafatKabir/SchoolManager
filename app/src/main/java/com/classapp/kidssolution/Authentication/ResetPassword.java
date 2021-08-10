package com.classapp.kidssolution.Authentication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.classapp.kidssolution.ModelClasses.StoreClassesData;
import com.classapp.kidssolution.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPassword extends AppCompatDialogFragment implements View.OnClickListener{

    ProgressBar progressBar;
    EditText passEdit, oldPass;
    View view;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    String userEmail;
    TextView reset, no;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.reset_password, null);
        builder.setView(view).setCancelable(false).setTitle("Reset password ?");

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        progressBar = view.findViewById(R.id.progressBarId);
        progressBar.setVisibility(View.GONE);
        passEdit = view.findViewById(R.id.enterNewPasswordID);
        oldPass = view.findViewById(R.id.enterOldPasswordID);

        no = view.findViewById(R.id.notChangeId);
        no.setOnClickListener(this);
        reset = view.findViewById(R.id.resetPassId);
        reset.setOnClickListener(this);

        Bundle mArgs = getArguments();
        userEmail = mArgs.getString("email_key");

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        String newPassword = passEdit.getText().toString();
        String oldPassword = oldPass.getText().toString();

        if(v.getId()==R.id.resetPassId){
            progressBar.setVisibility(View.VISIBLE);

            if(oldPassword.isEmpty()){
                oldPass.setError("Enter old password");
                progressBar.setVisibility(View.GONE);
            }

            if(newPassword.length() < 8) {
                passEdit.setError("Password must be minimum 8 characters");
                progressBar.setVisibility(View.GONE);
            }

            else {
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    AuthCredential credential = EmailAuthProvider.getCredential(userEmail, oldPassword);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    getDialog().dismiss();

                                                } else {
                                                    Toast.makeText(getActivity(), "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), "Password Reset Failed", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getActivity(), "Turn on internet connection", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        if(v.getId()==R.id.notChangeId){
            getDialog().dismiss();
            progressBar.setVisibility(View.GONE);
        }
    }

}
