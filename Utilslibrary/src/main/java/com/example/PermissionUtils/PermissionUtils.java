package com.example.PermissionUtils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: Weather_Forecast
 * 包名: com.example.PermissionUtils
 * 创建时间: 2020/11/21 11:36
 */
public class PermissionUtils {

    public static void setUpPermission(List<String> permissions, Activity activity, int requestCode){
        List<String> p = new ArrayList<>();
        for (String jurisdiction:permissions) {
            if (activity.checkCallingPermission(jurisdiction)!= PackageManager.PERMISSION_GRANTED){
                p.add(jurisdiction);

            }
        }
        String[] strings = p.toArray(new String[permissions.size()]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(activity,strings,requestCode);
        }
    }
}
