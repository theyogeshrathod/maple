package com.coolapps.yo.maple.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.OnFragmentChangeListener;

/**
 * Base activity which should be extended by all the activities.
 */
public abstract class BaseActivity extends AppCompatActivity implements OnFragmentChangeListener {

    /**
     * Logging tag to be used for this class.
     */
    private static final String TAG = "BaseActivity";

    /**
     * Tag used perform transactions on current fragment.
     */
    private static final String CONTENT_FRAGMENT = "content_fragment";

    /**
     * Argument used to instantiate fragment class. Ex. xxFragment.class.getName().
     */
    public static final String FRAGMENT_CLASS = "fragment_class";

    /**
     * Arguments key for which arguments should be passed for the fragment.
     */
    public static final String FRAGMENT_ARGS = "fragment_args";

    private TextView mToolbarTitle;
    private ImageButton mToolbarBackButton;
    private ImageButton mToolbarSettingsButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() " + this);

        getWindow().setFormat(PixelFormat.RGBA_8888);

        setContentView(R.layout.base_layout);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final Intent intent = getIntent();

        if (intent != null) {
            final Fragment currentFragment = fragmentManager.findFragmentByTag(CONTENT_FRAGMENT);
            if (currentFragment == null) {
                final String fragmentClass = intent.getStringExtra(FRAGMENT_CLASS);
                if (fragmentClass != null) {
                    setFragment(Fragment.instantiate(this, fragmentClass, intent.getBundleExtra(FRAGMENT_ARGS)));
                }
            }
        }

        mToolbarSettingsButton = findViewById(R.id.toolbar_settings_button);
        mToolbarTitle = findViewById(R.id.toolbar_title);
        mToolbarBackButton = findViewById(R.id.toolbar_back_button);
        mToolbarBackButton.setOnClickListener(v -> super.onBackPressed());
    }

    /**
     * Method which should be called from Activity to set initial fragment.
     * @param fragment the fragment to be added
     */
    protected void setFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager().popBackStack();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, fragment, CONTENT_FRAGMENT);
        fragmentTransaction.commit();
    }

    protected void setToolbarTitle(@NonNull String title) {
        mToolbarTitle.setText(title);
    }

    protected void setToolbarTitle(@StringRes int res) {
        mToolbarTitle.setText(res);
    }

    protected void setToolbarBackClickListener(View.OnClickListener listener) {
        mToolbarBackButton.setOnClickListener(listener);
    }

    protected void setToolbarSettingsClickListener(View.OnClickListener listener) {
        mToolbarSettingsButton.setOnClickListener(listener);
    }

    protected void setShowBackButton(boolean show) {
        mToolbarBackButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    protected void setShowSettingsButton(boolean show) {
        mToolbarSettingsButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() " + this);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState() " + this);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() " + this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() " + this);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() " + this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop() " + this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy() " + this);
        super.onDestroy();
    }

    @Override
    public void onFragmentChange(@NonNull Fragment fragment) {
        if (getSupportFragmentManager().isStateSaved()) {
            Log.e(TAG, "Cannot change fragment because activity state is already saved");
            return;
        }

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment, CONTENT_FRAGMENT);
        transaction.commit();
    }

    @Override
    public void onBackButtonPressed() {
        super.onBackPressed();
    }

    @Override
    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }
}
