package com.coolapps.yo.maple;

import android.util.Log;

import androidx.annotation.NonNull;

import com.coolapps.yo.maple.activity.NewsModel;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private List<NewsModel> mNewsModels = new ArrayList<>();

    public interface OnFetchNewsDataListener {
        void onDataFetchComplete(boolean success);
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
     * This method fetches all news data and saves in a List.
     * @param listener callback on data fetch complete.
     */
    public void fetchAllNewsData(@NonNull OnFetchNewsDataListener listener) {
        mFirestore.collection("Articles").get()
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
                    }
                    mNewsModels.clear();
                    mNewsModels.addAll(list);
                    listener.onDataFetchComplete(true);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false);
                });
    }

    /**
     * This method gives list of all free NewsModels.
     * @return List of NewsModel
     */
    public List<NewsModel> getFreeNewsModels() {
        final List<NewsModel> freeModels = new ArrayList<>();
        for (NewsModel newsModel : mNewsModels) {
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
        for (NewsModel newsModel : mNewsModels) {
            if (newsModel.getNewsType() == ArticleContentType.PAID) {
                paidModels.add(newsModel);
            }
        }

        return paidModels;
    }
}
