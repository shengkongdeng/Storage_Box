package com.example.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.adapterlibrary.R;

/**
 * @author: Administrator
 * @date: 2020/11/11
 */
public class ImageUtils {
    public static void loadImage(Context context, String url, ImageView imageView,int rounded){
        if (rounded==0){
            Glide.with(context).load(url).into(imageView);
        }else {
            RequestOptions options = new RequestOptions().error(R.drawable.img_load_error).bitmapTransform(new RoundedCorners(rounded));
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

    public static void loadLocalImage(Context context, int drawable, ImageView imageView,int rounded){
        if (rounded==0){
            Glide.with(context).load(drawable).into(imageView);
        }else {
            RequestOptions options = new RequestOptions().error(R.drawable.img_load_error).bitmapTransform(new RoundedCorners(rounded));
            Glide.with(context).load(drawable).apply(options).into(imageView);
        }
    }
}
