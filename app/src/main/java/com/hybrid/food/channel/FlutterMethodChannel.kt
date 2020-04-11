package com.hybrid.food.channel

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

/**
 * @author YangJ
 */
class FlutterMethodChannel {

    companion object {
        private const val METHOD_CHANNEL_NAME = "method_channel"

        fun create(messenger: BinaryMessenger): MethodChannel {
            return MethodChannel(messenger, METHOD_CHANNEL_NAME)
        }
    }
}