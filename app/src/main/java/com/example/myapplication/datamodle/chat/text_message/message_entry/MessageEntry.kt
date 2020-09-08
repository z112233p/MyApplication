package com.example.myapplication.datamodle.chat.text_message.message_entry

data class MessageEntry(
    var retype: String,
    var recontent: String,
    var relabel: String,
    var rename: String,
    var content: String
){
    fun clear(){
        retype = ""
        recontent = ""
        relabel = ""
        rename = ""
        content = ""
    }
}