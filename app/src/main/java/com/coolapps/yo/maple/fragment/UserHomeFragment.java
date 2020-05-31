package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.adapter.NewsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

/**
 * HomeFragment for user
 */
public class UserHomeFragment extends BaseFragment {

    public static UserHomeFragment newInstance() {
        final Bundle args = new Bundle();
        final UserHomeFragment fragment = new UserHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_home_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager != null) {
            final NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(requireActivity(), fragmentManager);
            final ViewPager viewPager = view.findViewById(R.id.view_pager);
            viewPager.setAdapter(newsPagerAdapter);
            final TabLayout tabs = view.findViewById(R.id.tabs);
            tabs.setupWithViewPager(viewPager);
        }
    }
}
