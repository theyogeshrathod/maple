package com.coolapps.yo.maple.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.NewsModel;
import com.coolapps.yo.maple.interfaces.NewsItemSeeMoreClickListener;
import com.coolapps.yo.maple.util.GetDateFromTimestamp;
import com.coolapps.yo.maple.widget.MapleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * NewsAdapter for news list
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsModelItem> mNewsModelList = new ArrayList<>();
    private NewsItemSeeMoreClickListener mNewsItemSeeMoreClickListener;

    public void setNewsItemSeeLessClickListener(@NonNull NewsItemSeeMoreClickListener newsItemSeeMoreClickListener) {
        mNewsItemSeeMoreClickListener = newsItemSeeMoreClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news, parent, false));
    }

    public void setData(@NonNull List<NewsModel> newsModelList) {
        mNewsModelList.clear();
        final List<NewsModelItem> newsModelItems = new ArrayList<>();
        for (NewsModel newsModel: newsModelList) {
            newsModelItems.add(new NewsModelItem(newsModel, false));
        }
        mNewsModelList.addAll(newsModelItems);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<NewsModel> newsModelList) {
        final int prevSize = mNewsModelList.size();
        final List<NewsModelItem> newsModelItems = new ArrayList<>();
        for (NewsModel newsModel: newsModelList) {
            newsModelItems.add(new NewsModelItem(newsModel, false));
        }
        mNewsModelList.addAll(newsModelItems);
        notifyItemRangeInserted(prevSize, newsModelList.size());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mNewsModelList.get(position));

        holder.mSeeFullArticle.setOnClickListener(v -> {
            final boolean isExpanded = mNewsModelList.get(position).isExpanded();

            // TODO: Check subscription for Premium
            mNewsModelList.get(position).setExpanded(!isExpanded);
            notifyItemChanged(position);

            if (mNewsItemSeeMoreClickListener != null) {
                mNewsItemSeeMoreClickListener.onNewsItemClick(v, holder.getAdapterPosition(), !isExpanded);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private MapleImageView mNewsImage;
        private TextView mNewsTitle;
        private TextView mNewsDate;
        private TextView mShortNewsDescription;
        private TextView mFullNewsDescription;
        private TextView mSeeFullArticle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.newsCard);
            mNewsImage = itemView.findViewById(R.id.newsImage);
            mNewsTitle = itemView.findViewById(R.id.newsTitle);
            mNewsDate = itemView.findViewById(R.id.newsDate);
            mShortNewsDescription = itemView.findViewById(R.id.shortNewsDescription);
            mFullNewsDescription = itemView.findViewById(R.id.fullNewsDescription);
            mSeeFullArticle = itemView.findViewById(R.id.see_full_article_text_view);
        }

        void bind(@NonNull NewsModelItem newsModelItem) {
            final NewsModel newsModel = newsModelItem.getNewsModel();
            mNewsTitle.setText(Html.fromHtml(newsModel.getHtmlTitle()));
            final String date = GetDateFromTimestamp.getDate(Long.parseLong(newsModel.getTimeInMillis()));
            mNewsDate.setText(date);
            mShortNewsDescription.setText(Html.fromHtml(newsModel.getHtmlDescription()));
            mFullNewsDescription.setText(Html.fromHtml(newsModel.getHtmlDescription()));

            if (newsModel.getNewsType() == ArticleContentType.FREE) {
                mCardView.setBackground(itemView.getResources().getDrawable(R.drawable.background_free_news));
            } else {
                mCardView.setBackground(itemView.getResources().getDrawable(R.drawable.background_premium_news));
            }

            if (newsModel.getImageUri() == null || newsModel.getImageUri().isEmpty()) {
                mNewsImage.setVisibility(View.GONE);
            } else {
                final RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(itemView)
                        .load(newsModel.getImageUri())
                        .placeholder(R.drawable.preview_image)
                        .apply(requestOptions)
                        .into(mNewsImage);
            }
            if (newsModelItem.isExpanded()) {
                mShortNewsDescription.setVisibility(View.GONE);
                mFullNewsDescription.setVisibility(View.VISIBLE);
                mSeeFullArticle.setText(R.string.see_less_text);
            } else {
                mShortNewsDescription.setVisibility(View.VISIBLE);
                mFullNewsDescription.setVisibility(View.GONE);
                mSeeFullArticle.setText(R.string.see_more_text);
            }
        }
    }
}
