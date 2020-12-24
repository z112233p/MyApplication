package com.illa.joliveapp.tools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.illa.joliveapp.activity.*
import com.labo.kaji.relativepopupwindow.RelativePopupWindow


object IntentHelper {


    fun gotoChatRoom(ctx: Context, rID: String, eventId: String) {
        val intent = Intent(ctx, ChatRoomActivity::class.java)
        val b = Bundle()
        b.putString("ChatRoomID", rID)
        b.putString("eventID", eventId)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoEventActivity(ctx: Context){
        val intent = Intent(ctx, EventsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION;

        ctx.startActivity(intent)
//        (ctx as Activity).overridePendingTransition(0, 0)
    }

    fun gotoMapsActivity(ctx: Context){
        val intent = Intent(ctx, MapsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoPersonalActivity(ctx: Context){
        val intent = Intent(ctx, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }


    fun gotoFullScreenImageActivity(ctx: Context, imgUrl: String, imageView: ImageView){
        val intent = Intent(ctx, FullScreenImageActivity::class.java)
        val b = Bundle()
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            ctx as ChatRoomActivity, imageView, ViewCompat.getTransitionName(imageView)!!
        )
        b.putString("imgUrl", imgUrl)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent, options.toBundle())
    }

    fun gotoCreateEventActivity(ctx: Context){
        val intent = Intent(ctx, CreateEventActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoCreateEventActivity(ctx: Context, label: String){
        val intent = Intent(ctx, CreateEventActivity::class.java)
        val b = Bundle()
        b.putString("Label", label)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }


    fun gotoProfileActivity(ctx: Context){
        val intent = Intent(ctx, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoEventDetailActivity(ctx: Context, label: String, gotoReview: Boolean){
        val intent = Intent(ctx, EventDetailActivity::class.java)
        val b = Bundle()
        b.putString("Label", label)
        b.putBoolean("gotoReview", gotoReview)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoEventReviewActivity(ctx: Context, label: String, eventId: String){
        val intent = Intent(ctx, EventReviewActivity::class.java)
        val b = Bundle()
        b.putString("Label", label)
        b.putString("eventId", eventId)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoMyInfoActivity(ctx: Context, label: String){
        val intent = Intent(ctx, MyInfoActivity::class.java)
        val b = Bundle()
        b.putString("Label", label)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoMyInfoActivity(ctx: Context, label: String, position: Int){
        val intent = Intent(ctx, MyInfoActivity::class.java)
        val b = Bundle()
        b.putString("Label", label)
        b.putInt("position", position)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }


    fun gotoEditMyInfoActivity(ctx: Context){
        val intent = Intent(ctx, EditMyInfoActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoSettingActivity(ctx: Context){
        val intent = Intent(ctx, SettingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoNoticeActivity(ctx: Context){
        val intent = Intent(ctx, NoticeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    fun gotoSeeMoreActivity(ctx: Context, sortType: String, eventsCategorysId: String, title: String){
        val intent = Intent(ctx, SeeMoreActivity::class.java)
        val b = Bundle()
        b.putString("title", title)
        b.putString("sortType", sortType)
        b.putString("eventsCategorysId", eventsCategorysId)
        intent.putExtras(b)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }
}