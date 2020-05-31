package com.coolapps.yo.maple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        this.mNewsItemClickListener = newsItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_news, parent, false));
    }

    public void setData(@NonNull List<NewsModel> newsModelList) {
        this.mNewsModelList.clear();
        this.mNewsModelList.addAll(newsModelList);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mNewsModelList.get(position));

        holder.itemView.setOnClickListener(v -> {
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
        CardView mCardView;
        ImageView mNewsImage;
        TextView mNewsTitle;
        TextView mNewsDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.newsCard);
            mNewsImage = itemView.findViewById(R.id.newsImage);
            mNewsTitle = itemView.findViewById(R.id.newsTitle);
            mNewsDescription = itemView.findViewById(R.id.newsDescription);
        }

        void bind(@NonNull NewsModel newsModel) {
            mNewsTitle.setText(newsModel.getTitle());
            mNewsDescription.setText(newsModel.getDescription());

            if (newsModel.getNewsType() == ArticleContentType.FREE) {
                mCardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.white));
            } else {
                mCardView.setCardBackgroundColor(itemView.getResources().getColor(R.color.premiumCardBackground));
            }

            if (newsModel.getImagePath().isEmpty()) {
                mNewsImage.setVisibility(View.GONE);
            } else {
                Glide.with(itemView)
                        .load(newsModel.getImagePath())
                        .placeholder(R.drawable.preview_image)
                        .into(mNewsImage);
            }
        }
    }
}
