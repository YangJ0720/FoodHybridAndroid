package com.hybrid.food.platform;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class PlatformTextView implements PlatformView {

    private TextView mTextView;

    // TAG
    private static final String TAG = "PlatformTextView";

    public PlatformTextView(Context context, BinaryMessenger messenger, int id) {
        TextView textView = new TextView(context);
        textView.setText("Hello Flutter");
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundColor(Color.BLUE);
        this.mTextView = textView;
    }

    @Override
    public View getView() {
        return mTextView;
    }

    @Override
    public void dispose() {
        Log.e(TAG, "dispose: ");
    }

}
