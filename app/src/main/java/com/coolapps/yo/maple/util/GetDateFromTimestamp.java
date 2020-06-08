package com.coolapps.yo.maple.util;

import android.text.format.DateUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * This method returns date from timestamp.
 */
public class GetDateFromTimestamp {

    public static String getDate(long milliSeconds) {
        final Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(milliSeconds);
        final CharSequence relTime = DateUtils.getRelativeTimeSpanString(
                calendar.getTimeInMillis(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);
        return relTime.toString();
    }
}
