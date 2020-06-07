package com.coolapps.yo.maple.interfaces;

import android.view.View;

/**
 * Interface for recycler view item click
 */
public interface NewsItemSeeMoreClickListener {
    void onNewsItemClick(View view, int position, boolean isSeeMore);
}
