package com.coolapps.yo.maple.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.HomeSettingsFragment;
import com.coolapps.yo.maple.fragment.UserHomeFragment;

/*
 * News Activity for showing news to user
 * */
public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Fragment fragment = UserHomeFragment.newInstance();
        getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());

        super.onCreate(savedInstanceState);

        setShowBackButton(false);
        setShowSettingsButton(true);
        setToolbarSettingsClickListener(v -> onFragmentChange(HomeSettingsFragment.newInstance()));
        setToolbarTitle(R.string.home_text);
    }
}
