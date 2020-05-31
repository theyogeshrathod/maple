package com.coolapps.yo.maple.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.AccountTypeEnum;
import com.coolapps.yo.maple.LoginManager;
import com.coolapps.yo.maple.MapleAlerts;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.HomeActivity;
import com.coolapps.yo.maple.widget.MapleButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a Login type selection fragment.
 */
public class LoginTypeSelectionFragment extends BaseFragment {

    private static final String TAG = "LoginTypeSelectionFragment";

    private static final String COLLECTION_USER_TYPE = "UserType";
    private static final String USER_TYPE = "Type";

    private MapleButton mAdminButton;
    private MapleButton mEditorButton;
    private MapleButton mUserButton;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    public static LoginTypeSelectionFragment newInstance() {
        final Bundle args = new Bundle();
        LoginTypeSelectionFragment fragment = new LoginTypeSelectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_type_selection_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdminButton = view.findViewById(R.id.admin_login_button);
        mEditorButton = view.findViewById(R.id.editor_login_button);
        mUserButton = view.findViewById(R.id.user_login_button);

        mAdminButton.setOnClickListener(v -> addUserType(AccountTypeEnum.ADMIN));
        mEditorButton.setOnClickListener(v -> addUserType(AccountTypeEnum.EDITOR));
        mUserButton.setOnClickListener(v -> addUserType(AccountTypeEnum.USER));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addUserType(@NonNull AccountTypeEnum type) {
        final Map<String, String> map = new HashMap<>();
        map.put(USER_TYPE, type.getValue());
        final FirebaseUser user = LoginManager.getLoggedInUser();
        if (user != null) {
            mFirestore.collection(COLLECTION_USER_TYPE).document(LoginManager.getLoggedInUser().getUid()).set(map)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Successfully added user type");
                        launchHomeActivity();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed adding user type", e);
                        MapleAlerts.createSomethingWentWrongAlert(
                                requireContext(),
                                (dialog, which) -> LoginManager.signOut(requireContext())
                        ).show();
                    });
        }
    }

    private void launchHomeActivity() {
        final Intent intent = new Intent(requireActivity(), HomeActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
