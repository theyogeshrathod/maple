package com.coolapps.yo.maple;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.activity.LoginActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Manager class to manage login.
 */
public final class LoginManager {

    /**
     * Utility class. Should not be instantiated.
     */
    private LoginManager() { }

    public static void signOut(@NonNull Context context) {
        AuthUI.getInstance().signOut(context).addOnCompleteListener(task -> {
            final Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Nullable
    public static FirebaseUser getLoggedInUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    /**
     * This method returns the auth provider used while login to app
     * @return : google.com for Login with gmail, and phone for Login with phone
     */
    public static String getAuthProvider() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null && firebaseUser.getProviderData().size() > 0) {
            return firebaseUser.getProviderData().get(firebaseUser.getProviderData().size() - 1).getProviderId();
        }
        return "anonymous";
    }
}
