package com.coolapps.yo.maple.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.coolapps.yo.maple.R;

/**
 * This is a full screen dialog fragment used to show loading.
 */
public class LoadingFragment extends DialogFragment {

    private TextView mBall1;

    public static LoadingFragment newInstance() {
        Bundle args = new Bundle();
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.loading_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBall1 = view.findViewById(R.id.ball1);
        setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();

        final ObjectAnimator ballAnimator = ObjectAnimator.ofFloat(mBall1, "translationY", -200f, 0f);
        ballAnimator.setInterpolator(new BounceInterpolator());
        ballAnimator.setDuration(3000);
        ballAnimator.start();
        ballAnimator.setRepeatCount(Animation.INFINITE);
    }
}
