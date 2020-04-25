package com.hybrid.food.map

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

/**
 * @author YangJ
 */
class LocationTools {

    private var mLocationClient: LocationClient

    constructor(context: Context) {
        // 定位初始化
        val client = LocationClient(context)
        // 通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        // 打开GPS
        option.isOpenGps = true
        // 设置坐标类型
        option.setCoorType("bd0911")
        option.setScanSpan(5000)
        // 设置locationClientOption
        client.locOption = option
        //
        this.mLocationClient = client
    }

    fun registerLocationListener(listener: BDAbstractLocationListener) {
        // 注册LocationListener监听器
        this.mLocationClient.registerLocationListener(listener)
    }

    fun unRegisterLocationListener(listener: BDAbstractLocationListener) {
        // 解绑LocationListener监听器
        this.mLocationClient.unRegisterLocationListener(listener)
    }

    fun start() {
        // 开启地图定位图层
        this.mLocationClient.start()
    }

    fun stop() {
        this.mLocationClient.stop()
    }
}