package thefour.com.worldshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;

import com.google.gson.Gson;

import thefour.com.worldshop.models.City;

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

    public static SharedPreferences getSharedPreferences(Context c){
        SharedPreferences setting = c.getSharedPreferences("setting",Context.MODE_PRIVATE);
        return setting;
    }

    public static City loadSelectedCity(Context c){
        String cityJson = getSharedPreferences(c).getString(c.getString(R.string.setting_traveling_city),null);
        City city = new Gson().fromJson(cityJson, City.class);
        return city;
    }

    public static void saveTravelingCity(Context c,@Nullable City city){
        getSharedPreferences(c).edit()
                .putString(c.getString(R.string.setting_traveling_city),new Gson().toJson(city))
                .apply();
    }

}
