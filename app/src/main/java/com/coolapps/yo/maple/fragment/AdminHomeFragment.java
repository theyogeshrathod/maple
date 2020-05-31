package com.coolapps.yo.maple.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.coolapps.yo.maple.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Home screen for Admins.
 */
public class AdminHomeFragment extends BaseFragment {

    private static final String TAG = "AdminHomeFragment";

    private static final String LOADING_TAG = "loading_tag";

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private RecyclerView mPendingForApprovalList;
    private PendingForApprovalAdapter mPendingForApprovalAdapter = new PendingForApprovalAdapter();

    private LoadingFragment mLoadingFragment = LoadingFragment.newInstance();

    public static AdminHomeFragment newInstance() {
        final Bundle args = new Bundle();
        AdminHomeFragment fragment = new AdminHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_home_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPendingForApprovalList = view.findViewById(R.id.pending_for_approval_list);
        mPendingForApprovalList.setAdapter(mPendingForApprovalAdapter);
        mPendingForApprovalList.setLayoutManager(new LinearLayoutManager(requireContext()));
        mPendingForApprovalList.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        getData();
    }

    private void getData() {
        showLoadingFragment();
        mFirestore.collection("Articles").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final List<ArticleData> list = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        final String title = (String) snapshot.get("title");
                        final String description = (String) snapshot.get("description");
                        final String pendingForApproval = (String) snapshot.get("pendingForApproval");
                        final String imageDownloadUriString = (String) snapshot.get("downloadUri");

                        if ("true".equalsIgnoreCase(pendingForApproval)) {
                            list.add(new ArticleData(title, description, imageDownloadUriString != null ? Uri.parse(imageDownloadUriString) : null));
                        }
                    }
                    mPendingForApprovalAdapter.setArticleList(list);
                    hideLoadingFragment();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    hideLoadingFragment();
                });
    }

    private void showLoadingFragment() {
        if (!mLoadingFragment.isAdded()) {
            mLoadingFragment.show(getChildFragmentManager(), LOADING_TAG);
        }
    }

    private void hideLoadingFragment() {
        if (mLoadingFragment.isAdded()) {
            mLoadingFragment.dismiss();
        }
    }

    private static class PendingForApprovalViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private TextView mDescription;
        private ImageView mImage;

        PendingForApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mDescription = itemView.findViewById(R.id.description);
            mImage = itemView.findViewById(R.id.image);
        }

        void bind(@NonNull ArticleData articleData) {
            mTitle.setText(articleData.getTitle());
            mDescription.setText(articleData.getDescription());
            Glide.with(itemView).load(articleData.getImageUri()).into(mImage);
        }
    }

    private static class PendingForApprovalAdapter extends RecyclerView.Adapter<PendingForApprovalViewHolder> {

        private List<ArticleData> mPendingArticles = new ArrayList<>();

        @NonNull
        @Override
        public PendingForApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PendingForApprovalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_for_review_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PendingForApprovalViewHolder holder, int position) {
            holder.bind(mPendingArticles.get(holder.getAdapterPosition()));
        }

        @Override
        public int getItemCount() {
            return mPendingArticles.size();
        }

        void setArticleList(@NonNull List<ArticleData> list) {
            mPendingArticles.clear();
            mPendingArticles.addAll(list);
            notifyDataSetChanged();
        }
    }

    private static class ArticleData {
        private String mTitle;
        private String mDescription;
        private Uri mImageUri;

        ArticleData(@Nullable String title, @Nullable String description, @Nullable Uri imageUri) {
            this.mTitle = title;
            this.mDescription = description;
            this.mImageUri = imageUri;
        }

        String getTitle() {
            return mTitle;
        }

        String getDescription() {
            return mDescription;
        }

        Uri getImageUri() {
            return mImageUri;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ArticleData)) return false;
            ArticleData that = (ArticleData) o;
            return Objects.equals(mTitle, that.mTitle) &&
                    Objects.equals(mDescription, that.mDescription) &&
                    Objects.equals(mImageUri, that.mImageUri);
        }

        @Override
        public int hashCode() {
            return Objects.hash(mTitle, mDescription, mImageUri);
        }
    }
}
