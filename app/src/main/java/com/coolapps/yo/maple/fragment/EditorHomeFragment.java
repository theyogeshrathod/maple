package com.coolapps.yo.maple.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.ArticleTags;
import com.coolapps.yo.maple.MapleAlerts;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.widget.MapleButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.coolapps.yo.maple.widget.MapleEditText;

/**
 * Home screen for Editors.
 */
public class EditorHomeFragment extends BaseFragment {

    private static final String TAG = "EditorHomeFragment";

    private static final String LOADING_TAG = "loading_tag";

    private static final String ARTICLES_KEY = "Articles";

    private static final String DEBUG_KEY = "debug";
    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";
    private static final String ID_KEY = "id";
    private static final String ARTICLE_TYPE_KEY = "articleType";
    private static final String TIME_IN_MILLIS_KEY = "timeInMillis";
    private static final String PENDING_FOR_APPROVAL_KEY = "pendingForApproval";
    private static final String IMAGE_URI_KEY = "imageUri";
    private static final String ARTICLE_AREA_KEY = "articleArea";

    private static final int IMAGE_PICK_REQUEST_ID = 100;

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private LoadingFragment mLoadingFragment = LoadingFragment.newInstance();

    private Button mUploadImageButton;
    private MapleButton mSubmitButton;
    private MapleEditText mTitleEditText;
    private MapleEditText mDescriptionEditText;
    private TextView mImageUploadedTextView;
    private Uri mImageDownloadUri;

    private RadioButton mFreeRadioButton;
    private RadioButton mPaidRadioButton;
    private RadioButton mKnowledgeRadioButton;
    private RadioButton mProjectsRadioButton;

    private RadioButton mBusinessRadioButton;
    private RadioButton mManufacturingRadioButton;
    private RadioButton mServiceRadioButton;
    private RadioButton mInternationalRadioButton;
    private RadioButton mTechnologyRadioButton;
    private RadioButton mStartUpRadioButton;
    private RadioButton mStockMarketRadioButton;
    private RadioButton mInnovationRadioButton;
    private RadioButton mSuccessStoryRadioButton;
    private RadioButton mEconomicsRadioButton;
    private RadioButton mImportExportRadioButton;

    private ArticleContentType mSelectedArticleType = ArticleContentType.FREE;
    private ArticleTags mSelectedArticleTag = ArticleTags.BUSINESS;

    private View.OnClickListener mOnRadioButtonClickedListener = v -> {
        if (v instanceof RadioButton) {
            final boolean isChecked = ((RadioButton) v).isChecked();
            if (isChecked) {
                switch (v.getId()) {
                    case R.id.free_radio_button: {
                        mSelectedArticleType = ArticleContentType.FREE;
                        break;
                    }
                    case R.id.paid_radio_button: {
                        mSelectedArticleType = ArticleContentType.PAID;
                        break;
                    }
                    case R.id.knowledge_radio_button: {
                        mSelectedArticleType = ArticleContentType.KNOWLEDGE;
                        break;
                    }
                    case R.id.projects_radio_button: {
                        mSelectedArticleType = ArticleContentType.PROJECTS;
                        break;
                    }
                    case R.id.business_radio_button: {
                        mSelectedArticleTag = ArticleTags.BUSINESS;
                        break;
                    }
                    case R.id.manufacturing_radio_button: {
                        mSelectedArticleTag = ArticleTags.MANUFACTURING;
                        break;
                    }
                    case R.id.service_radio_button: {
                        mSelectedArticleTag = ArticleTags.SERVICE;
                        break;
                    }
                    case R.id.international_radio_button: {
                        mSelectedArticleTag = ArticleTags.INTERNATIONAL;
                        break;
                    }
                    case R.id.technology_radio_button: {
                        mSelectedArticleTag = ArticleTags.TECHNOLOGY;
                        break;
                    }
                    case R.id.start_up_radio_button: {
                        mSelectedArticleTag = ArticleTags.START_UP;
                        break;
                    }
                    case R.id.stock_market_radio_button: {
                        mSelectedArticleTag = ArticleTags.STOCK_MARKET;
                        break;
                    }
                    case R.id.innovation_radio_button: {
                        mSelectedArticleTag = ArticleTags.INNOVATION;
                        break;
                    }
                    case R.id.success_story_radio_button: {
                        mSelectedArticleTag = ArticleTags.SUCCESS_STORY;
                        break;
                    }
                    case R.id.economics_radio_button: {
                        mSelectedArticleTag = ArticleTags.ECONOMICS;
                        break;
                    }
                    case R.id.import_export_radio_button: {
                        mSelectedArticleTag = ArticleTags.IMPORT_EXPORT;
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        Log.d(TAG, "Selected mSelectedArticleType: " + mSelectedArticleType + ", selected mSelectedArticleTag: " + mSelectedArticleTag);
    };

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
        mImageUploadedTextView = view.findViewById(R.id.image_uploaded_text);

        mUploadImageButton = view.findViewById(R.id.image_upload_button);
        mUploadImageButton.setOnClickListener(v -> {
            final Intent imagePickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            imagePickerIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(imagePickerIntent, "Select Picture"), IMAGE_PICK_REQUEST_ID);
        });

        mSubmitButton = view.findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(v -> {
            submitData();
        });

        mFreeRadioButton = view.findViewById(R.id.free_radio_button);
        mFreeRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mPaidRadioButton = view.findViewById(R.id.paid_radio_button);
        mPaidRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mKnowledgeRadioButton = view.findViewById(R.id.knowledge_radio_button);
        mKnowledgeRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mProjectsRadioButton = view.findViewById(R.id.projects_radio_button);
        mProjectsRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mBusinessRadioButton = view.findViewById(R.id.business_radio_button);
        mBusinessRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mManufacturingRadioButton = view.findViewById(R.id.manufacturing_radio_button);
        mManufacturingRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mServiceRadioButton = view.findViewById(R.id.service_radio_button);
        mServiceRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mInternationalRadioButton = view.findViewById(R.id.international_radio_button);
        mInternationalRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mTechnologyRadioButton = view.findViewById(R.id.technology_radio_button);
        mTechnologyRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mStartUpRadioButton = view.findViewById(R.id.start_up_radio_button);
        mStartUpRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mStockMarketRadioButton = view.findViewById(R.id.stock_market_radio_button);
        mStockMarketRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mInnovationRadioButton = view.findViewById(R.id.innovation_radio_button);
        mInnovationRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mSuccessStoryRadioButton = view.findViewById(R.id.success_story_radio_button);
        mSuccessStoryRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mEconomicsRadioButton = view.findViewById(R.id.economics_radio_button);
        mEconomicsRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mImportExportRadioButton = view.findViewById(R.id.import_export_radio_button);
        mImportExportRadioButton.setOnClickListener(mOnRadioButtonClickedListener);

        mFreeRadioButton.setChecked(true);
        mBusinessRadioButton.setChecked(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST_ID) {
            if (data != null) {
                showLoadingFragment();

                final Uri pickedImage = data.getData();
                if (pickedImage != null) {
                    final Bitmap bitmap = getBitmap(pickedImage, requireActivity().getContentResolver());
                    if (bitmap != null) {
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        final byte[] byteData = baos.toByteArray();

                        final StorageReference imageRef = mStorageRef.child("/images/" + UUID.randomUUID() + ".png");
                        final UploadTask uploadTask = imageRef.putBytes(byteData);
                        uploadTask.addOnSuccessListener(taskSnapshot -> Log.d(TAG, "Image uploaded successfully"));
                        uploadTask.addOnFailureListener(e -> Log.e(TAG, "Image uploade failed", e));

                        uploadTask.continueWithTask(task -> {
                            if (!task.isSuccessful()) {
                                Log.d(TAG, "uploadTask.continueWithTask failed");
                                hideLoadingFragment();
                            }
                            return imageRef.getDownloadUrl();
                        }).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                mImageDownloadUri = task.getResult();
                                if (mImageDownloadUri != null) {
                                    mUploadImageButton.setVisibility(View.GONE);
                                    mImageUploadedTextView.setVisibility(View.VISIBLE);
                                }
                                Log.d(TAG, "Successfully got download Uri: " + task.getResult());
                            } else {
                                Log.e(TAG, "Failed to get download Uri");
                            }
                            hideLoadingFragment();
                            if (!isStateSaved()) {
                                MapleAlerts.createSuccessfullyUploadedAlert(requireContext(), null).show();
                            }
                        });
                    }
                }
            }
        }
    }

    @Nullable
    private Bitmap getBitmap(@NonNull Uri file, @NonNull ContentResolver contentResolver) {
        Bitmap bitmap = null;
        try {
            final InputStream inputStream = contentResolver.openInputStream(file);
            bitmap = BitmapFactory.decodeStream(inputStream);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (@NonNull IOException io) {
                Log.e(TAG, "Error closing stream", io);
            }
        } catch (@NonNull FileNotFoundException e) {
            Log.e(TAG, "Error opening file", e);
        }
        return bitmap;
    }

    private void submitData() {
        showLoadingFragment();

        final String timeInMillis = String.valueOf(System.currentTimeMillis());
        final String id = UUID.randomUUID().toString();

        final Map<String, String> data = new HashMap<>();
        data.put(ID_KEY, id);
        data.put(DEBUG_KEY, "true");
        data.put(TITLE_KEY, getSpannedHtmlText(mTitleEditText.getText().toString().trim()));
        data.put(DESCRIPTION_KEY, getSpannedHtmlText(mDescriptionEditText.getText().toString().trim()));
        data.put(ARTICLE_TYPE_KEY, String.valueOf(mSelectedArticleType.getValue()).trim());
        data.put(TIME_IN_MILLIS_KEY, timeInMillis);
        data.put(PENDING_FOR_APPROVAL_KEY, "true");
        data.put(ARTICLE_AREA_KEY, mSelectedArticleTag.getId());
        data.put(IMAGE_URI_KEY, mImageDownloadUri != null ? mImageDownloadUri.toString() : "");

        mFirestore.collection(ARTICLES_KEY).document(id).set(data)
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

    private String getSpannedHtmlText(@NonNull CharSequence spannedText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.toHtml(new SpannableString(spannedText), Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL).trim();
        } else {
            return Html.toHtml(new SpannableString(spannedText)).trim();
        }
    }

    private void clearFields() {
        mImageDownloadUri = null;
        mTitleEditText.setText(null);
        mDescriptionEditText.setText(null);
        mUploadImageButton.setVisibility(View.VISIBLE);
        mImageUploadedTextView.setVisibility(View.GONE);
        mFreeRadioButton.setChecked(true);
        mBusinessRadioButton.setChecked(true);
    }

    private void showLoadingFragment() {
        if (!isStateSaved() && !mLoadingFragment.isAdded()) {
            mLoadingFragment.show(getChildFragmentManager(), LOADING_TAG);
        }
    }

    private void hideLoadingFragment() {
        if (mLoadingFragment.isAdded()) {
            mLoadingFragment.dismissAllowingStateLoss();
        }
    }
}
