package com.coolapps.yo.maple.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.MapleAlerts;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.widget.MapleButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Home screen for Editors.
 */
public class EditorHomeFragment extends BaseFragment {

    private static final String TAG = "EditorHomeFragment";

    private static final String LOADING_TAG = "loading_tag";

    private static final String ARTICLES_KEY = "Articles";

    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";
    private static final String TIME_IN_MILLIS_KEY = "timeInMillis";
    private static final String PENDING_FOR_APPROVAL_KEY = "pendingForApproval";
    private static final String DOWNLOAD_URI = "downloadUri";

    private static final int IMAGE_PICK_REQUEST_ID = 100;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private LoadingFragment mLoadingFragment = LoadingFragment.newInstance();

    private Button mUploadImageButton;
    private MapleButton mSubmitButton;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private TextView mImagePathTextView;
    private Uri mImageDownloadUrl;

    public static EditorHomeFragment newInstance() {
        final Bundle args = new Bundle();
        EditorHomeFragment fragment = new EditorHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.editor_home_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitleEditText = view.findViewById(R.id.title_edit_text);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mImagePathTextView = view.findViewById(R.id.image_path_text);

        mUploadImageButton = view.findViewById(R.id.image_upload_button);
        mUploadImageButton.setOnClickListener(v -> {
            final Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
            imagePickerIntent.setType("image/*");
            startActivityForResult(imagePickerIntent, IMAGE_PICK_REQUEST_ID);
        });

        mSubmitButton = view.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(v -> {
            saveData();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST_ID) {
            if (data != null && data.getData() != null) {
                showLoadingFragment();
                final StorageReference imageRef = mStorageRef.child("/images/" + UUID.randomUUID() + ".png");

                final UploadTask uploadTask = imageRef.putFile(data.getData());

                uploadTask.addOnSuccessListener(taskSnapshot -> Log.d(TAG, "Image uploaded successfully"));
                uploadTask.addOnFailureListener(e -> Log.e(TAG, "Image uploade failed", e));

                uploadTask.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        Log.d(TAG, "uploadTask.continueWithTask failed");
                    }
                    return imageRef.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mImageDownloadUrl = task.getResult();
                        if (mImageDownloadUrl != null) {
                            mUploadImageButton.setVisibility(View.GONE);
                            mImagePathTextView.setVisibility(View.VISIBLE);
                        }
                        Log.d(TAG, "Successfully got download Uri: " + task.getResult());
                    } else {
                        Log.e(TAG, "Failed to get download Uri");
                    }
                    hideLoadingFragment();
                });
            }
        }
    }

    private void saveData() {
        showLoadingFragment();
        final String timeInMillis = String.valueOf(System.currentTimeMillis());
        final Map<String, String> data = new HashMap<>();
        data.put(TITLE_KEY, mTitleEditText.getText().toString());
        data.put(DESCRIPTION_KEY, mDescriptionEditText.getText().toString());
        data.put(TIME_IN_MILLIS_KEY, timeInMillis);
        data.put(PENDING_FOR_APPROVAL_KEY, "true");
        data.put(DOWNLOAD_URI, mImageDownloadUrl != null ? mImageDownloadUrl.toString() : "");

        mFirestore.collection(ARTICLES_KEY).document(timeInMillis).set(data)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Successfully added article");
                    hideLoadingFragment();
                    MapleAlerts.createSuccessfullySubmittedAlert(requireContext(), null).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed adding article", e);
                    hideLoadingFragment();
                    MapleAlerts.createSomethingWentWrongAlert(requireContext(), null).show();
                    clearFields();
                });
    }

    private void clearFields() {
        mImageDownloadUrl = null;
        mTitleEditText.setText(null);
        mDescriptionEditText.setText(null);
        mUploadImageButton.setVisibility(View.VISIBLE);
        mImagePathTextView.setVisibility(View.GONE);
    }

    private void showLoadingFragment() {
        if (!mLoadingFragment.isAdded()) {
            mLoadingFragment.show(getChildFragmentManager(), LOADING_TAG);
        }
    }

    private void hideLoadingFragment() {
        if (mLoadingFragment.isAdded()) {
            mLoadingFragment.dismiss();
        }
    }
}
