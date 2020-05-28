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
import androidx.appcompat.app.AlertDialog;

import com.coolapps.yo.maple.AccountTypeEnum;
import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.HomeActivity;
import com.coolapps.yo.maple.widget.MapleButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a Login type selection fragment.
 */
public class LoginTypeSelectionFragment extends BaseFragment {

    private static final String TAG = "LoginTypeSelectionFragment";

    private static final String COLLECTION_USER_TYPE = "UserType";
    private static final String USER_TYPE = "Type";
    private static final int RC_SIGN_IN = 100;

    private MapleButton mManufacturerButton;
    private MapleButton mTraderButton;
    private MapleButton mServiceProvider;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private AccountTypeEnum mSelectedType = AccountTypeEnum.UNKNOWN;

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
                if (user != null) {
                    getUserType(user);
                }
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

        mManufacturerButton.setOnClickListener(v -> showSignUi(AccountTypeEnum.MANUFACTURER));
        mTraderButton.setOnClickListener(v -> showSignUi(AccountTypeEnum.TRADER));
        mServiceProvider.setOnClickListener(v -> showSignUi(AccountTypeEnum.SERVICE_PROVIDER));
    }

    private void showSignUi(@NonNull AccountTypeEnum type) {
        mSelectedType = type;
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers).build(), RC_SIGN_IN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LoginManager.getLoggedInUser() != null) {
            launchHomeActivity();
        }
    }

    private void signOut() {
        LoginManager.signOut(requireContext());
    }

    private void getUserType(@NonNull FirebaseUser user) {
        mFirestore.collection(COLLECTION_USER_TYPE).document(user.getUid()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    final Map data = documentSnapshot.getData();
                    if (data != null) {
                        final Object type = data.get(USER_TYPE);
                        if (type != null && type.equals(mSelectedType.getValue())) {
                            Log.d(TAG, "User type matched. Launching HomeActivity");
                            launchHomeActivity();
                        } else {
                            Log.e(TAG, "User type mismatch: Selected user type = " + mSelectedType + ", available user type = " + type);
                            final AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
                            dialog.setMessage(R.string.account_type_mismatch_text);
                            dialog.setCancelable(false);
                            dialog.setPositiveButton(R.string.ok_text, (dialog1, which) -> signOut());
                            dialog.show();
                        }
                    } else {
                        Log.e(TAG, "No record found with this user " + user);
                        addUserType(user);
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Failed to get user type for " + user, e));
    }

    private void addUserType(@NonNull FirebaseUser user) {
        final Map<String, String> map = new HashMap<>();
        map.put(USER_TYPE, mSelectedType.getValue());
        mFirestore.collection(COLLECTION_USER_TYPE).document(user.getUid()).set(map)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully added user type");
                    launchHomeActivity();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Failed adding user type");
                    signOut();
                });
    }

    private void launchHomeActivity() {
        final Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
