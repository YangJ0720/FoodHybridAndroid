package com.hybrid.food.base

import android.app.Application
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.hybrid.food.boost.PageRouter
import com.hybrid.food.ui.MainActivity
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.Utils
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.idlefish.flutterboost.interfaces.INativeRouter
import io.flutter.embedding.android.FlutterView

/**
 * @author YangJ
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // 初始化百度地图SDK
        initBaiduMap()
        // 初始化flutter_boost混合栈框架
        val router = INativeRouter { context, url, urlParams, requestCode, _ ->
            val assembleUrl = Utils.assembleUrl(url, urlParams)
            PageRouter.openPageByUrl(context, assembleUrl, urlParams, requestCode)
        }
        val listener = object : FlutterBoost.BoostLifecycleListener {
            override fun onEngineCreated() {

            }

            override fun onPluginsRegistered() {

            }

            override fun beforeCreateEngine() {

            }

            override fun onEngineDestroy() {

            }

        }
        val platform = FlutterBoost.ConfigBuilder(this, router)
            .isDebug(true)
            .whenEngineStart(FlutterBoost.ConfigBuilder.ANY_ACTIVITY_CREATED)
            .renderMode(FlutterView.RenderMode.texture)
            .lifecycleListener(listener)
            .build()
        FlutterBoost.instance().init(platform)
    }

    private fun initBaiduMap() {
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        // 自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        // 包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}