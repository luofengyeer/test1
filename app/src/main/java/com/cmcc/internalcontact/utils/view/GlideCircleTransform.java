package com.cmcc.internalcontact.utils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.cmcc.internalcontact.utils.Utils;

import java.security.MessageDigest;

/**
 * 圆形处理
 * @author hjl
 * @date 2017/5/18
 */

public class GlideCircleTransform extends BitmapTransformation {

    private Paint mBorderPaint;
    private float mBorderWidth;

    public GlideCircleTransform() {
        super();
    }

    public GlideCircleTransform(@DimenRes int borderWidth, @ColorRes int borderColorRes) {
        this(Utils.getContext(),borderWidth,borderColorRes);
    }

    @SuppressLint("ResourceType")
    public GlideCircleTransform(Context context, @DimenRes int borderWidth, @ColorRes int borderColorRes) {
        super();

        mBorderWidth = context.getResources().getDimensionPixelSize(borderWidth);
        mBorderPaint = new Paint();
        mBorderPaint.setDither(true);
        mBorderPaint.setAntiAlias(true);

        if(borderColorRes > 0){
            mBorderPaint.setColor(context.getResources().getColor(borderColorRes));
        }
        else {
            mBorderPaint.setColor(context.getResources().getColor(android.R.color.transparent));
        }

        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }


    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = (int) (Math.min(source.getWidth(), source.getHeight()) - (mBorderWidth / 2));
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (mBorderPaint != null) {
            float borderRadius = r - mBorderWidth / 2;
            canvas.drawCircle(r, r, borderRadius, mBorderPaint);
        }
        return result;
    }
    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}