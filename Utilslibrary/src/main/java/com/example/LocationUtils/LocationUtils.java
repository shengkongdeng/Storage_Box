package com.example.LocationUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * 创建者: 走廊里の声控灯
 * 项目名: Weather_Forecast
 * 包名: com.example
 * 创建时间: 2020/11/21 11:21
 */
public class LocationUtils {
    private Activity activity;

    private LocationUtils(Builder builder) {
        this.activity = builder.activity;
    }


    public void getLocationLL(){
        getLastKnownLocation();
        Log.d("*************", "获取定位权限1 - 开始");
        Location location = getLastKnownLocation();
        if (location != null) {
            //传递经纬度给网页
            String result = "{code: '0',type:'2',data: {longitude: '" + location.getLongitude() + "',latitude: '" + location.getLatitude() + "'}}";

            //日志
            String locationStr = "维度：" + location.getLatitude() + "\n"
                    + "经度：" + location.getLongitude();
            Log.d("*************", "经纬度：" + locationStr);
        } else {
            Toast.makeText(activity, "位置信息获取失败", Toast.LENGTH_SHORT).show();
            Log.d("*************", "获取定位权限7 - " + "位置获取失败");
        }
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation(){
        LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers){
            Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
            if (lastKnownLocation == null){
                continue;
            }
            if (bestLocation == null || lastKnownLocation.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = lastKnownLocation;
            }
        }
        return bestLocation;
    }



    public static class Builder{
        private Activity activity;

        public Builder setContext(Activity activity) {
            this.activity = activity;
            return this;
        }

        public LocationUtils build(){
            return new LocationUtils(this);
        }
    }
}
