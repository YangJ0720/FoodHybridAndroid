package com.hybrid.food.boost

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.hybrid.food.ui.*
import com.idlefish.flutterboost.containers.BoostFlutterActivity

object PageRouter {

    // 应用程序首页
    const val URL_HOME = "sample://home"

    // 设置界面
    const val URL_SYSTEM_SETTINGS = "sample://system_settings"

    // 用户信息界面
    const val URL_USER_INFO = "sample://user_info"

    // 商铺信息界面
    const val URL_STORE_INFO = "sample://store_info"

    // 商品列表界面
    const val URL_PRODUCT_INFO = "sample://product_info"

    // 骑手送货位置信息界面
    const val URL_TRANSPORT_MAP = "sample://transport_map"

    // 搜索界面
    const val URL_SEARCH_INFO = "sample://search_info"

    // 选择收货地址界面
    const val URL_LOCATION_INFO = "sample://location_info"
    //
    const val URL_SHOW = "sample://show"

    //
    const val NATIVE_PAGE_URL = "sample://nativePage"

    //
    const val FLUTTER_FRAGMENT_PAGE_URL = "sample://flutterFragmentPage"

    private val pageName: Map<String, Class<out BoostFlutterActivity>> by lazy {
        val hashMap = HashMap<String, Class<out BoostFlutterActivity>>()
        hashMap[URL_HOME] = MainActivity::class.java
        hashMap[URL_SYSTEM_SETTINGS] = SystemSettings::class.java
        hashMap[URL_USER_INFO] = UserInfoActivity::class.java
        hashMap[URL_STORE_INFO] = StoreInfoActivity::class.java
        hashMap[URL_PRODUCT_INFO] = ProductInfoActivity::class.java
        hashMap[URL_TRANSPORT_MAP] = TransportMapActivity::class.java
        hashMap[URL_SEARCH_INFO] = SearchActivity::class.java
        hashMap[URL_LOCATION_INFO] = LocationActivity::class.java
        hashMap[URL_SHOW] = ShowActivity::class.java
        hashMap
    }

    fun openPageByUrl(context: Context, url: String, params: Map<*, *>): Boolean {
        return openPageByUrl(context, url, params, 0)
    }

    fun openPageByUrl(context: Context, url: String, params: Map<*, *>, requestCode: Int): Boolean {
        val path = url.split("?")[0]
        return try {
            when {
                pageName.containsKey(path) -> {
                    val activityClass = pageName[path] ?: return false
                    val intent = BoostFlutterActivity.NewEngineIntentBuilder(activityClass)
                        .url(path)
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
                url.startsWith(FLUTTER_FRAGMENT_PAGE_URL) -> {
//                    context.startActivity(Intent(context, FlutterFragmentPageActivity::class.java))
                    return true
                }
                url.startsWith(NATIVE_PAGE_URL) -> {
                    context.startActivity(Intent(context, TransportMapActivity::class.java))
                    return true
                }
                else -> false
            }
        } catch (t: Throwable) {
            false
        }
    }
}