package com.example.mytest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;


public class FloatingView extends View implements View.OnTouchListener{
    private WindowManager wm;
    private WindowManager.LayoutParams wmParams;
    private int height;
    private int width;
    private int x;
    private int y;
    private View contentView;
    private Button bt1;
    private Context appContext;

    public FloatingView(Context context, WindowManager wm) {
        super(context);
        this.appContext = context;
        this.wm = wm;
        contentView = View.inflate(context, R.layout.service_floating, null);
        initWmParams();

//        bt1 = (Button)contentView.findViewById(R.id.bt1);
//        bt1 = new Button(context);
//        bt1.setText("test");
        contentView.setOnTouchListener(this);
//        wm.addView(new Button(context), wmParams);
    }

    private void initWmParams(){
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.width = 200;
        wmParams.height = 80;
        wmParams.x = 300;
        wmParams.y = 300;

    }
    void show(){
        wm.addView(contentView, wmParams);
    }
    void clear(){
        wm.removeView(contentView);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x = (int)motionEvent.getRawX();
                y = (int)motionEvent.getRawY();
                System.out.println("x: "+x+ ", y: "+y);
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int)motionEvent.getRawX();
                int nowY = (int)motionEvent.getRawY();
                int moveX = nowX-x;
                int moveY = nowY-y;
                x = nowX;
                y = nowY;
                wmParams.x += moveX;
                wmParams.y += moveY;
                wm.updateViewLayout(view, wmParams);
                break;
        }
        //事件继续向外传播
        return false;
    }
}
