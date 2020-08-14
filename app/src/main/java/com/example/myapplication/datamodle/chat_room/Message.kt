package com.example.myapplication.datamodle.chat_room

import com.stfalcon.chatkit.commons.models.IMessage
import java.util.*

class Message : IMessage {
    /*...*/
    private var id = "Peter"
    private var text = "TEST"
    var author = Author()
    private var createdAt = Calendar.getInstance().time

    //    Date currentTime = Calendar.getInstance().getTime();
    fun setId(id: String) {
        this.id = id
    }

    fun setText(text: String) {
        this.text = text
    }

    fun setCreatedAt(createdAt: Date) {
        this.createdAt = createdAt
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
}