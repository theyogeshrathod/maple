package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.R;

/**
 * This is a Home Fragment.
 */
public class HomeFragment extends BaseFragment {

    @NonNull
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hello_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) view.findViewById(R.id.text)).setText("Hello");
        view.findViewById(R.id.next).setOnClickListener(v -> showFragment(WorldFragment.newInstance()));
    }
    @Override
    public void onResume() {
        super.onResume();
        updateToolbar();
    }

    private void updateToolbar() {
        setToolbarTitle(R.string.home_title);
    }

}
