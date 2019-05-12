package com.example.mytest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private final static String TAG = "MainActivity";
    private View bt1, bt2;
    private View test;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.start_service);
        bt2 = findViewById(R.id.stop_service);
        test = (Button)findViewById(R.id.test);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        test.setOnClickListener(this);
        if(null != test.getWindowToken()){
            init();
        }
    }
    public void init(){
        Button b = new Button(getApplicationContext());
        b.setText("hello");
        final PopupWindow popupWindow = new PopupWindow(b, 200,100, true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
//      show popupWindow
        popupWindow.showAsDropDown(test);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                            popupWindow.dismiss();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start_service:
                Log.d(TAG, "start floatingService .....");
                startService(new Intent(this, FloatingService.class));
                break;
            case R.id.stop_service:
                Log.d(TAG, "stop floatingService .....");
                stopService(new Intent(this, FloatingService.class));
                break;
            case R.id.test:
                init();
                break;
            default:break;
        }
    }
}
