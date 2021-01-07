package com.illa.joliveapp.interface_class

interface ItemMoveSwipeListener {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onItemSwipe(position: Int)
    fun onFinish()
}