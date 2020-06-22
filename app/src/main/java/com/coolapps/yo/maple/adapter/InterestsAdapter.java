package com.coolapps.yo.maple.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coolapps.yo.maple.R;
import com.coolapps.yo.maple.model.TagInterestsModel;

import java.util.ArrayList;
import java.util.List;

public class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.InterestViewHolder> {

    private static final String TAG = "InterestsAdapter";
    private List<TagInterestsModel> mTagsList = new ArrayList<>();
    private OnItemCheckListener mOnItemClick;
    private List<String> mTagIds;

    public InterestsAdapter(List<String> tagIdList) {
        mTagIds = tagIdList;
    }

    public void setOnItemClick(OnItemCheckListener onItemClick) {
        mOnItemClick = onItemClick;
    }

    @NonNull
    @Override
    public InterestsAdapter.InterestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InterestViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_choose_interest, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InterestViewHolder holder, int position) {
        holder.bind(mTagsList.get(position));

        holder.itemView.setOnClickListener(v -> {
            if (holder.mInterestCheckBox.isChecked()) {
                mOnItemClick.onItemCheck(mTagsList.get(position));
            } else {
                mOnItemClick.onItemUncheck(mTagsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTagsList.size();
    }

    public void setInterestsList(@NonNull List<TagInterestsModel> list) {
        mTagsList.clear();
        mTagsList.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnItemCheckListener {
        void onItemCheck(TagInterestsModel tag);

        void onItemUncheck(TagInterestsModel tag);
    }

    final class InterestViewHolder extends RecyclerView.ViewHolder {
        private CheckBox mInterestCheckBox;

        InterestViewHolder(@NonNull View itemView) {
            super(itemView);
            mInterestCheckBox = itemView.findViewById(R.id.checkboxInterest);
            mInterestCheckBox.setClickable(false);
        }

        void bind(TagInterestsModel tagInterests) {
            Log.e(TAG, "bind: tag name " + tagInterests.getTagName());
            mInterestCheckBox.setText(tagInterests.getTagName());

            if (mTagIds.contains(tagInterests.getId())) {
                mInterestCheckBox.setChecked(true);
            }
        }
    }
}
