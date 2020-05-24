package com.coolapps.yo.maple.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Base class for all the fragments.
 */
abstract class BaseFragment extends Fragment {

    /**
     * Logging tag to be used for this class.
     */
    private static final String TAG = "BaseFragment";

    private OnFragmentChangeListener mOnFragmentChangeListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach() " + this);

        mOnFragmentChangeListener = resolveHostInterface(this, OnFragmentChangeListener.class);
    }

    /**
     * This methods a reference of the fragment or activity which has implemented {@link OnFragmentChangeListener}
     * Returns null if the parent fragment or activity does not implement {@link OnFragmentChangeListener}.
     * @param fragment the fragment
     * @param callbackClass {@link OnFragmentChangeListener} class
     * @return {@link OnFragmentChangeListener} if callbackClass is implemented by parent fragment or activity, null otherwise.
     */
    @Nullable
    private OnFragmentChangeListener resolveHostInterface (@NonNull Fragment fragment, @NonNull Class<OnFragmentChangeListener> callbackClass) {
        final Fragment parentFragment = fragment.getParentFragment();
        if (parentFragment != null && callbackClass.isAssignableFrom(parentFragment.getClass())) {
            return callbackClass.cast(parentFragment);
        }

        final Activity parentActivity = fragment.getActivity();
        if (parentActivity != null && callbackClass.isAssignableFrom(parentActivity.getClass())) {
            return callbackClass.cast(parentActivity);
        }

        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() " + this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = super.onCreateView(inflater, container, savedInstanceState);
        Log.d(TAG, "onCreateView() " + this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated() " + this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated() " + this);
    }

    /**
     * This methods replaces current fragment with the provided fragment.
     * @param fragment the fragment to replace
     */
    protected void showFragment(@NonNull Fragment fragment) {
        if (mOnFragmentChangeListener != null) {
            mOnFragmentChangeListener.onFragmentChange(fragment);
        } else {
            Log.e(TAG, "Cannot find Host interface");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() " + this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() " + this);
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause() " + this);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState() " + this);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored() " + this);
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop() " + this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView() " + this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() " + this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach() " + this);

        mOnFragmentChangeListener = null;
        super.onDetach();
    }
}
