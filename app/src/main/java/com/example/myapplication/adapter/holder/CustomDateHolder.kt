package com.example.myapplication.adapter.holder

import android.view.View
import android.widget.TextView
import com.stfalcon.chatkit.R
import com.stfalcon.chatkit.commons.ViewHolder
import com.stfalcon.chatkit.utils.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class CustomDateHolder(itemView: View?) : ViewHolder<Date>(itemView) {

    private var customDateFormat: SimpleDateFormat = SimpleDateFormat("MM月dd日 EEEE")
    protected var text: TextView = itemView!!.findViewById<View>(R.id.messageText) as TextView
    protected var dateFormat: String? = null
    protected var dateHeadersFormatter: DateFormatter.Formatter? = null


    override fun onBind(date: Date?) {
//        super.onBind(date)

        if (DateFormatter.isToday(date)) {
            text.text = "今天"
        } else if (DateFormatter.isYesterday(date)) {
            text.text = "昨天"
        } else {
            text.text = customDateFormat.format(date)
        }
    }

}