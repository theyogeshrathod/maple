package com.coolapps.yo.maple.activity

import android.os.Parcelable
import com.coolapps.yo.maple.ArticleContentType
import kotlinx.android.parcel.Parcelize

/**
 * NewsModel class
 */

@Parcelize
data class NewsModel (val id: String, val newsType: ArticleContentType, val title: String, val description: String, val imagePath: String) : Parcelable