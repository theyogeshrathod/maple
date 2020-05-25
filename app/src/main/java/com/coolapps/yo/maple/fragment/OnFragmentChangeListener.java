package com.coolapps.yo.maple.fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * The fragment change listener
 */
public interface OnFragmentChangeListener {

    /**
     * Method to call to change the fragment.
     * @param fragment the fragment to change to
     */
    void onFragmentChange(@NonNull Fragment fragment);

    /**
     * This method simulates the back button pressed of the device
     */
    void onBackButtonPressed();

    /**
     * This method pops one item from the back-stack.
     */
    void popBackStack();
}
