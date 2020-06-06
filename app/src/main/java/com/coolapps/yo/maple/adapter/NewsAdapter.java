package com.coolapps.yo.maple.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coolapps.yo.maple.ArticleContentType;
import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.activity.NewsModel;
import com.coolapps.yo.maple.interfaces.NewsItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * NewsAdapter for news list
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsModel> mNewsModelList = new ArrayList<>();
    private NewsItemClickListener mNewsItemClickListener;

    public void setNewsItemClickListener(@NonNull NewsItemClickListener newsItemClickListener) {
        mNewsItemClickListener = newsItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news, parent, false));
    }

    public void setData(@NonNull List<NewsModel> newsModelList) {
        mNewsModelList.clear();
        mNewsModelList.addAll(newsModelList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mNewsModelList.get(position));

        holder.itemView.setOnClickListener(v -> {
            holder.seeFullArticle();

            if (mNewsItemClickListener != null) {
                mNewsItemClickListener.onNewsItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewsModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private ImageView mNewsImage;
        private TextView mNewsTitle;
        private TextView mNewsDescription;
        private TextView mSeeFullArticle;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.newsCard);
            mNewsImage = itemView.findViewById(R.id.newsImage);
            mNewsTitle = itemView.findViewById(R.id.newsTitle);
            mNewsDescription = itemView.findViewById(R.id.newsDescription);
            mSeeFullArticle = itemView.findViewById(R.id.see_full_article_text_view);
        }

        void bind(@NonNull NewsModel newsModel) {
            mNewsTitle.setText(Html.fromHtml(newsModel.getTitle()));
            mNewsDescription.setText(Html.fromHtml(newsModel.getDescription()));

            if (newsModel.getNewsType() == ArticleContentType.FREE) {
                mCardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.white));
            } else {
                mCardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.premiumCardBackground));
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
        }

        void seeFullArticle() {
            mNewsDescription.setMaxLines(Integer.MAX_VALUE);
            mSeeFullArticle.setVisibility(View.INVISIBLE);
        }
    }
}
