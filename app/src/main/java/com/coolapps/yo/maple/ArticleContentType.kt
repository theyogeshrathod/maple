package com.coolapps.yo.maple

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Enum for news types
 */
@Parcelize
enum class ArticleContentType(private val value: Int) : Parcelable {
    UNKNOWN(-1), FREE(0), PAID(1), KNOWLEDGE(2), PROJECTS(3);

    companion object {
        @JvmStatic
        fun from(typeValue: Int): ArticleContentType {
            for (type in values()) {
                if (type.value == typeValue) {
                    return type
                }
            }
            return UNKNOWN
        }
    }

    fun getValue(): Int {
        return value
    }
}