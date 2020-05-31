package com.coolapps.yo.maple.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.coolapps.yo.maple.MockNewsData;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.NewsFragment;

public class NewsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[]{R.string.tab_text_1, R.string.tab_text_2};

    private Context mContext;

    public NewsPagerAdapter(Context context, @NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        MockNewsData mockNewsData = new MockNewsData();
        if (position == 0) {
            return NewsFragment.newInstance(mockNewsData.getFreeNewsData());
        } else if (position == 1) {
            return NewsFragment.newInstance(mockNewsData.getPremiumNewsData());
        }
        return NewsFragment.newInstance(mockNewsData.getFreeNewsData());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}