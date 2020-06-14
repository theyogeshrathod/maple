package com.coolapps.yo.maple.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.fragment.NewsFragment;

/*
* NewsPager adapter for news tabs
* */
public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4};

    private Context mContext;
    private String mArticleTagId;

    public NewsPagerAdapter(Context context, @NonNull FragmentManager fm, @Nullable String articleTagId) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mArticleTagId = articleTagId;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setArticleTagId(@Nullable String tagId) {
        mArticleTagId = tagId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return NewsFragment.newInstance(ArticleContentType.FREE, mArticleTagId);
        } else if (position == 1) {
            return NewsFragment.newInstance(ArticleContentType.PAID, mArticleTagId);
        } else if (position == 2) {
            return NewsFragment.newInstance(ArticleContentType.KNOWLEDGE, mArticleTagId);
        } else if (position == 3) {
            return NewsFragment.newInstance(ArticleContentType.PROJECTS, mArticleTagId);
        }
        return NewsFragment.newInstance(ArticleContentType.FREE, mArticleTagId);
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