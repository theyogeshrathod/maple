package com.coolapps.yo.maple.util;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * This method returns date from timestamp.
 */
public class GetDateFromTimestamp {

    public static String getDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault());

        calendar.setTimeInMillis(milliSeconds);

        CharSequence relTime = DateUtils.getRelativeTimeSpanString(
                calendar.getTimeInMillis(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS);

//        return relTime.toString(); // shows mins/ days ago  and on past dates shows date only not time
        return sdf.format(calendar.getTime()); // shows date with time
    }
}
