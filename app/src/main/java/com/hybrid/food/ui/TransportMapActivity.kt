package com.hybrid.food.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.mapapi.map.*
import com.baidu.mapapi.search.poi.PoiResult
import com.hybrid.food.R
import com.hybrid.food.map.LocationTools
import com.hybrid.food.map.PoiSearchTools
import kotlinx.android.synthetic.main.activity_transport_map.*

/**
 * 骑手送货位置信息界面
 * @author YangJ
 */
class TransportMapActivity : AppCompatActivity() {

    private lateinit var mLocationTools: LocationTools
    private lateinit var mListener: BDAbstractLocationListener

    // 创建POI检索实例
    private lateinit var mPoiSearchTools: PoiSearchTools
    private lateinit var mBaiduMap: BaiduMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_map)
        initData()
        initView()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        // 停止poi检索
        mPoiSearchTools.setOnGetPoiResultListener(null)
        mPoiSearchTools.destroy()
        // 停止定位
        mLocationTools.unRegisterLocationListener(mListener)
        mLocationTools.stop()
        mBaiduMap.isMyLocationEnabled = false
        mapView.onDestroy()
        super.onDestroy()
    }

    private fun initData() {
        // 定位初始化
        val tools = LocationTools(this)
        val listener = MyLocationListener()
        tools.registerLocationListener(listener)
        tools.start()
        this.mListener = listener
        this.mLocationTools = tools
        // 创建POI检索实例
        val poiSearchTools = PoiSearchTools()
        poiSearchTools.setOnGetPoiResultListener(object : PoiSearchTools.OnGetPoiResultListener {
            override fun onGetPoiResult(poiResult: PoiResult) {
                mBaiduMap.clear()
                // 根据检索结果设置覆盖物
                val allPoi = poiResult.allPoi
                // 绘制商家覆盖物
                mBaiduMap.addOverlay(
                    MarkerOptions().position(allPoi[0].location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ano))
                )
                // 绘制骑手覆盖物
                mBaiduMap.addOverlay(
                    MarkerOptions().position(allPoi[1].location)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_aba))
                )
            }
        })
        this.mPoiSearchTools = poiSearchTools
    }

    private fun initView() {
        val baiduMap = mapView.map
        // 开启地图的定位图层
        baiduMap.isMyLocationEnabled = true
        // 设置地图定位配置项
        val configuration = MyLocationConfiguration(
            MyLocationConfiguration.LocationMode.FOLLOWING, // 跟随模式
            true, // 是否开启方向
            null,
            0,
            0
        )
        baiduMap.setMyLocationConfiguration(configuration)
        this.mBaiduMap = baiduMap
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            // mapView 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return
            }
            val locData = MyLocationData.Builder()
                .accuracy(location.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(location.direction).latitude(location.latitude)
                .longitude(location.longitude).build()
            mBaiduMap.setMyLocationData(locData)
            // 执行poi检索
            mPoiSearchTools.searchNearby(location.latitude, location.longitude)
        }
    }
}
