package com.hybrid.food.ui

import android.content.Context
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.hybrid.food.LocationOption
import com.hybrid.food.R
import com.hybrid.food.channel.FlutterMethodChannel
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author YangJ
 */
class MainActivity : BoostFlutterActivity() {

    // Channel
    private lateinit var mMethodChannel: MethodChannel
    // 百度定位
    private lateinit var mLocationClient: LocationClient
    private lateinit var mListener: BDAbstractLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止定位
        val client = mLocationClient
        client.unRegisterLocationListener(mListener)
        if (client.isStarted) {
            client.stop()
        }
    }

    private fun initData() {
        // 初始化Flutter引擎Engine
        val engine = FlutterBoost.instance().engineProvider()
        // 初始化Channel
        val methodChannel = FlutterMethodChannel.create(engine.dartExecutor)
        methodChannel.setMethodCallHandler { call, result ->
            when (val method = call.method) {
                "getLocation" -> {
                    result.success(200)
                    getLocation()
                }
                else -> {
                    result.notImplemented()
                    Log.e("MainActivity", "没有找到方法: $method")
                }
            }
        }
        this.mMethodChannel = methodChannel
        // 初始化百度定位
        val client = LocationClient(this)
        client.locOption = LocationOption.getLocationOption()
        this.mListener = MyLocationListener()
        client.registerLocationListener(mListener)
        this.mLocationClient = client
        // 判断gps是否开启
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!service.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS未开启", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {

    }

    private fun getLocation() {
        mLocationClient.start()
    }

    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            val lon = location.longitude
            val lat = location.latitude
            println("lat = $lat, lon = $lon")
            val locType = location.locType
            val district = location.district
            val street = location.street
            val town = location.town
            val streetNumber = location.streetNumber
            val address = "${location.city}$district$town$street$streetNumber"
            println("address = $address")
            mMethodChannel.invokeMethod("setLocation", address)
        }
    }
}
