package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.coolapps.yo.maple.R;

/**
 * This is a test World Fragment.
 * TODO: Remove later?
 */
public class WorldFragment extends BaseFragment {

    @NonNull
    public static WorldFragment newInstance() {
        return new WorldFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hello_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.text)).setText("World!");
        view.findViewById(R.id.next).setVisibility(View.GONE);
    }
}
