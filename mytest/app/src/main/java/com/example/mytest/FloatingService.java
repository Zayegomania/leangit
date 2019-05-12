package com.example.mytest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

// stopSelf() 服务停止运行
public class FloatingService extends Service {
    private FloatingManager floatingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        floatingManager = new FloatingManager(getApplicationContext(), (WindowManager)getSystemService(WINDOW_SERVICE));
        floatingManager.showFloatView();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("FloatingService:","Destroy ....");
        floatingManager.removeFloatView();
        super.onDestroy();
    }
}
