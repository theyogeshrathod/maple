package com.coolapps.yo.maple.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.coolapps.yo.maple.fragment.HomeFragment;
import com.coolapps.yo.maple.fragment.HomeSettingsFragment;

/**
 * This is a Home Activity.
 */
public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final Fragment fragment = HomeFragment.newInstance();
        getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());

        super.onCreate(savedInstanceState);
        setShowBackButton(true);
        setShowSettingsButton(true);
        setToolbarSettingsClickListener(v -> onFragmentChange(HomeSettingsFragment.newInstance()));
    }
}
