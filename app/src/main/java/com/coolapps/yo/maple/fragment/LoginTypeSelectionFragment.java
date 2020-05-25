package com.coolapps.yo.maple.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.HomeActivity;
import com.coolapps.yo.maple.widget.MapleButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * This is a Login type selection fragment.
 */
public class LoginTypeSelectionFragment extends BaseFragment {

    private static final String TAG = "LoginTypeSelectionFragment";

    private static final int RC_SIGN_IN = 100;

    private MapleButton mManufacturerButton;
    private MapleButton mTraderButton;
    private MapleButton mServiceProvider;

    private View.OnClickListener mOnLoginTypeSelected = v -> {
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), RC_SIGN_IN);
    };

    public static LoginTypeSelectionFragment newInstance() {
        final Bundle args = new Bundle();
        LoginTypeSelectionFragment fragment = new LoginTypeSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "Logged in successfully with user: " + user + ", response: "  + response);
                launchHomeActivity();
            } else {
                Log.e(TAG, "Login failed");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_type_selection_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mManufacturerButton = view.findViewById(R.id.manufacturer_login_button);
        mTraderButton = view.findViewById(R.id.trader_login_button);
        mServiceProvider = view.findViewById(R.id.service_provider_login_button);

        mManufacturerButton.setOnClickListener(mOnLoginTypeSelected);
        mTraderButton.setOnClickListener(mOnLoginTypeSelected);
        mServiceProvider.setOnClickListener(mOnLoginTypeSelected);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            launchHomeActivity();
        }
    }

    private void launchHomeActivity() {
        final Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
