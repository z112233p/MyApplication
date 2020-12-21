package com.illa.joliveapp.custom_view

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.illa.joliveapp.R


class BlurFilterView@JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :ConstraintLayout(context, attrs, defStyleAttr) {


    val view = View.inflate(context, R.layout.item_blur_filter_view, this)


    override fun onDraw(canvas: Canvas?) {
        //关闭硬件加速
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);


        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        //红色的画笔
        //红色的画笔
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        //NORMAL: 内外都模糊绘制
        //NORMAL: 内外都模糊绘制
        paint.maskFilter = BlurMaskFilter(50F, BlurMaskFilter.Blur.NORMAL)
//        canvas!!.drawRect(0F, 0F, 400F, 400F, paint)
        canvas!!.drawCircle(100F, 100F, 500F, paint);

    }
}