package com.coolapps.yo.maple.activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.AdminHomeFragment;
import com.coolapps.yo.maple.fragment.EditorHomeFragment;
import com.coolapps.yo.maple.fragment.HomeSettingsFragment;
import com.google.firebase.auth.FirebaseUser;

/**
 * This is a Home Activity.
 */
public class HomeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final FirebaseUser currentUser = LoginManager.getLoggedInUser();

        if (currentUser != null) {
            final Fragment fragment;
            final int toolbarTextRes;
            // Hard-coded Admin email for now.
            if ("yogesh.rathod04@gmail.com".equalsIgnoreCase(currentUser.getEmail())) {
                fragment = AdminHomeFragment.newInstance();
                toolbarTextRes = R.string.admin_text;
            } else {
                fragment = EditorHomeFragment.newInstance();
                toolbarTextRes = R.string.upload_article_text;
            }
            getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());
            super.onCreate(savedInstanceState);

            setShowBackButton(true);
            setShowSettingsButton(true);
            setToolbarSettingsClickListener(v -> onFragmentChange(HomeSettingsFragment.newInstance()));
            setToolbarTitle(toolbarTextRes);
        } else {
            LoginManager.signOut(this);
        }
    }
}
