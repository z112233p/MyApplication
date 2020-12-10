package com.example.myapplication.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.tools.DensityUtils;


public class ImageViewPlus extends androidx.appcompat.widget.AppCompatImageView {

    private Paint paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mRawBitmap;
    private BitmapShader mShader;
    private Matrix matrix = new Matrix();
    private float borderWidth;
    private int borderColor;

    private static final int DEFAULT_BORDER_COLOR = Color.TRANSPARENT;

    private static final int DEFAULT_BORDER_WIDTH = 0;

    public ImageViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageViewPlus);
        borderColor = ta.getColor(R.styleable.ImageViewPlus_borderColor, DEFAULT_BORDER_COLOR);
        borderWidth = ta.getDimensionPixelSize(R.styleable.ImageViewPlus_borderWidth, DensityUtils.dip2px(context, DEFAULT_BORDER_WIDTH));
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, paintBorder);

        Bitmap rawBitmap = getBitmap(getDrawable());
        if (rawBitmap != null) {
            int viewWidth = getWidth();
            int viewHeight = getHeight();
            int viewMinSize = Math.min(viewWidth, viewHeight);
            float dstWidth = viewMinSize;
            float dstHeight = viewMinSize;
            if (mShader == null || !rawBitmap.equals(mRawBitmap)) {
                mRawBitmap = rawBitmap;
                mShader = new BitmapShader(mRawBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
            if (mShader != null) {
                matrix.setScale((dstWidth - borderWidth * 2) / rawBitmap.getWidth(), (dstHeight - borderWidth * 2) / rawBitmap.getHeight());
                mShader.setLocalMatrix(matrix);
            }
            paintBitmap.setShader(mShader);
//            paintBorder.setStyle(Paint.Style.STROKE);
//            paintBorder.setStrokeWidth(borderWidth);
            paintBorder.setColor(borderColor);
            paintBorder.setMaskFilter(new BlurMaskFilter(800, BlurMaskFilter.Blur.NORMAL));

            int center = getWidth()/2;
            int center2 = getHeight()/2;
            int halfW = getWidth()/3;
            canvas.drawCircle(center, center2, halfW, paintBorder);

            float radius = viewMinSize / 2.0f;
//            canvas.drawCircle(radius, radius, radius - borderWidth / 2.0f, paintBorder);
//            canvas.translate(borderWidth, borderWidth);
//            canvas.drawCircle(radius - borderWidth, radius - borderWidth, radius - borderWidth, paintBitmap);
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取imageview的bitmap
     * @param drawable
     * @return
     */
    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            Rect rect = drawable.getBounds();
            int width = rect.right - rect.left;
            int height = rect.bottom - rect.top;
            int color = ((ColorDrawable) drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }
    }

}

