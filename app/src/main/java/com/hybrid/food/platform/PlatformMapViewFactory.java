package com.hybrid.food.platform;

import android.content.Context;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * @author YangJ
 */
public class PlatformMapViewFactory extends PlatformViewFactory {

    private PlatformView mView;
    private BinaryMessenger mBinaryMessenger;

    /**
     * @param createArgsCodec the codec used to decode the args parameter of {@link #create}.
     */
    public PlatformMapViewFactory(MessageCodec<Object> createArgsCodec, PlatformView view,
                                  BinaryMessenger messenger) {
        super(createArgsCodec);
        this.mView = view;
        this.mBinaryMessenger = messenger;
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        return mView;
    }
}
