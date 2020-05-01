package com.hybrid.food.ui

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.hybrid.food.utils.PortraitUtils
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * @author YangJ 用户信息界面
 */
class UserInfoActivity : BoostFlutterActivity() {

    private var mMethodChannel: MethodChannel? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val name = "method_channel_user_info"
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, name)
        channel.setMethodCallHandler { call, result ->
            val method = call.method
            if (TextUtils.equals("getPhoto", method)) {
                getPhoto()
                result.success(0)
            } else {
                result.notImplemented()
            }
        }
        this.mMethodChannel = channel
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (Activity.RESULT_OK != resultCode) return
        when (requestCode) {
            PortraitUtils.REQUEST_CODE_PICK_PHOTO -> { // 从相册选择
                data?.let {
                    PortraitUtils.startSystemCrop(this, it.data)
                }
            }
            PortraitUtils.REQUEST_CODE_PICK_CAMERA -> { // 拍照
                val uri = PortraitUtils.getUriByCamera(this)
                PortraitUtils.startSystemCrop(this, uri)
            }
            PortraitUtils.REQUEST_CODE_PICK_CROP -> { // 裁剪
                data?.let {
                    val path = it.data.path
                    this.mMethodChannel?.invokeMethod("setPhoto", path)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMethodChannel?.setMethodCallHandler(null)
        mMethodChannel = null
    }

    private fun getPhoto() {
        PortraitUtils.startSystemPhoto(this)
    }
}
