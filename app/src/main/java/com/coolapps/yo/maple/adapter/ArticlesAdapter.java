package com.coolapps.yo.maple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.model.TagInterestsModel;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    public interface OnTagsSelectedListener {
        void onTagSelected(@NonNull String tag);
    }

    private List<TagInterestsModel> mTagsList = new ArrayList<>();
    private OnTagsSelectedListener mOnTagsSelectedListener;
    private Context mContext;
    private int mSelectedIndex = 0;

    public ArticlesAdapter(@NonNull Context context, @NonNull OnTagsSelectedListener listener) {
        mContext = context;
        mOnTagsSelectedListener = listener;
    }

    @NonNull
    @Override
    public ArticlesAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.bind(mTagsList.get(position),
                mSelectedIndex == position ? mContext.getResources().getColor(R.color.selected_article_tag_background_color)
                        : mContext.getResources().getColor(R.color.white));
        holder.itemView.setOnClickListener(v -> {
            mOnTagsSelectedListener.onTagSelected(mTagsList.get(holder.getAdapterPosition()).getId());
            mSelectedIndex = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mTagsList.size();
    }

    public void setTagsList(@NonNull List<TagInterestsModel> list) {
        mTagsList.clear();
        mTagsList.add(new TagInterestsModel("0", "all"));
        mTagsList.addAll(list);
        notifyDataSetChanged();
    }

    final static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mArticleTextView;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            mArticleTextView = itemView.findViewById(R.id.article_text_view);
        }

        void bind(@NonNull TagInterestsModel tagInterests, @ColorInt int backgroundColor) {
            mArticleTextView.setText(String.format("#%s", tagInterests.getTagName()));
            itemView.setBackgroundColor(backgroundColor);
        }
    }
}
