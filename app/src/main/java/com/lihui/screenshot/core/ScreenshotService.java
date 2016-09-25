package com.lihui.screenshot.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.lihui.screenshot.core.ScreenshotObserver;

public class ScreenshotService extends Service {

    public static final String ACTION = "com.lihui.screenshot.core.ScreenshotService";

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenshotObserver.startObserve(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ScreenshotObserver.stopObserve();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
