package com.coolapps.yo.maple.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.LoginTypeSelectionFragment;

/**
 * This is a Login Activity.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Fragment fragment = LoginTypeSelectionFragment.newInstance();
        getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());
        super.onCreate(savedInstanceState);

        setToolbarTitle(R.string.login_title);
    }
}
