package com.example.ImageUtils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author: Administrator
 * @date: 2020/11/11
 */
public class ImageUtils {
    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context).load(url).into(imageView);
    }
}
