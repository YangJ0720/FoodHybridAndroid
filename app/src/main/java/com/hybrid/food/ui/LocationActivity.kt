package com.hybrid.food.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.hybrid.food.R
import com.hybrid.food.map.LocationTools
import com.hybrid.food.map.location.SimpleBDAbstractLocationListener
import com.hybrid.food.utils.PermissionUtils
import com.hybrid.food.utils.checkSelfPermission
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

private const val REQUEST_CODE_LOCATION = 1

/**
 * @author YangJ 选择收货地址界面
 */
class LocationActivity : BoostFlutterActivity() {

    // Channel
    private lateinit var mMethodChannel: MethodChannel

    // 百度定位
    private lateinit var mLocationTools: LocationTools
    private lateinit var mListener: BDAbstractLocationListener

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val binaryMessenger: BinaryMessenger = flutterEngine.dartExecutor.binaryMessenger
        val name = "method_channel_location_info"
        val methodChannel = MethodChannel(binaryMessenger, name)
        methodChannel.setMethodCallHandler { call, result ->
            val method = call.method
            if (TextUtils.equals("executeLocation", method)) {
                executeLocation()
                result.success(0)
            } else {
                result.notImplemented()
            }
        }
        this.mMethodChannel = methodChannel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 停止定位
        mLocationTools.unRegisterLocationListener(mListener)
        mLocationTools.stop()
    }

    private fun initData() {
        // 定位初始化
        val tools = LocationTools(this, 0)
        val listener = SimpleBDAbstractLocationListener(object :
            SimpleBDAbstractLocationListener.OnLocationListener {
            override fun onLocation(location: BDLocation, address: String?) {
                var text = address
                if (TextUtils.isEmpty(address)) { // 定位失败
                    text = getString(R.string.location_failed)
                }
                mMethodChannel.invokeMethod("setLocation", text)
                // 停止定位
                mLocationTools.stop()
            }
        })
        tools.registerLocationListener(listener)
        this.mListener = listener
        this.mLocationTools = tools
    }

    /**
     * 启动定位
     */
    private fun executeLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permission = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (!checkSelfPermission(this, permission, REQUEST_CODE_LOCATION)) {
                return
            }
        }
        this.mLocationTools.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_LOCATION == requestCode) {
            if (PermissionUtils.onRequestPermissionsResult(grantResults)) {
                this.mLocationTools.start()
            }
        }
    }

}