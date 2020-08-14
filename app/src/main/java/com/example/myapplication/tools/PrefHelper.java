package com.example.myapplication.tools;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.myapplication.MyApp;

public class PrefHelper {
    final static String TAG = PrefHelper.class.getSimpleName();

    private static String API_HEADER = "sessionid";
    private static String CHAT_TOKEN = "chat_token";
    private static String CHAT_ID = "chat_id";
    private static String CHAT_LABLE = "chat_lable";
    private static String CHAT_ROOM_ID = "chat_room_id";


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

    public static String getApiHeader() {
        return getInstance().getString(API_HEADER, "");
    }

    //Chat Token
    public static void setChatToken(String value){savePref(CHAT_TOKEN,value);}

    public static String getChatToken(){
        return getInstance().getString(CHAT_TOKEN,"");
    }

    //Chat ID
    public static void setChatId(String value){
        savePref(CHAT_ID,value);
    }

    public static String getChatId(){
        return getInstance().getString(CHAT_ID, "");
    }

    //Chat Lable
    public static void setChatLable(String value){
        savePref(CHAT_LABLE,value);
    }

    public static String getChatLable(){
        return getInstance().getString(CHAT_LABLE,"");
    }

    //Chat Room ID
    public static void setChatRoomId(String value){
        savePref(CHAT_ROOM_ID,value);
    }

    public static String getChatRoomId(){
        return getInstance().getString(CHAT_ROOM_ID,"");
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
