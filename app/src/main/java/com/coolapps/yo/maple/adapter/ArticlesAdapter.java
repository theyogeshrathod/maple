package com.coolapps.yo.maple.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolapps.yo.maple.R;

import java.util.ArrayList;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private List<String> mTagsList = new ArrayList<>();

    @NonNull
    @Override
    public ArticlesAdapter.ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.bind(mTagsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTagsList.size();
    }

    public void setTagsList(@NonNull List<String> list) {
        mTagsList.clear();
        mTagsList.addAll(list);
        notifyDataSetChanged();
    }

    final static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private TextView mArticleTextView;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            mArticleTextView = itemView.findViewById(R.id.article_text_view);
        }

        void bind(@NonNull String text) {
            mArticleTextView.setText(String.format("#%s", text));
        }
    }
}
