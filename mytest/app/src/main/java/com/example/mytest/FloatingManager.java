package com.example.mytest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import java.text.DecimalFormat;

public class FloatingManager {
    private final static String TAG = "FloatingManager";
    private WindowManager wManager;
    private WindowManager.LayoutParams wParams;
    private Context mContext;
    private Button hintBtn;
    private static LinearLayout viewHolder;
    private static Button timeBtn;
    private TimeTicker mHandler;
    private static long baseTimer;
    private int width = 240;
    private int height = 60;
    public FloatingManager(Context context, WindowManager wm) {
        mContext = context;
        wManager = wm;
        initParams();
        initWidget();
        mHandler = new TimeTicker();

    }
    private void initWidget(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1,-1);
        viewHolder = new LinearLayout(mContext);
        viewHolder.setLayoutParams(params);
        viewHolder.setOrientation(LinearLayout.VERTICAL);

        timeBtn = new Button(mContext);
        timeBtn.setText("00:00:00");
        timeBtn.setBackgroundResource(R.drawable.play);
        timeBtn.setPadding(40,0,0,0);
        viewHolder.addView(timeBtn, wParams);

        timeBtn.setOnTouchListener(new FloatingOnTouchListener());
        timeBtn.setOnClickListener(new FloatingOnClickListener());

    }

    private void initParams() {
        wParams = new WindowManager.LayoutParams();
        wParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        wParams.format = PixelFormat.RGBA_8888;
        wParams.alpha = 0.4f;
        Point p = new Point();
        wManager.getDefaultDisplay().getSize(p);
        wParams.width = width;
        wParams.height = height;
        wParams.x = p.x/2;
        wParams.y = (int)(-p.y/2+height*1.5);
        System.out.println("x:"+p.x);
        System.out.println("y:"+p.y);
    }
    public void showFloatView(){
        wManager.addView(viewHolder, wParams);
    }
    public void removeFloatView(){
        wManager.removeView(viewHolder);
    }

    static class TimeTicker extends Handler{
        @Override
        public void handleMessage(Message msg) {
            int time = (int)((SystemClock.elapsedRealtime() - baseTimer) / 1000);
            String hh = new DecimalFormat("00").format(time / 3600);
            String mm = new DecimalFormat("00").format(time % 3600 / 60);
            String ss = new DecimalFormat("00").format(time % 60);
            timeBtn.setText(hh + ":" + mm + ":" + ss);
            sendMessageDelayed(Message.obtain(this, 0x0), 1000);
        }
    }
    class FloatingOnTouchListener implements View.OnTouchListener{
        int x ,y;
        long startTime, endTime;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch(motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startTime = System.currentTimeMillis();
                    x = (int)motionEvent.getRawX();
                    y = (int)motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    int nowX = (int)motionEvent.getRawX();
                    int nowY = (int)motionEvent.getRawY();
                    int moveX = nowX-x;
                    int moveY = nowY-y;
                    x = nowX;
                    y = nowY;
                    wParams.x += moveX;
                    wParams.y += moveY;
                    wManager.updateViewLayout(viewHolder, wParams);
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    long elapsed = endTime - startTime;
                    if(elapsed > 200){
                        return true;
                    }
            }
            //事件继续向外传播
            return false;
        }
    }
    class FloatingOnClickListener implements View.OnClickListener{
        String state = "READY";
        @Override
        public void onClick(View v) {
            switch(state){
                case "READY":
                    baseTimer = SystemClock.elapsedRealtime();
                    timeBtn.setBackgroundResource(R.drawable.pause);
                    mHandler.sendMessageDelayed(Message.obtain(mHandler, 0x0), 1000);
                    state = "ONGOING";
                    break;
                case "ONGOING":
                    Log.d(TAG,"ONGOING");
                    mHandler.removeCallbacksAndMessages(null);
                    hintBtn= new Button(mContext);
                    hintBtn.setText("hello");
                    viewHolder.addView(hintBtn);
                    wParams.height = (int)(height*2.2);
                    wManager.updateViewLayout(viewHolder, wParams);
                    state = "COMPLETED";
                    break;
            }
        }
    }


}
