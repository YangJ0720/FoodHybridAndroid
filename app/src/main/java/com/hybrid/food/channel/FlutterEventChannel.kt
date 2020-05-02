package com.hybrid.food.channel

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

/**
 * @author YangJ
 */
class FlutterEventChannel {

    companion object {

        fun factory(messenger: BinaryMessenger, name: String): EventChannel {
            return EventChannel(messenger, name)
        }
    }

}