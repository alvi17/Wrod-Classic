package alvi17.onesearch;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by User on 3/31/2017.
 */

public class Util {

    public static void saveInfo(Context context, String key, String value )
    {
        try
        {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).apply();
        }
        catch (Exception ex)
        {

        }
    }
    public static String getInfo(Context context, String key)
    {
        try {
            return PreferenceManager.getDefaultSharedPreferences(context).getString(key,"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
