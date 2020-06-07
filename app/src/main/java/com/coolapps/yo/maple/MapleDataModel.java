package com.coolapps.yo.maple;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a central DataModel class. This holds all the data app fetches.
 * Call {@link MapleDataModel#getInstance()} to get the static DataModel instance.
 */
public class MapleDataModel {

    private static volatile MapleDataModel sInstance = null;

    private static final String TAG = "MapleDataModel";

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    private List<NewsModel> mFreeNewsModels = new ArrayList<>();
    private List<NewsModel> mPaidNewsModels = new ArrayList<>();

    private QueryDocumentSnapshot mLastFetchedFreeNewsDoc;
    private QueryDocumentSnapshot mLastFetchedPaidNewsDoc;

    private Query mFreeNewsFetchQuery;
    private Query mPaidNewsFetchQuery;

    public interface OnFetchNewsDataListener {
        void onDataFetchComplete(boolean success, @NonNull List<NewsModel> newsModels);
    }

    /**
     * Should not be instantiated outside the class
     */
    private MapleDataModel() { }

    public static MapleDataModel getInstance() {
        if (sInstance == null) {
            synchronized (MapleDataModel.class) {
                if (sInstance == null) {
                    sInstance = new MapleDataModel();
                }
            }
        }
        return sInstance;
    }

    /**
     * This method fetches free news data and saves in a list.
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchFreeNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedFreeNewsDoc = null;
        mFreeNewsModels.clear();
        mFreeNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "0")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(15);
        fetchFreeNewsAndProcess(listener);
    }

    /**
     * This method fetch next batch of free news data and merges with the original list.
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchFreeNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mFreeNewsFetchQuery != null && mLastFetchedFreeNewsDoc != null) {
            mFreeNewsFetchQuery = mFreeNewsFetchQuery.startAfter(mLastFetchedFreeNewsDoc);
            fetchFreeNewsAndProcess(listener);
        }
    }

    /**
     * This method fetches paid news data and saves in a list.
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchPaidNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedPaidNewsDoc = null;
        mPaidNewsModels.clear();
        mPaidNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "1")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(15);
        fetchPaidNewsAndProcess(listener);
    }

    /**
     * This method fetch next batch of paid news data and merges with the original list.
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchPaidNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mPaidNewsFetchQuery != null && mLastFetchedPaidNewsDoc != null) {
            mPaidNewsFetchQuery = mPaidNewsFetchQuery.startAfter(mLastFetchedPaidNewsDoc);
            fetchPaidNewsAndProcess(listener);
        }
    }

    // TODO: Refactor to extract common code
    private void fetchFreeNewsAndProcess(@NonNull OnFetchNewsDataListener listener) {
        mFreeNewsFetchQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final List<NewsModel> list = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        final String id = (String) snapshot.get("id");
                        final String title = (String) snapshot.get("title");
                        final String description = (String) snapshot.get("description");
                        final String pendingForApproval = (String) snapshot.get("pendingForApproval");
                        final String imageUriString = (String) snapshot.get("imageUri");
                        final String articleArea = (String) snapshot.get("articleArea");
                        final String articleType = (String) snapshot.get("articleType");
                        final String debug = (String) snapshot.get("debug");
                        final String timeInMillis = (String) snapshot.get("timeInMillis");

                        if (id != null && title != null && articleType != null && debug != null
                                && timeInMillis != null && pendingForApproval != null) {
                            list.add(new NewsModel(id,
                                    ArticleContentType.from(Integer.parseInt(articleType)), title,
                                    description, imageUriString, debug, timeInMillis, pendingForApproval, articleArea));
                        }
                        mLastFetchedFreeNewsDoc = snapshot;
                    }
                    mFreeNewsModels.addAll(list);
                    listener.onDataFetchComplete(true, list);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    // TODO: Refactor to extract common code
    private void fetchPaidNewsAndProcess(@NonNull OnFetchNewsDataListener listener) {
        mPaidNewsFetchQuery.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    final List<NewsModel> list = new ArrayList<>();
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        final String id = (String) snapshot.get("id");
                        final String title = (String) snapshot.get("title");
                        final String description = (String) snapshot.get("description");
                        final String pendingForApproval = (String) snapshot.get("pendingForApproval");
                        final String imageUriString = (String) snapshot.get("imageUri");
                        final String articleArea = (String) snapshot.get("articleArea");
                        final String articleType = (String) snapshot.get("articleType");
                        final String debug = (String) snapshot.get("debug");
                        final String timeInMillis = (String) snapshot.get("timeInMillis");

                        if (id != null && title != null && articleType != null && debug != null
                                && timeInMillis != null && pendingForApproval != null) {
                            list.add(new NewsModel(id,
                                    ArticleContentType.from(Integer.parseInt(articleType)), title,
                                    description, imageUriString, debug, timeInMillis, pendingForApproval, articleArea));
                        }
                        mLastFetchedPaidNewsDoc = snapshot;
                    }
                    mPaidNewsModels.addAll(list);
                    listener.onDataFetchComplete(true, list);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    /**
     * This method gives list of all free NewsModels.
     * @return List of NewsModel
     */
    public List<NewsModel> getFreeNewsModels() {
        final List<NewsModel> freeModels = new ArrayList<>();
        for (NewsModel newsModel : mFreeNewsModels) {
            if (newsModel.getNewsType() == ArticleContentType.FREE) {
                freeModels.add(newsModel);
            }
        }

        return freeModels;
    }

    /**
     * This method gives list of all paid NewsModels.
     * @return List of NewsModel
     */
    public List<NewsModel> getPaidNewsModels() {
        final List<NewsModel> paidModels = new ArrayList<>();
        for (NewsModel newsModel : mPaidNewsModels) {
            if (newsModel.getNewsType() == ArticleContentType.PAID) {
                paidModels.add(newsModel);
            }
        }

        return paidModels;
    }
}
