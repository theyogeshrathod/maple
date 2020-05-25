package com.coolapps.yo.maple.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.coolapps.yo.maple.activity.BaseActivity;
import com.coolapps.yo.maple.fragment.HelloFragment;

/**
 * This is a test Hello Activity.
 * TODO: Remove later?
 */
public class HelloActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final Fragment fragment = HelloFragment.newInstance();
        getIntent().putExtra(FRAGMENT_CLASS, fragment.getClass().getName());
        super.onCreate(savedInstanceState);
        setToolbarTitle("My toolbar");
    }
}
