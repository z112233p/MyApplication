package com.illa.joliveapp.interface_class

import com.illa.joliveapp.datamodle.chat.MessageReceived.message_received_args.MessageReceivedArgs2

interface WebSocketModle {
    fun messageReceive(dataList: MessageReceivedArgs2)
}