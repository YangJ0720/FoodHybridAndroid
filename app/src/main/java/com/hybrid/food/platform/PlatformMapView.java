package com.hybrid.food.platform;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class PlatformMapView implements PlatformView {

    private MapView mMapView;

    // TAG
    private static final String TAG = "PlatformMapView";

    public PlatformMapView(Context context, BinaryMessenger messenger, int id) {
        MapView mapView = new MapView(context);
        BaiduMap baiduMap = mapView.getMap();
        // 开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);
        // 设置地图定位配置项
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true,
                null, 0, 0);
        baiduMap.setMyLocationConfiguration(configuration);
        this.mMapView = mapView;
    }

    @Override
    public MapView getView() {
        return mMapView;
    }

    @Override
    public void dispose() {
        Log.e(TAG, "dispose");
    }

    @Override
    public void onFlutterViewAttached(View flutterView) {
        Log.i(TAG, "onFlutterViewAttached: " + flutterView);
    }

    @Override
    public void onFlutterViewDetached() {
        Log.e(TAG, "onFlutterViewDetached: ");
    }
}
