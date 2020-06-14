package com.coolapps.yo.maple;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.coolapps.yo.maple.model.TagInterestsModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is a central DataModel class. This holds all the data app fetches.
 * Call {@link MapleDataModel#getInstance()} to get the static DataModel instance.
 */
public class MapleDataModel {

    private static final String TAG = "MapleDataModel";
    private static final String USER_PROFILE_COLLECTION = "UserProfiles";
    private static volatile MapleDataModel sInstance = null;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private List<NewsModel> mFreeNewsModels = new ArrayList<>();
    private List<NewsModel> mPaidNewsModels = new ArrayList<>();
    private List<NewsModel> mKnowledgeNewsModels = new ArrayList<>();
    private List<NewsModel> mProjectsNewsModels = new ArrayList<>();
    private QueryDocumentSnapshot mLastFetchedFreeNewsDoc;
    private QueryDocumentSnapshot mLastFetchedPaidNewsDoc;
    private QueryDocumentSnapshot mLastFetchedKnowledgeNewsDoc;
    private QueryDocumentSnapshot mLastFetchedProjectsNewsDoc;
    private Query mFreeNewsFetchQuery;
    private Query mPaidNewsFetchQuery;
    private Query mKnowledgeNewsFetchQuery;
    private Query mProjectsNewsFetchQuery;
    private Set<TagInterestsModel> mAvailableTags = new HashSet<>();

    /**
     * Should not be instantiated outside the class
     */
    private MapleDataModel() {
    }

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
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchFreeNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedFreeNewsDoc = null;
        mFreeNewsModels.clear();
        mFreeNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "0")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(20);
        fetchFreeNewsAndProcess(listener, null);
    }

    /**
     * This method fetch next batch of free news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchFreeNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mFreeNewsFetchQuery != null && mLastFetchedFreeNewsDoc != null) {
            mFreeNewsFetchQuery = mFreeNewsFetchQuery.startAfter(mLastFetchedFreeNewsDoc);
            fetchFreeNewsAndProcess(listener, null);
        }
    }

    /**
     * This method fetch next batch of free news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchFreeNewsData(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        if (mFreeNewsFetchQuery != null && mLastFetchedFreeNewsDoc != null) {
            mFreeNewsFetchQuery = mFreeNewsFetchQuery.startAfter(mLastFetchedFreeNewsDoc);
            fetchFreeNewsAndProcess(listener, articleTagId);
        }
    }

    /**
     * This method fetches paid news data and saves in a list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchPaidNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedPaidNewsDoc = null;
        mPaidNewsModels.clear();
        mPaidNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "1")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(20);
        fetchPaidNewsAndProcess(listener, null);
    }

    /**
     * This method fetch next batch of paid news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchPaidNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mPaidNewsFetchQuery != null && mLastFetchedPaidNewsDoc != null) {
            mPaidNewsFetchQuery = mPaidNewsFetchQuery.startAfter(mLastFetchedPaidNewsDoc);
            fetchPaidNewsAndProcess(listener, null);
        }
    }

    /**
     * This method fetch next batch of paid news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchPaidNewsData(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        if (mPaidNewsFetchQuery != null && mLastFetchedPaidNewsDoc != null) {
            mPaidNewsFetchQuery = mPaidNewsFetchQuery.startAfter(mLastFetchedPaidNewsDoc);
            fetchPaidNewsAndProcess(listener, articleTagId);
        }
    }

    /**
     * This method fetches Knowledge news data and saves in a list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchKnowledgeNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedKnowledgeNewsDoc = null;
        mKnowledgeNewsModels.clear();
        mKnowledgeNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "2")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(20);
        fetchKnowledgeNewsAndProcess(listener, null);
    }

    /**
     * This method fetch next batch of Knowledge news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchKnowledgeNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mKnowledgeNewsFetchQuery != null && mLastFetchedKnowledgeNewsDoc != null) {
            mKnowledgeNewsFetchQuery = mKnowledgeNewsFetchQuery.startAfter(mLastFetchedKnowledgeNewsDoc);
            fetchKnowledgeNewsAndProcess(listener, null);
        }
    }

    /**
     * This method fetch next batch of Knowledge news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchKnowledgeNewsData(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        if (mKnowledgeNewsFetchQuery != null && mLastFetchedKnowledgeNewsDoc != null) {
            mKnowledgeNewsFetchQuery = mKnowledgeNewsFetchQuery.startAfter(mLastFetchedKnowledgeNewsDoc);
            fetchKnowledgeNewsAndProcess(listener, articleTagId);
        }
    }

    /**
     * This method fetches Projects news data and saves in a list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchFirstBatchProjectsNewsData(@NonNull OnFetchNewsDataListener listener) {
        mLastFetchedProjectsNewsDoc = null;
        mProjectsNewsModels.clear();
        mProjectsNewsFetchQuery = mFirestore.collection("Articles")
                .whereEqualTo("articleType", "3")
                .orderBy("timeInMillis", Query.Direction.DESCENDING)
                .limit(20);
        fetchProjectsNewsAndProcess(listener, null);
    }

    /**
     * This method fetch next batch of Projects news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchProjectsNewsData(@NonNull OnFetchNewsDataListener listener) {
        if (mProjectsNewsFetchQuery != null && mLastFetchedProjectsNewsDoc != null) {
            mProjectsNewsFetchQuery = mProjectsNewsFetchQuery.startAfter(mLastFetchedProjectsNewsDoc);
            fetchProjectsNewsAndProcess(listener, null);
        }
    }

    /**
     * This method fetch next batch of Projects news data and merges with the original list.
     *
     * @param listener callback on data fetch complete.
     */
    public void fetchNextBatchProjectsNewsData(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        if (mProjectsNewsFetchQuery != null && mLastFetchedProjectsNewsDoc != null) {
            mProjectsNewsFetchQuery = mProjectsNewsFetchQuery.startAfter(mLastFetchedProjectsNewsDoc);
            fetchProjectsNewsAndProcess(listener, articleTagId);
        }
    }

    // TODO: Refactor to extract common code
    private void fetchFreeNewsAndProcess(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
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
                    if (articleTagId == null) {
                        listener.onDataFetchComplete(true, list);
                    } else {
                        final List<NewsModel> returnList = new ArrayList<>(list.size());
                        for (NewsModel newsModel : list) {
                            if (articleTagId.equalsIgnoreCase(newsModel.getInterestAreasTagId())) {
                                returnList.add(newsModel);
                            }
                        }
                        listener.onDataFetchComplete(true, returnList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    // TODO: Refactor to extract common code
    private void fetchPaidNewsAndProcess(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
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
                    if (articleTagId == null) {
                        listener.onDataFetchComplete(true, list);
                    } else {
                        final List<NewsModel> returnList = new ArrayList<>(list.size());
                        for (NewsModel newsModel : list) {
                            if (articleTagId.equalsIgnoreCase(newsModel.getInterestAreasTagId())) {
                                returnList.add(newsModel);
                            }
                        }
                        listener.onDataFetchComplete(true, returnList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    // TODO: Refactor to extract common code
    private void fetchKnowledgeNewsAndProcess(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        mKnowledgeNewsFetchQuery.get()
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
                        mLastFetchedKnowledgeNewsDoc = snapshot;
                    }
                    mKnowledgeNewsModels.addAll(list);
                    if (articleTagId == null) {
                        listener.onDataFetchComplete(true, list);
                    } else {
                        final List<NewsModel> returnList = new ArrayList<>(list.size());
                        for (NewsModel newsModel : list) {
                            if (articleTagId.equalsIgnoreCase(newsModel.getInterestAreasTagId())) {
                                returnList.add(newsModel);
                            }
                        }
                        listener.onDataFetchComplete(true, returnList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    // TODO: Refactor to extract common code
    private void fetchProjectsNewsAndProcess(@NonNull OnFetchNewsDataListener listener, @Nullable String articleTagId) {
        mProjectsNewsFetchQuery.get()
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
                        mLastFetchedProjectsNewsDoc = snapshot;
                    }
                    mProjectsNewsModels.addAll(list);
                    if (articleTagId == null) {
                        listener.onDataFetchComplete(true, list);
                    } else {
                        final List<NewsModel> returnList = new ArrayList<>(list.size());
                        for (NewsModel newsModel : list) {
                            if (articleTagId.equalsIgnoreCase(newsModel.getInterestAreasTagId())) {
                                returnList.add(newsModel);
                            }
                        }
                        listener.onDataFetchComplete(true, returnList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onDataFetchComplete(false, new ArrayList<>());
                });
    }

    public void fetchAvailableTags(@NonNull OnArticleTagsFetchListener listener) {
        mAvailableTags.clear();
        mFirestore.collection("ArticleTags").orderBy("name").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            final TagInterestsModel tagInterests = new TagInterestsModel((String) snapshot.get("id"), (String) snapshot.get("name"));
                            mAvailableTags.add(tagInterests);
                        }
                    }
                    listener.onTagsFetched(true, mAvailableTags);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get data", e);
                    listener.onTagsFetched(false, mAvailableTags);
                });
    }

    /**
     * This method gives user profile Data
     */
    public void fetchProfileData(String documentKey) {
        Log.d(TAG, "fetchProfileData: docKey " + documentKey);
        mFirestore.collection(USER_PROFILE_COLLECTION).document(documentKey)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> profileData = documentSnapshot.getData();

                        Log.d(TAG, "onSuccess: profileData " + profileData);

                        if (profileData != null) {
                            Log.d(TAG, "onSuccess: name " + profileData.get("name"));
                        }

                    }
                });
    }

    @NonNull
    public Set<TagInterestsModel> getAvailableTags() {
        return mAvailableTags;
    }

    /**
     * This method gives list of all free NewsModels.
     *
     * @return List of NewsModel
     */
    @NonNull
    public List<NewsModel> getFreeNewsModels(@Nullable String articleType) {
        final List<NewsModel> freeModels = new ArrayList<>();
        if (articleType == null) {
            freeModels.addAll(mFreeNewsModels);
            return freeModels;
        }

        for (NewsModel newsModel : mFreeNewsModels) {
            if (newsModel.getInterestAreasTagId() != null && newsModel.getInterestAreasTagId().equalsIgnoreCase(articleType)) {
                freeModels.add(newsModel);
            }
        }

        return freeModels;
    }

    /**
     * This method gives list of all paid NewsModels.
     *
     * @return List of NewsModel
     */
    @NonNull
    public List<NewsModel> getPaidNewsModels(@Nullable String articleType) {
        final List<NewsModel> paidModels = new ArrayList<>();
        if (articleType == null) {
            paidModels.addAll(mPaidNewsModels);
            return paidModels;
        }

        for (NewsModel newsModel : mPaidNewsModels) {
            if (newsModel.getInterestAreasTagId() != null && newsModel.getInterestAreasTagId().equalsIgnoreCase(articleType)) {
                paidModels.add(newsModel);
            }
        }

        return paidModels;
    }

    /**
     * This method gives list of all Knowledge NewsModels.
     *
     * @return List of NewsModel
     */
    @NonNull
    public List<NewsModel> getKnowledgeNewsModels(@Nullable String articleType) {
        final List<NewsModel> knowledgeModels = new ArrayList<>();
        if (articleType == null) {
            knowledgeModels.addAll(mKnowledgeNewsModels);
            return knowledgeModels;
        }

        for (NewsModel newsModel : mKnowledgeNewsModels) {
            if (newsModel.getInterestAreasTagId() != null && newsModel.getInterestAreasTagId().equalsIgnoreCase(articleType)) {
                knowledgeModels.add(newsModel);
            }
        }

        return knowledgeModels;
    }

    /**
     * This method gives list of all Projects NewsModels.
     *
     * @return List of NewsModel
     */
    @NonNull
    public List<NewsModel> getProjectsNewsModels(@Nullable String articleType) {
        final List<NewsModel> projectsModels = new ArrayList<>();
        if (articleType == null) {
            projectsModels.addAll(mProjectsNewsModels);
            return projectsModels;
        }

        for (NewsModel newsModel : mProjectsNewsModels) {
            if (newsModel.getInterestAreasTagId() != null && newsModel.getInterestAreasTagId().equalsIgnoreCase(articleType)) {
                projectsModels.add(newsModel);
            }
        }

        return projectsModels;
    }

    public interface OnFetchNewsDataListener {
        void onDataFetchComplete(boolean success, @NonNull List<NewsModel> newsModels);
    }

    public interface OnArticleTagsFetchListener {
        void onTagsFetched(boolean success, @NonNull Set<TagInterestsModel> tags);
    }
}
