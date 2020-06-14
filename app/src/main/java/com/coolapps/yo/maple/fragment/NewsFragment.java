package com.coolapps.yo.maple.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.MapleAlerts;
import com.coolapps.yo.maple.MapleDataModel;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.NewsModel;
import com.coolapps.yo.maple.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for Free, Paid, Knowledge, Projects news
 */
public class NewsFragment extends BaseFragment {
    private static final String ARTICLE_TYPE_ARGS = "article_type_arg";
    private static final String ARTICLE_TAG_ID_ARGS = "article_tag_id_arg";
    private static final String TAG = "FreeNewsFragment";

    private SwipeRefreshLayout mRoot;
    private RecyclerView mNewsRecyclerView;
    private TextView mNoItemsAvailable;
    private List<NewsModel> mNewsList = new ArrayList<>();
    private NewsAdapter mNewsAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mLoading = false;
    private ArticleContentType mArticleType;
    private String mArticleTagId;

    public static NewsFragment newInstance(@NonNull ArticleContentType type, @Nullable String articleTagId) {
        final Bundle args = new Bundle();
        args.putParcelable(ARTICLE_TYPE_ARGS, type);
        args.putString(ARTICLE_TAG_ID_ARGS, articleTagId);
        final NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        if (args != null) {
            mArticleType = args.getParcelable(ARTICLE_TYPE_ARGS);
            mArticleTagId = args.getString(ARTICLE_TAG_ID_ARGS);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRoot = view.findViewById(R.id.root_container);
        mRoot.setOnRefreshListener(() -> {
            MapleDataModel.getInstance().fetchFirstBatchFreeNewsData((success, newsModels) -> {
                refreshNewsList();
                mRoot.setRefreshing(false);
            });
        });

        mNoItemsAvailable = view.findViewById(R.id.no_items_text_view);
        mNewsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        mNewsRecyclerView.setHasFixedSize(true);

        final SimpleItemAnimator itemAnimator = (SimpleItemAnimator) mNewsRecyclerView.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.setSupportsChangeAnimations(false);
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        mNewsRecyclerView.setLayoutManager(mLayoutManager);

        mNewsAdapter = new NewsAdapter();
        mNewsRecyclerView.setAdapter(mNewsAdapter);

        mNewsAdapter.setNewsItemSeeLessClickListener((view1, position, isSeeMore) -> {
            Log.d(TAG, "onNewsItemClick: clicked " + position);

            final Bundle args = getArguments();
            if (args != null) {
                if (mArticleType == ArticleContentType.PAID) {
                    // TODO: Check if has subscription before showing alert
                    if (false) {
                        MapleAlerts.createNoSubscriptionAlert(requireContext(), null, null, null).show();
                    }
                }

                if (!isSeeMore) {
                    mNewsRecyclerView.smoothScrollToPosition(position);
                }
            }
        });

        refreshNewsList();

        mNewsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final int totalItemCount = mLayoutManager.getItemCount();
                final int lastVisibleItemPos = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (!mLoading && totalItemCount <= lastVisibleItemPos + 1) {
                    fetchMoreNews();
                }
            }
        });
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
            if (mArticleType == ArticleContentType.FREE) {
                mNewsList = MapleDataModel.getInstance().getFreeNewsModels(mArticleTagId);
            } else if (mArticleType == ArticleContentType.PAID) {
                mNewsList = MapleDataModel.getInstance().getPaidNewsModels(mArticleTagId);
            } else if (mArticleType == ArticleContentType.KNOWLEDGE) {
                mNewsList = MapleDataModel.getInstance().getKnowledgeNewsModels(mArticleTagId);
            } else if (mArticleType == ArticleContentType.PROJECTS) {
                mNewsList = MapleDataModel.getInstance().getProjectsNewsModels(mArticleTagId);
            }

            mNewsAdapter.setData(mNewsList);
            if (mNewsList.size() == 0) {
                mNoItemsAvailable.setVisibility(View.VISIBLE);
                mNewsRecyclerView.setVisibility(View.GONE);
            } else {
                mNoItemsAvailable.setVisibility(View.GONE);
                mNewsRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void refreshNewsList(@NonNull List<NewsModel> newsModels) {
        mNewsList.addAll(newsModels);
        mNewsAdapter.addData(newsModels);
    }

    private void fetchMoreNews() {
        mLoading = true;

        final Bundle args = getArguments();
        if (args != null) {
            if (mArticleType == ArticleContentType.FREE) {
                MapleDataModel.getInstance().fetchNextBatchFreeNewsData((success, newsModels) -> {
                    refreshNewsList(newsModels);
                    mLoading = false;
                }, mArticleTagId);
            } else if (mArticleType == ArticleContentType.PAID) {
                MapleDataModel.getInstance().fetchNextBatchPaidNewsData((success, newsModels) -> {
                    refreshNewsList(newsModels);
                    mLoading = false;
                }, mArticleTagId);
            } else if (mArticleType == ArticleContentType.KNOWLEDGE) {
                MapleDataModel.getInstance().fetchNextBatchKnowledgeNewsData((success, newsModels) -> {
                    refreshNewsList(newsModels);
                    mLoading = false;
                }, mArticleTagId);
            } else if (mArticleType == ArticleContentType.PROJECTS) {
                MapleDataModel.getInstance().fetchNextBatchProjectsNewsData((success, newsModels) -> {
                    refreshNewsList(newsModels);
                    mLoading = false;
                }, mArticleTagId);
            }
        }
    }
}
