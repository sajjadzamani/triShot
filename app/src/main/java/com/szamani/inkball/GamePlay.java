package com.szamani.inkball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.szamani.inkball.InkView.InkThread;

import java.io.Console;


public class GamePlay extends Activity {
    private InkThread mInkThread;
    private InkView mInkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Navigation and notification bar hidden
        final View decorView = getWindow().getDecorView();
         int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
                            decorView.setSystemUiVisibility(option);
                        }

                    }
                 });
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game_play);
        Button btnR = (Button)findViewById(R.id.btnRight);
        Button btnL = (Button)findViewById(R.id.btnLeft);
        Button btnRetry = (Button) findViewById(R.id.btnRetry);
        TextView tx = (TextView) findViewById(R.id.textView);
        mInkView = (InkView) findViewById(R.id.inkball);
        mInkView.setTextView(tx);
        mInkView.isButtonClicked(btnR, btnL);
        mInkView.isRetryClicked(btnRetry);
    }
}



