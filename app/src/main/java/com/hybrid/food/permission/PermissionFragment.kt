package com.hybrid.food.permission

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import java.security.InvalidParameterException
import java.util.*

private const val ARG_PARAM_PERMISSIONS = "permissions"

/**
 * @author YangJ
 */
class PermissionFragment : Fragment() {

    private var mPermissions: Array<String>? = null

    private var mListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mModCount++
        arguments?.let {
            mPermissions = it.getStringArray(ARG_PARAM_PERMISSIONS)
            mPermissions?.let {
                requestPermissions(it, mModCount)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mListener = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (mModCount == requestCode) {
            // 判断是否授权
            val length = permissions.size
            val list = ArrayList<String>(length)
            for (i in 0 until length) {
                // 判断是否获取权限
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    val hasRefuse = ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissions[i])
                    if (hasRefuse) {
                        // 拒绝权限
                        list.add(permissions[i])
                    } else {
                        // 永久拒绝权限申请
                        list.add(permissions[i])
                    }
                }
            }
            // 回调
            mListener?.let {
                if (list.isEmpty()) {
                    it.onGranted()
                } else {
                    it.onDenied(list)
                }
            }
        }
        // 销毁Fragment
        fragmentManager?.let {
            val transaction = it.beginTransaction()
            transaction.remove(this)
            transaction.commit()
        }
    }

    companion object {

        private var mModCount = 0

        private const val TAG = "BlankFragment"

        @JvmStatic
        private fun newInstance(permissions: Array<String>, @Nullable listener: PermissionListener) =
            PermissionFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(ARG_PARAM_PERMISSIONS, permissions)
                }
                this.mListener = listener
            }

        @JvmStatic
        fun requestPermission(context: Context, permissions: Array<String>, listener: PermissionListener) {
            val manager = get(context)
            val transaction = manager.beginTransaction()
            val fragment = newInstance(permissions, listener)
            transaction.add(fragment, TAG)
            transaction.commit()
        }

        @JvmStatic
        private fun get(context: Context): FragmentManager {
            if (context == null) throw NullPointerException("You cannot request permission on a null Context")
            if (context is FragmentActivity) {
                return context.supportFragmentManager
            } else if (context is ContextWrapper) {
                return get(context.baseContext)
            }
            throw InvalidParameterException("Invalid Context")
        }
    }
}
