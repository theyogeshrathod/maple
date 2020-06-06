package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.NewsModel;
import com.coolapps.yo.maple.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for Free news
 */
public class NewsFragment extends BaseFragment {
    private static final String ARTICLE_TYPE_ARGS = "article_type_arg";
    private static final String TAG = "FreeNewsFragment";

    private SwipeRefreshLayout mRoot;
    private RecyclerView mNewsRecyclerView;
    private List<NewsModel> mNewsList = new ArrayList<>();
    private NewsAdapter mNewsAdapter;

    public static NewsFragment newInstance(@NonNull ArticleContentType type) {
        final Bundle args = new Bundle();
        args.putParcelable(ARTICLE_TYPE_ARGS, type);
        final NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRoot = view.findViewById(R.id.root_container);
        mRoot.setOnRefreshListener(() -> {
            MapleDataModel.getInstance().fetchAllNewsData(success -> {
                refreshNewsList();
                mRoot.setRefreshing(false);
            });
        });

        mNewsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewsAdapter = new NewsAdapter();
        mNewsRecyclerView.setAdapter(mNewsAdapter);

        mNewsAdapter.setNewsItemClickListener((view1, position) -> Log.d(TAG, "onNewsItemClick: clicked " + position));
        refreshNewsList();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    private void refreshNewsList() {
        final Bundle args = getArguments();
        if (args != null) {
            if (args.getParcelable(ARTICLE_TYPE_ARGS) == ArticleContentType.FREE) {
                mNewsList = MapleDataModel.getInstance().getFreeNewsModels();
            } else if (args.getParcelable(ARTICLE_TYPE_ARGS) == ArticleContentType.PAID) {
                mNewsList = MapleDataModel.getInstance().getPaidNewsModels();
            }
            mNewsAdapter.setData(mNewsList);
        }
    }
}