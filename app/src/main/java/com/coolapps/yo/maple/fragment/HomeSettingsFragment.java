package com.coolapps.yo.maple.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.R;

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
        view.findViewById(R.id.sign_out_button).setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setCancelable(false);
            builder.setTitle(R.string.are_you_sure_to_logout);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    LoginManager.signOut(requireContext());
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        });

        view.findViewById(R.id.textProfile).setOnClickListener(v -> showFragment(new ProfileFragment()));
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
