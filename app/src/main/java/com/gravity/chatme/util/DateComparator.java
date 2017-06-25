package com.gravity.chatme.util;


import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class DateComparator {

    public static String compareDate(long date) {
      /*  long currentTime = new Date().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        if (simpleDateFormat.format(userLastSeen).equals(simpleDateFormat.format(currentTime))) {
            return "Last Seen : Today at " + new SimpleDateFormat("hh:mm a").format(userLastSeen);
        }
        return "Last Seen : " + new SimpleDateFormat("MMM d, hh:mm a").format(userLastSeen);*/

        //date *= 1000;
        Calendar rightNow = Calendar.getInstance();
        int day = rightNow.get(Calendar.DAY_OF_YEAR);
        int year = rightNow.get(Calendar.YEAR);
        rightNow.setTimeInMillis(date);
        int lastSeenDay = rightNow.get(Calendar.DAY_OF_YEAR);
        int lastSeenYear = rightNow.get(Calendar.YEAR);


        Log.d("LastSeenTest", new SimpleDateFormat("MMM d, hh:mm a").format(date));

        if (Math.abs(System.currentTimeMillis() - date) >= 31536000000L) {
            return formatter(date, 4);
        } else {
            int dayDiff = lastSeenDay - day;
            if (dayDiff == 0 && year == lastSeenYear) {
                return formatter(date, 0);
            } else if (dayDiff == -1 && year == lastSeenYear) {
                return formatter(date, 1);
            } else if (dayDiff > -7 && dayDiff < -1) {
                return formatter(date, 2);
            } else {
                return formatter(date, 3);
            }
        }
    }

    private static String formatter(long userLastSeen, int option) {

        switch (option) {
            case 0:
                return "Last seen : today at " + new SimpleDateFormat("hh:mm a").format(userLastSeen);
            case 1:
                return "Last seen : yesterday at " + new SimpleDateFormat("hh:mm a").format(userLastSeen);
            case 2:
                return "Last seen within a week";
            case 3:
                return "Last seen within a month";
            case 4:
                return "Last seen a long time ago";
            default:
                return "Error In Options";

        }
    }
}
