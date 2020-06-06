package com.coolapps.yo.maple;

import com.coolapps.yo.maple.activity.NewsModel;

import java.util.ArrayList;
import java.util.List;

import static com.coolapps.yo.maple.ArticleContentType.FREE;
import static com.coolapps.yo.maple.ArticleContentType.PAID;

/**
 * Mock News Data
 */
public class MockNewsData {

    public ArrayList<NewsModel> getFreeNewsData() {
        final ArrayList<NewsModel> newsModelList = new ArrayList<>();
        // Take real data from database, should be removed
        newsModelList.add(new NewsModel("1", FREE, "Title 1", "Description 1", "",
                "true", "123456", "false", ""));
        newsModelList.add(new NewsModel("2", FREE, "Title 2", "Description 2", "https://homepages.cae.wisc.edu/~ece533/images/cat.png",
                "true", "123457", "false", ""));
        newsModelList.add(new NewsModel("3", FREE, "Title 3", "Description 3", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
                "true", "123458", "false", ""));
        newsModelList.add(new NewsModel("4", FREE, "Title 4", "Description 4", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
                "true", "123459", "false", ""));

        return newsModelList;
    }

    public ArrayList<NewsModel> getPremiumNewsData() {
        final ArrayList<NewsModel> newsModelList = new ArrayList<>();
        // Take real data from database, should be removed
        newsModelList.add(new NewsModel("1", PAID, "Title 1", "Description 1", "",
                "true", "1234566", "false", ""));
        newsModelList.add(new NewsModel("2", PAID, "Title 2", "Description 2", "https://homepages.cae.wisc.edu/~ece533/images/cat.png",
                "true", "1234567", "false", ""));
        newsModelList.add(new NewsModel("3", PAID, "Title 3", "Description 3", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
                "true", "1234568", "false", ""));
        newsModelList.add(new NewsModel("4", PAID, "Title 4", "Description 4", "https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
                "true", "1234569", "false", ""));
        return newsModelList;
    }
}
