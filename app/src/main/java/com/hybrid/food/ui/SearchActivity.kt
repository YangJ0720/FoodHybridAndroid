package com.hybrid.food.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.hybrid.food.R
import com.hybrid.food.voice.AutoCheck
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import org.json.JSONObject
import java.util.ArrayList
import java.util.LinkedHashMap

/**
 * @author YangJ 搜索界面
 */
class SearchActivity : BoostFlutterActivity(), EventListener {

    private var mMethodChannel: MethodChannel? = null

    private lateinit var asr: EventManager
    private var enableOffline = false // 测试离线命令词，需要改成true

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val name = "method_channel_search_voice"
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, name)
        channel.setMethodCallHandler { call, result ->
            val method = call.method
            when {
                TextUtils.equals("start", method) -> {
                    start()
                    result.success(0)
                }
                TextUtils.equals("stop", method) -> {
                    stop()
                    result.success(0)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
        this.mMethodChannel = channel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initPermission()
    }

    override fun onPause() {
        super.onPause()
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        Log.i("ActivityMiniRecog", "On pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0)
        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this)
        //
        mMethodChannel?.setMethodCallHandler(null)
        mMethodChannel = null
    }

    private fun initData() {
        // 基于sdk集成1.1 初始化EventManager对象
        // 基于sdk集成1.1 初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr")
        // 基于sdk集成1.3 注册自己的输出事件类
        // 基于sdk集成1.3 注册自己的输出事件类
        asr.registerListener(this) //  EventListener 中 onEvent方法

    }

    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val toApplyList = ArrayList<String>()
        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (toApplyList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }

    /**
     * 基于SDK集成2.2 发送开始事件
     * 点击开始按钮
     * 测试参数填在这里
     */
    private fun start() {
        val params: MutableMap<String?, Any?> = LinkedHashMap()
        var event: String? = null
        event = SpeechConstant.ASR_START // 替换成测试的event
        if (enableOffline) {
            params[SpeechConstant.DECODER] = 2
        }
        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        // params.put(SpeechConstant.NLU, "enable");
        // params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
        // params.put(SpeechConstant.IN_FILE, "res:///com/baidu/android/voicedemo/16k_test.pcm");
        // params.put(SpeechConstant.VAD, SpeechConstant.VAD_DNN);
        // params.put(SpeechConstant.PID, 1537); // 中文输入法模型，有逗号
        /* 语音自训练平台特有参数 */ // params.put(SpeechConstant.PID, 8002);
        // 语音自训练平台特殊pid，8002：模型类似开放平台 1537  具体是8001还是8002，看自训练平台页面上的显示
        // params.put(SpeechConstant.LMID,1068); // 语音自训练平台已上线的模型ID，https://ai.baidu.com/smartasr/model
        // 注意模型ID必须在你的appId所在的百度账号下
        /* 语音自训练平台特有参数 */ /* 测试InputStream*/ // InFileStream.setContext(this);
        // params.put(SpeechConstant.IN_FILE, "#com.baidu.aip.asrwakeup3.core.inputstream.InFileStream.createMyPipedInputStream()");
        // 请先使用如‘在线识别’界面测试和生成识别参数。 params同ActivityRecog类中myRecognizer.start(params);
        // 复制此段可以自动检测错误
        AutoCheck(applicationContext, object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == 100) {
                    val autoCheck: AutoCheck = msg.obj as AutoCheck
                    synchronized(autoCheck) {
                        val message: String = autoCheck.obtainErrorMessage() // autoCheck.obtainAllMessage();
//                        txtLog.append(message + "\n")
                        // 可以用下面一行替代，在logcat中查看代码
                        println(message)
                    }
                }
            }
        }, enableOffline).checkAsr(params)
        var json: String? = null // 可以替换成自己的json
        json = JSONObject(params).toString() // 这里可以替换成你需要测试的json
        asr.send(event, json, null, 0, 0)
    }

    /**
     * 点击停止按钮
     * 基于SDK集成4.1 发送停止事件
     */
    private fun stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0) //
    }

    // 基于sdk集成1.2 自定义输出事件类 EventListener 回调方法
    // 基于SDK集成3.1 开始回调事件
    override fun onEvent(name: String, params: String?, data: ByteArray?, offset: Int, length: Int) {
        var logTxt = "name: $name"
        if (name == SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL) { // 识别相关的结果都在这里
            if (params == null || params.isEmpty()) {
                return
            }
            if (params.contains("\"nlu_result\"")) { // 一句话的语义解析结果
                if (length > 0 && data != null && data.isNotEmpty()) {
                    logTxt += ", 语义解析结果：" + String(data, offset, length)
                }
            } else if (params.contains("\"partial_result\"")) { // 一句话的临时识别结果
                logTxt += ", 临时识别结果：$params"
            } else if (params.contains("\"final_result\"")) { // 一句话的最终识别结果
                logTxt += ", 最终识别结果：$params"
                getResult(params)
            } else { // 一般这里不会运行
                logTxt += " ;params :$params"
                if (data != null) {
                    logTxt += " ;data length=" + data.size
                }
            }
        } else { // 识别开始，结束，音量，音频数据回调
            if (params != null && !params.isEmpty()) {
                logTxt += " ;params :$params"
            }
            if (data != null) {
                logTxt += " ;data length=" + data.size
            }
        }
        println(logTxt)
    }

    private fun getResult(params: String) {
        val json = JSONObject(params)
        var result = json.optString("results_recognition")
        result = if (TextUtils.isEmpty(result)) {
            getString(R.string.voice_unrecognized)
        } else {
            val length = result.length
            result.substring(2, length - 3)
        }
        Toast.makeText(this,  result, Toast.LENGTH_SHORT).show()
    }
}
