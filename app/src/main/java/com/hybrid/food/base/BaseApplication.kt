package com.hybrid.food.base

import android.app.Application
import com.hybrid.food.boost.PageRouter
import com.idlefish.flutterboost.FlutterBoost
import com.idlefish.flutterboost.Utils
import com.idlefish.flutterboost.interfaces.INativeRouter
import io.flutter.embedding.android.FlutterView

/**
 * @author YangJ
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
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
}