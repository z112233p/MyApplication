package com.example.myapplication.Tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myapplication.MyApp;

public class PrefHelper {
    final static String TAG = PrefHelper.class.getSimpleName();

    private static String API_HEADER = "sessionid";


    static private SharedPreferences _preferences;

    static public SharedPreferences getInstance() {
        if(_preferences==null) {
            _preferences = PreferenceManager.getDefaultSharedPreferences(MyApp.get());
        }
        return _preferences;
    }

    //SESSION
    public static void setApiHeader(String session) {
        savePref(API_HEADER, session);
    }

    static public String getApiHeader() {
        return getInstance().getString(API_HEADER, "");
    }


    //===============
    static private void savePref(String key, String value) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(key, value);
        editor.apply();
    }

    static private void savePref(String key, int value) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void savePref(String key, boolean value) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    static private String loadPref(String key) {
        return getInstance().getString(key, "");
    }


}
