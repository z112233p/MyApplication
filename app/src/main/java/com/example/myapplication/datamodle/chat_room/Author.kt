package com.example.myapplication.datamodle.chat_room

import com.example.myapplication.tools.PrefHelper
import com.stfalcon.chatkit.commons.models.IUser

class Author : IUser {
    /*...*/
    private var id: String = PrefHelper.chatLable
    private var name: String = PrefHelper.chatName
    private var avatar = "https://static.raccoontv.com/smtv//uploads/banner/19/6Ul4J8zah40BAUZn.jpeg"

    fun setId(id: String) {
        this.id = id
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setAvatar(avatar: String) {
        this.avatar = avatar
    }

    override fun getId(): String {
        return id
    }

    override fun getName(): String {
        return name
    }

    override fun getAvatar(): String {
        return avatar
    }
}