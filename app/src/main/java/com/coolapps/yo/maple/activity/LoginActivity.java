package com.coolapps.yo.maple.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.MapleAlerts;
import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

/**
 * This is a Login Activity.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private static final int RC_SIGN_IN = 100;
    private static final String COLLECTION_USER_TYPE = "UserType";
    private static final String USER_TYPE = "Type";

    private boolean mFirstBatchFreeNewsFetched = false;
    private boolean mFirstBatchPaidNewsFetched = false;
    private boolean mAllArticleTagsFetched = false;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        setToolbarTitle(R.string.login_title);
        if (LoginManager.getLoggedInUser() != null) {
            fetchAllData();
        } else {
            showSignUi();
        }
    }

    private void checkLaunchHomeScreen() {
        if (mFirstBatchFreeNewsFetched && mFirstBatchPaidNewsFetched && mAllArticleTagsFetched) {
            launchNewsActivity();
        }
    }

    private void showSignUi() {
        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers).build(), RC_SIGN_IN);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            final IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == Activity.RESULT_OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "Logged in successfully with user: " + user + ", response: "  + response);
                    fetchAllData();
                } else {
                    Log.d(TAG, "User is null. " + "response: "  + response);
                    showSomethingWentWrongAlert();
                }
            } else {
                Log.e(TAG, "Login failed");
                showSomethingWentWrongAlert();
            }
        }
    }

    private void launchHomeActivity() {
        final Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchNewsActivity() {
        final Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSomethingWentWrongAlert() {
        MapleAlerts.createSomethingWentWrongAlert(
                this,
                (dialog, which) -> LoginManager.signOut(LoginActivity.this)
        ).show();
    }

    private void fetchAllData() {
        fetchFirstBatchOfFreeAndPaidData();
        fetchAllArticlesData();
    }

    private void fetchFirstBatchOfFreeAndPaidData() {
        MapleDataModel.getInstance().fetchFirstBatchFreeNewsData((success, newsModels) -> {
            mFirstBatchFreeNewsFetched = true;
            checkLaunchHomeScreen();
        });
        MapleDataModel.getInstance().fetchFirstBatchPaidNewsData((success, newsModels) -> {
            mFirstBatchPaidNewsFetched = true;
            checkLaunchHomeScreen();
        });
    }

    private void fetchAllArticlesData() {
        MapleDataModel.getInstance().fetchAvailableTags((success, tags) -> {
            mAllArticleTagsFetched = true;
            checkLaunchHomeScreen();
        });
    }
}
