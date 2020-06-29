package com.coolapps.yo.maple.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.HomeSettingsFragment;
import com.coolapps.yo.maple.fragment.ProfileFragment;
import com.coolapps.yo.maple.fragment.UserHomeFragment;
import com.google.firebase.auth.FirebaseUser;

/*
 * News Activity for showing news to user
 * */
public class NewsActivity extends BaseActivity {

    private static final String TAG = "NewsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FirebaseUser firebaseUser = LoginManager.getLoggedInUser();
        Fragment fragment = null;
        if (firebaseUser != null) {

            if (MapleDataModel.getInstance().fetchProfileData(firebaseUser.getUid()) != null) {
                Log.d(TAG, "onCreate: go to news");
                fragment = UserHomeFragment.newInstance();
            } else {
                Log.d(TAG, "onCreate: go to profile");
                fragment = ProfileFragment.newInstance();
            }

            getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());
            super.onCreate(savedInstanceState);

            setShowBackButton(false);
            setShowSettingsButton(true);
            setShowHomeButton(true);
            setToolbarSettingsClickListener(v -> onFragmentChange(HomeSettingsFragment.newInstance()));
            setToolbarHomeClickListener(v -> onFragmentChange(UserHomeFragment.newInstance()));
            setToolbarTitle(R.string.home_text);
        }
    }
}
