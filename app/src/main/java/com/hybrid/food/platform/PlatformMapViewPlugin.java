package com.hybrid.food.platform;

import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewRegistry;

/**
 * @author YangJ
 */
public class PlatformMapViewPlugin {

    public static void registerWith(FlutterEngine engine, PlatformView view) {
        // 注册Android Native View
        PlatformViewRegistry registry = engine.getPlatformViewsController().getRegistry();
        String viewTypeId = "platform_map_view";
        PlatformMapViewFactory factory = new PlatformMapViewFactory(StandardMessageCodec.INSTANCE,
                view, engine.getDartExecutor().getBinaryMessenger());
        registry.registerViewFactory(viewTypeId, factory);
    }

}