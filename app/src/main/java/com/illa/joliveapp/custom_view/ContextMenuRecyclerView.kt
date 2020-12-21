package com.illa.joliveapp.custom_view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ContextMenu
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stfalcon.chatkit.messages.MessagesList

class ContextMenuRecyclerView : MessagesList {

    class ContextMenuInfo(
        val recyclerView: RecyclerView,
        val itemView: View,
        val position: Int,
        val id: Long
    ) : ContextMenu.ContextMenuInfo

    private var contextMenuInfo: ContextMenuInfo? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    override fun getContextMenuInfo() = contextMenuInfo

    override fun showContextMenuForChild(originalView: View): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView)
    }

    override fun showContextMenuForChild(originalView: View, x: Float, y: Float): Boolean {
        saveContextMenuInfo(originalView)
        return super.showContextMenuForChild(originalView, x, y)
    }

    private fun saveContextMenuInfo(originalView: View) {
        Log.e("peter", "originalView:  $originalView")
        val position: Int = getChildAdapterPosition(originalView)
        val longId = getChildItemId(originalView)
        contextMenuInfo = ContextMenuInfo(this, originalView, position, longId)
    }
}