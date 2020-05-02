package com.hybrid.food.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.hybrid.food.BuildConfig
import java.io.File

/**
 * Created by YangJ on 2018/12/17.
 * 用户头像操作工具类
 */
object PortraitUtils {

    /**
     * 打开系统相册
     */
    const val REQUEST_CODE_PICK_PHOTO = 0

    /**
     * 打开系统相机
     */
    const val REQUEST_CODE_PICK_CAMERA = 1

    /**
     * 打开系统裁剪
     */
    const val REQUEST_CODE_PICK_CROP = 2

    /**
     * 相机拍照临时文件
     */
    private const val DEFAULT_CAMERA_PATH = "camera.jpg"

    /**
     * 照片裁剪临时文件
     */
    private var mCachePath: String? = null

    /**
     * 从系统相机返回数据中获取uri
     */
    fun getUriByCamera(context: Context): Uri {
        val file = createOutputFile(context, DEFAULT_CAMERA_PATH)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authority = BuildConfig.APPLICATION_ID + ".provider"
            FileProvider.getUriForFile(context, authority, file)
        } else {
            Uri.fromFile(file)
        }
    }

    /**
     * 创建头像裁剪临时文件
     */
    private fun createOutputFile(context: Context?, child: String): File {
        return File(context?.externalCacheDir, child)
    }

    /**
     * 裁剪照片
     */
    private fun onCropPicture(context: Context, uri: Uri, width: Int, height: Int): Intent {
        val child = "${System.currentTimeMillis()}.jpg"
        val file = createOutputFile(context, child)
        this.mCachePath = file.absolutePath
        return onCropPicture(uri, width, height, Uri.fromFile(file))
    }

    /**
     * 裁剪照片
     */
    private fun onCropPicture(uri: Uri?, width: Int, height: Int, output: Uri): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        // 设置裁剪类型为图片
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", true)
        // 设置裁剪框宽高比例
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // 设置裁剪之后照片的宽度和高度
        intent.putExtra("outputX", width)
        intent.putExtra("outputY", height)
        // 是否保留比例
        intent.putExtra("scale", true)
        // 设置裁剪之后照片是否通过intent返回
        intent.putExtra("return-data", false)
        // 设置裁剪之后照片的格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        // 设置裁剪之后照片输出到自定义路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        return intent
    }

    /**
     * 打开系统相册
     */
    fun startSystemPhoto(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO)
    }

    /**
     * 打开系统相机
     */
    fun startSystemCamera(activity: Activity) {
        // 设置相机拍照临时文件
        val file = createOutputFile(activity, DEFAULT_CAMERA_PATH)
        // 创建intent并启动
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val authority = BuildConfig.APPLICATION_ID + ".provider"
            FileProvider.getUriForFile(activity, authority, file)
        } else {
            Uri.fromFile(file)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_CAMERA)
    }

    /**
     * 打开系统裁剪
     */
    fun startSystemCrop(activity: Activity, data: Uri) {
        startSystemCrop(activity, data, 0, 0)
    }

    /**
     * 打开系统裁剪
     */
    fun startSystemCrop(activity: Activity, data: Uri, width: Int, height: Int) {
        val intent = onCropPicture(activity, data, width, height)
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_CROP)
    }

    /**
     * 获取照片裁剪临时文件
     */
    fun getCachePath(): String? {
        return mCachePath
    }

    /**
     * 删除缓存的临时文件
     */
    fun deleteDirectory(context: Context) {
        deleteDirectory(context.externalCacheDir)
    }

    /**
     * 删除缓存的临时文件
     */
    private fun deleteDirectory(file: File) {
        file.listFiles().forEach {
            if (it.isFile && !TextUtils.equals(it.absolutePath, mCachePath)) {
                it.delete()
            }
        }
    }

}