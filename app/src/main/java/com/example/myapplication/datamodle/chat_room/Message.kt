package com.example.myapplication.datamodle.chat_room

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.MessageContentType
import java.util.*

class Message : IMessage ,MessageContentType.Image, MessageContentType{
    /*...*/
    private var id = "Peter"
    private var text = ""
    var author = Author()
    private var createdAt = Calendar.getInstance().time
    private var imageUrl: String? = null
    private var success: String? = null




    fun setId(id: String) {
        this.id = id
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setCreatedAt(createdAt: Date) {
        this.createdAt = createdAt
    }

    fun setImageUrl(url: String){
        this.imageUrl = url
    }

    fun setSuccess(status: String){
        success = status
    }


    override fun getImageUrl(): String? {
//        return "https://static.raccoontv.com/smtv//uploads/banner/19/6Ul4J8zah40BAUZn.jpeg"
//        return "content://media/external/images/media/37"
        return imageUrl
    }

    override fun getId(): String {
        return id
    }

    override fun getText(): String {
        return text
    }

    override fun getUser(): Author {
        return author
    }

    override fun getCreatedAt(): Date {
        return createdAt
    }

    fun getSuccess(): String? {
        return success
    }
}