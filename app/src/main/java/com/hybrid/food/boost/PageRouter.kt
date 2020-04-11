package com.hybrid.food.boost

import android.app.Activity
import android.content.Context
import android.util.Log
import com.idlefish.flutterboost.containers.BoostFlutterActivity

object PageRouter {

    private const val TAG = "PageRouter"
    // 应用程序首页
    const val URL_HOME = "sample://home"
    // 设置界面
    const val URL_SYSTEM_SETTINGS = "sample://system_settings"
    // 用户信息界面
    const val URL_USER_INFO = "sample://user_info"
    const val FLUTTER_FRAGMENT_PAGE_URL = "sample://flutterFragmentPage"

    private val pageName: Map<String, String> by lazy {
        val hashMap = HashMap<String, String>()
        hashMap[URL_HOME] = "home"
        hashMap[URL_SYSTEM_SETTINGS] = "system_settings"
        hashMap[URL_USER_INFO] = "user_info"
        hashMap
    }

    fun openPageByUrl(context: Context, url: String, params: Map<*, *>): Boolean {
        return openPageByUrl(context, url, params, 0)
    }

    fun openPageByUrl(
        context: Context,
        url: String,
        params: Map<*, *>,
        requestCode: Int
    ): Boolean {
        val path = url.split("\\?").toTypedArray()[0]
        Log.i(TAG, "openPageByUrl = $path")
        return try {
            when {
                pageName.containsKey(path) -> {
                    val url = pageName[path] ?: return false
                    val intent = BoostFlutterActivity.withNewEngine()
                        .url(url)
                        .params(params)
                        .backgroundMode(BoostFlutterActivity.BackgroundMode.opaque)
                        .build(context)
                    if (context is Activity) {
                        context.startActivityForResult(intent, requestCode)
                    } else {
                        context.startActivity(intent)
                    }
                    return true
                }
//                url.startsWith(FLUTTER_FRAGMENT_PAGE_URL) -> {
//                    context.startActivity(Intent(context, FlutterFragmentPageActivity::class.java))
//                    return true
//                }
//                url.startsWith(NATIVE_PAGE_URL) -> {
//                    context.startActivity(Intent(context, NativePageActivity::class.java))
//                    return true
//                }
                else -> false
            }
        } catch (t: Throwable) {
            false
        }
    }
}