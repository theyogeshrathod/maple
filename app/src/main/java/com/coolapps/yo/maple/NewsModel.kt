package com.coolapps.yo.maple

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * NewsModel class
 */

@Parcelize
data class NewsModel (
    val id: String,
    val newsType: ArticleContentType,
    val htmlTitle: String,
    val htmlDescription: String?,
    val imageUri: String?,
    val isDebug: String,
    val timeInMillis: String,
    val isPendingForApproval: String,
    val interestAreas: String?
) : Parcelable