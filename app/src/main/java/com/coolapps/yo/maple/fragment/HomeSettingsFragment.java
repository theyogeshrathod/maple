package com.coolapps.yo.maple.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This is a Home Settings screen.
 */
public class HomeSettingsFragment extends BaseFragment {

    public static HomeSettingsFragment newInstance() {
        final Bundle args = new Bundle();
        HomeSettingsFragment fragment = new HomeSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_settings_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.sign_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(requireActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        final Intent intent = new Intent(requireActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    private void updateToolbar() {
        setToolbarTitle(R.string.home_settings_title);
    }

}
