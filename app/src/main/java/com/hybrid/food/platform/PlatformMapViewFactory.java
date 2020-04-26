package com.hybrid.food.platform;

import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class PlatformMapViewFactory extends PlatformViewFactory {

    private BinaryMessenger mBinaryMessenger;
    private PlatformView mPlatformView;

    /**
     * @param createArgsCodec the codec used to decode the args parameter of {@link #create}.
     */
    public PlatformMapViewFactory(MessageCodec<Object> createArgsCodec, BinaryMessenger messenger,
                                  PlatformView view) {
        super(createArgsCodec);
        this.mBinaryMessenger = messenger;
        this.mPlatformView = view;
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        return mPlatformView;
    }
}
