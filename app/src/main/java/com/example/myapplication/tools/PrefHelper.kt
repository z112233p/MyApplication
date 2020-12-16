package com.example.myapplication.tools

import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import com.example.myapplication.MyApp.Companion.get

object PrefHelper {
    val TAG = PrefHelper::class.java.simpleName
    private const val API_HEADER = "sessionid"
    private const val USER_ID = "user_id"
    private const val CHAT_TOKEN = "chat_token"
    private const val CHAT_ID = "chat_id"
    private const val CHAT_LABLE = "chat_lable"
    private const val CHAT_Name = "chat_name"
    private const val CHAT_ROOM_ID = "chat_room_id"
    private const val USER_PHONE = "user_iduser_phone"


    private var _preferences: SharedPreferences? = null
    val instance: SharedPreferences
        get() {
            if (_preferences == null) {
                _preferences = PreferenceManager.getDefaultSharedPreferences(get())
            }
            return this._preferences!!
        }

    //SESSION
    fun setApiHeader(session: String) {
        savePref(API_HEADER, session)
    }

    val apiHeader: String?
        get() = instance!!.getString(API_HEADER, "")

    //User ID
    fun setUserID(session: String) {
        savePref(USER_ID, session)
    }

    val userID: String?
        get() = instance!!.getString(USER_ID, "")

    //User Phone
    fun setUserPhone(session: String) {
        savePref(USER_PHONE, session)
    }

    val userPhone: String?
        get() = instance!!.getString(USER_PHONE, "")

    //Chat Token
    fun setChatToken(value: String) {
        savePref(CHAT_TOKEN, value)
    }

    val chatToken: String
        get() = instance!!.getString(CHAT_TOKEN, "")!!

    //Chat ID
    fun setChatId(value: String) {
        savePref(CHAT_ID, value)
    }

    val chatId: String
        get() = instance!!.getString(CHAT_ID, "")!!

    //Chat Lable
    fun setChatLable(value: String) {
        savePref(CHAT_LABLE, value)
    }

    val chatLable: String
        get() = instance.getString(CHAT_LABLE, "")!!

    //Chat Name
    fun setChatName(value: String) {
        savePref(CHAT_Name, value)
    }

    val chatName: String
        get() = instance.getString(CHAT_Name, "")!!

    //Chat Room ID
    fun setChatRoomId(value: String) {
        savePref(CHAT_ROOM_ID, value)
    }

    val chatRoomId: String?
        get() = instance.getString(CHAT_ROOM_ID, "")

    //===============
    private fun savePref(key: String, value: String) {
        val editor = instance!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun savePref(key: String, value: Int) {
        val editor = instance!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun savePref(key: String?, value: Boolean) {
        val editor = instance!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun loadPref(key: String): String? {
        return instance!!.getString(key, "")
    }

    private val navBarOverride: String?
        private get() {
            var sNavBarOverride: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    val c = Class.forName("android.os.SystemProperties")
                    val m =
                        c.getDeclaredMethod("get", String::class.java)
                    m.isAccessible = true
                    sNavBarOverride = m.invoke(null, "qemu.hw.mainkeys") as String
                } catch (e: Throwable) {
                }
            }
            return sNavBarOverride
        }
}