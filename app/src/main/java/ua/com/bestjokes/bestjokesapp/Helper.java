package ua.com.bestjokes.bestjokesapp;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by User on 25.08.2017.
 */

public class Helper {

    /**
     * Converting timestamp to human readable date
     * @param time Time in timestamp format
     */
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }
}
