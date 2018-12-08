package com.cmcc.internalcontact.utils.imagepicker;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cmcc.internalcontact.MyApplication;
import com.cmcc.internalcontact.R;
import com.imnjh.imagepicker.ImageLoader;

public class GlideImageLoader implements ImageLoader {
    @Override
    public void bindImage(ImageView imageView, Uri uri, int width, int height) {
        Glide.with(MyApplication.getAppContext())
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.ic_edit_info_headimg)
                .error(R.mipmap.ic_edit_info_headimg).override(width, height).dontAnimate())
                .load(uri)
              .into(imageView);
    }

    @Override
    public void bindImage(ImageView imageView, Uri uri) {
        Glide.with(MyApplication.getAppContext())
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.mipmap.ic_edit_info_headimg)
                        .error(R.mipmap.ic_edit_info_headimg).dontAnimate())
                        .load(uri).into(imageView);
    }

    @Override
    public ImageView createImageView(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    @Override
    public ImageView createFakeImageView(Context context) {
        return new ImageView(context);
    }
}