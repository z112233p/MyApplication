package com.illa.joliveapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.R
import com.illa.joliveapp.datamodle.event.index.EventIndexData
import com.illa.joliveapp.tools.ImgHelper
import com.illa.joliveapp.tools.PrefHelper
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat


@Suppress("UNREACHABLE_CODE", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Adapter_My_Events() :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_GOING = 1
    val TYPE_HISTORY = 2

    private lateinit var dataList: MutableList<EventIndexData>
    private lateinit var mContext: Context
    private var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private lateinit var customDateFormat: SimpleDateFormat
    private var holderType: Int = TYPE_GOING
    private var mOnItemClickListener: OnItemClickListener? = null


    constructor(context: Context?, holderType: Int) : this(){
        dataList = ArrayList()
        this.holderType = holderType
        if (context != null) {
            mContext = context
        }
        val symbols = DateFormatSymbols()
        val modifiedAmPm = arrayOf("am", "pm")
        symbols.setAmPmStrings(modifiedAmPm)
        symbols.shortWeekdays =  arrayOf("", "日", "一", "二", "三", "四", "五", "六")

        customDateFormat = SimpleDateFormat("MM月dd日 hh:mma(E)", symbols)
        Log.e("Peter","amPmStrings   ${customDateFormat.dateFormatSymbols.shortWeekdays[0]}")

    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, label: String)
    }

    fun setOnItemClickListener(listener: Adapter_My_Events.OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setData(dealData: List<EventIndexData>?) {
        if (dealData == null || dealData.isEmpty()) {
            return
        }
        dataList.clear()
        dataList.addAll(dealData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return holderType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cell :View = when(viewType){
            TYPE_GOING -> LayoutInflater.from(mContext).inflate(R.layout.item_my_event_v2, parent, false)
            TYPE_HISTORY -> LayoutInflater.from(mContext).inflate(R.layout.item_event_history_v2, parent, false)
            else -> LayoutInflater.from(mContext).inflate(R.layout.item_hot_event, parent, false)
        }
        val viewHolder = ViewHolder(cell)
        viewHolder.ivEvent = cell.findViewById(R.id.iv_event)
        viewHolder.ivEventBg = cell.findViewById(R.id.iv_event_bg)
        viewHolder.tvEventTitle = cell.findViewById(R.id.tv_event_title)
        viewHolder.tvEventLocation = cell.findViewById(R.id.tv_event_location)
        viewHolder.tvEventTime = cell.findViewById(R.id.tv_event_time)
        viewHolder.tvEventDescription = cell.findViewById(R.id.tv_event_description)
        viewHolder.tvOwner = cell.findViewById(R.id.tv_owner)
        viewHolder.ivOwner = cell.findViewById(R.id.iv_owner)

        viewHolder.ivEventMemberOne = cell.findViewById(R.id.iv_event_member_one)
        viewHolder.ivEventMemberTwo = cell.findViewById(R.id.iv_event_member_two)
        viewHolder.ivEventMemberThree = cell.findViewById(R.id.iv_event_member_three)
        viewHolder.tvEventMemberCount = cell.findViewById(R.id.tv_event_member_count)

        viewHolder.cvEventMemberOne = cell.findViewById(R.id.cv_event_member_one)
        viewHolder.cvEventMemberTwo = cell.findViewById(R.id.cv_event_member_two)
        viewHolder.cvEventMemberThree = cell.findViewById(R.id.cv_event_member_three)
        viewHolder.ivEventStatus = cell.findViewById(R.id.iv_event_status)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return dataList.size

    }

    @SuppressLint("WrongConstant")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(dataList.size == 0){return}
        val data = dataList[position]
        setBackground((holder as ViewHolder).ivEventBg, null)


        holder.ivEventStatus.visibility = View.INVISIBLE

        if(data.is_full_join == 1){
            holder.ivEventStatus.setImageDrawable(mContext.getDrawable(R.mipmap.ic_event_full))
            holder.ivEventStatus.visibility = View.VISIBLE

        } else {
            if(data.is_need_approved == 0){
                holder.ivEventStatus.setImageDrawable(mContext.getDrawable(R.mipmap.ic_event_come))
                holder.ivEventStatus.visibility = View.VISIBLE
            }
        }


        if(getItemViewType(position) == TYPE_GOING){
            ImgHelper.loadNormalImg(mContext, BuildConfig.IMAGE_URL+data.image, holder.ivEvent)
        } else {
            BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg"
            ImgHelper.loadNormalImgNoCache(mContext,BuildConfig.CHATROOM_IMAGE_URL+"dating/"+PrefHelper.chatLable +".jpg", holder.ivOwner)
            holder.tvOwner.text = PrefHelper.chatName
        }

        holder.tvEventTitle.text = data.title
        holder.tvEventDescription.text = data.description
        holder.tvEventLocation.text = data.location_title
        Log.e("Peter","amPmStrings2   ${customDateFormat.dateFormatSymbols.shortWeekdays[0]}")
        Log.e("Peter","amPmStrings2   ${customDateFormat.dateFormatSymbols.shortWeekdays.get(7)}")

        customDateFormat.dateFormatSymbols.amPmStrings = arrayOf("am", "pm")

        val startTime = customDateFormat.format(sdf.parse(data.start_time))
        holder.tvEventTime.text = startTime


        holder.itemView.setOnClickListener {
            mOnItemClickListener?.onItemClick(holder.itemView, position, data.label)
        }

        holder.itemView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        if(getItemViewType(position) == TYPE_GOING) {

            if (data.joins.isEmpty()) {
                holder.cvEventMemberOne.visibility = View.INVISIBLE
                holder.cvEventMemberTwo.visibility = View.INVISIBLE
                holder.cvEventMemberThree.visibility = View.INVISIBLE
                holder.tvEventMemberCount.visibility = View.INVISIBLE

            } else {
                for (i in data.joins.indices) {
                    when (i) {
                        0 -> {
                            val url =
                                BuildConfig.CHATROOM_IMAGE_URL + "dating/" + data.joins[i].label + ".jpg"
                            ImgHelper.loadNormalImgNoCache(mContext, url, holder.ivEventMemberOne)
                        }
                        1 -> {
                            val url =
                                BuildConfig.CHATROOM_IMAGE_URL + "dating/" + data.joins[i].label + ".jpg"
                            ImgHelper.loadNormalImgNoCache(mContext, url, holder.ivEventMemberTwo)
                        }
                        2 -> {
                            val url =
                                BuildConfig.CHATROOM_IMAGE_URL + "dating/" + data.joins[i].label + ".jpg"
                            ImgHelper.loadNormalImgNoCache(mContext, url, holder.ivEventMemberThree)
                        }
                    }
                }

                when(data.joins.size){
                    1 -> {
                        holder.cvEventMemberTwo.visibility = View.GONE
                        holder.cvEventMemberThree.visibility = View.GONE
                    }
                    2 ->{
                        holder.cvEventMemberThree.visibility = View.GONE
                    }
                }

                if (data.joins.size > 3) {
                    val diffCount = data.joins.size - 3
                    holder.tvEventMemberCount.text = "+$diffCount Going"
                } else {
                    holder.tvEventMemberCount.visibility = View.GONE
                }

            }
        }
        else {
            holder.cvEventMemberOne.visibility = View.GONE
            holder.cvEventMemberTwo.visibility = View.GONE
            holder.cvEventMemberThree.visibility = View.GONE
            holder.tvEventMemberCount.visibility = View.GONE
        }
        holder.ivEventStatus.bringToFront()

    }

    private fun setBackground(ivEvent: ImageView, gd: GradientDrawable?) {
        ivEvent.background = gd
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        lateinit var ivEvent: ImageView
        lateinit var ivEventBg: ImageView
        lateinit var tvEventTitle: TextView
        lateinit var tvEventLocation: TextView
        lateinit var tvEventTime: TextView
        lateinit var tvEventDescription: TextView

        lateinit var tvOwner: TextView
        lateinit var ivOwner: ImageView

        lateinit var ivEventMemberOne: ImageView
        lateinit var ivEventMemberTwo: ImageView
        lateinit var ivEventMemberThree: ImageView
        lateinit var tvEventMemberCount: TextView

        lateinit var cvEventMemberOne: CardView
        lateinit var cvEventMemberTwo: CardView
        lateinit var cvEventMemberThree: CardView
        lateinit var ivEventStatus: ImageView


    }

}