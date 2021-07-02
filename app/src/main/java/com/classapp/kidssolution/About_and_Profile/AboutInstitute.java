package com.classapp.kidssolution.About_and_Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.classapp.kidssolution.R;

public class AboutInstitute extends Fragment {

    View views;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.activity_about_institute, container, false);

        return views;
    }
}