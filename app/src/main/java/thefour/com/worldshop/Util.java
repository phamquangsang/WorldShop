package thefour.com.worldshop;

import android.text.format.DateUtils;

/**
 * Created by Quang Quang on 11/18/2016.
 */

public class Util {
    public static String relativeTimeFormat(long timeStamp){
//        final String pattern = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
//        //2016-05-03 07:58:41
//        final String pattern1 = "yyyy-MM-dd hh:mm:ss";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern1, Locale.ENGLISH);
//        simpleDateFormat.setLenient(true);
        String relativeTime = "";
        relativeTime = DateUtils.getRelativeTimeSpanString(timeStamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        return relativeTime;
    }

    public static String encodeEmail(String email){
        return email.replace(".",",");
    }
    public static String decodeEmail(String encodedEmail){
        return encodedEmail.replace(',','.');
    }

}
