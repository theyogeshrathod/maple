package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.NewsModel;
import com.coolapps.yo.maple.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for Free news
 */
public class NewsFragment extends BaseFragment {
    private static final String NEWS_ARGS = "news_arg";
    private static final String TAG = "FreeNewsFragment";
    private RecyclerView mNewsRecyclerView;
    private List<NewsModel> mNewsList = new ArrayList<>();
    private NewsAdapter mNewsAdapter;

    public static NewsFragment newInstance(@NonNull ArrayList<NewsModel> newsModels) {
        final Bundle args = new Bundle();
        args.putParcelableArrayList(NEWS_ARGS, newsModels);
        final NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();

        if (args != null) {
            mNewsList = args.getParcelableArrayList(NEWS_ARGS);
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        if (view != null) {
            init(view);
            mNewsAdapter.setData(mNewsList);
        }
        return view;
    }

    private void init(@NonNull View view) {
        mNewsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        mNewsRecyclerView.setHasFixedSize(true);
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNewsAdapter = new NewsAdapter();
        mNewsRecyclerView.setAdapter(mNewsAdapter);
    }
}
