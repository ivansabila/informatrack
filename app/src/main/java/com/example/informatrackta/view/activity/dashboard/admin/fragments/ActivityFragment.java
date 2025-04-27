package com.example.informatrackta.view.activity.dashboard.admin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.informatrackta.R;

public class ActivityFragment extends Fragment {
    private LinearLayout llUserSubmission;
    private TextView tvUserNotApproved, tvUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);

        initComponents(view);

        return view;
    }

    private void initComponents(View view) {
        llUserSubmission = view.findViewById(R.id.llUserSubmission);
        tvUserNotApproved = view.findViewById(R.id.tvUserNotApproved);
        tvUser = view.findViewById(R.id.tvUser);
    }
}