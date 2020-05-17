package com.hybrid.food.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * @author YangJ
 */
object PermissionUtils {

    @JvmStatic
    fun onRequestPermissionsResult(grantResults: IntArray): Boolean {
        var result = true
        for (i in grantResults) {
            if (PackageManager.PERMISSION_DENIED == i) {
                result = false
                break
            }
        }
        return result
    }
}

/**
 * 动态权限申请
 */
fun checkSelfPermission(activity: Activity, permission: Array<String>, requestCode: Int): Boolean {
    val list = permission.filter {
        val state = ContextCompat.checkSelfPermission(activity, it)
        PackageManager.PERMISSION_DENIED == state
    }
    if (list.isNotEmpty()) {
        ActivityCompat.requestPermissions(activity, list.toTypedArray(), requestCode)
        return false
    }
    return true
}