package com.example.myapplication.`interface`

import com.example.myapplication.datamodle.chat.MessageReceived.message_received_args.MessageReceivedArgs2

interface WebSocketModle {
    fun messageReceive(dataList: MessageReceivedArgs2)
}