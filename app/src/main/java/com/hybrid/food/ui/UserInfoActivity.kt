package com.hybrid.food.ui

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hybrid.food.R
import com.hybrid.food.utils.PortraitUtils
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * @author YangJ 用户信息界面
 */
class UserInfoActivity : BoostFlutterActivity() {

    private var mMethodChannel: MethodChannel? = null

    companion object {
        const val EXTRA_PATH = "extra_path"
    }

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
                    val uri = it.data
                    val path = if (uri == null) {
                        PortraitUtils.getCachePath()
                    } else {
                        uri.path
                    }
                    this.mMethodChannel?.invokeMethod("setPhoto", path)
                    // 将用户选择图片的结果返回给上一个界面
                    val data = Intent()
                    data.putExtra(EXTRA_PATH, path)
                    setResult(Activity.RESULT_OK, data)
                }
                // 删除缓存的临时文件
                PortraitUtils.deleteDirectory(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMethodChannel?.setMethodCallHandler(null)
        mMethodChannel = null
    }

    private fun showDialog() {
        val items = resources.getStringArray(R.array.item_select_portrait)
        AlertDialog.Builder(this).setItems(items, DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
            // 判断用户选择
            when (items[which]) {
                getString(R.string.select_portrait_gallery) -> { // 从相册选择
                    PortraitUtils.startSystemPhoto(this)
                }
                getString(R.string.select_portrait_camera) -> { // 拍照
                    PortraitUtils.startSystemCamera(this)
                }
            }
        }).show()
    }

    private fun getPhoto() {
        showDialog()
    }
}
