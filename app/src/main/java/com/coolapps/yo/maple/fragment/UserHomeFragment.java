package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.adapter.ArticlesAdapter;
import com.coolapps.yo.maple.adapter.NewsPagerAdapter;
import com.coolapps.yo.maple.model.TagInterestsModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

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

        final ViewPager viewPager = view.findViewById(R.id.view_pager);
        final TabLayout tabs = view.findViewById(R.id.tabs);
        final RecyclerView mArticlesRecyclerView = view.findViewById(R.id.articlesRecyclerView);

        final NewsPagerAdapter newsPagerAdapter = new NewsPagerAdapter(requireActivity(), getChildFragmentManager(), null);
        viewPager.setAdapter(newsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        final ArticlesAdapter articlesAdapter = new ArticlesAdapter(requireContext(), tagId -> {
            newsPagerAdapter.setArticleTagId("0".equalsIgnoreCase(tagId) ? null : tagId);
        });

        final List<TagInterestsModel> tagsList = new ArrayList<>(MapleDataModel.getInstance().getAvailableTags());
        articlesAdapter.setTagsList(tagsList);
        mArticlesRecyclerView.setAdapter(articlesAdapter);
        mArticlesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        final DividerItemDecoration decoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL);
        decoration.setDrawable(requireContext().getResources().getDrawable(R.drawable.article_divider_drawable));
        mArticlesRecyclerView.addItemDecoration(decoration);
    }
}
