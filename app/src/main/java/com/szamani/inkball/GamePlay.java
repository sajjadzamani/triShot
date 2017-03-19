package com.szamani.inkball;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
        //get screen size
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(size);

        setContentView(R.layout.activity_game_play);
        //get buttons and txtview from xml
        Button btnR = (Button)findViewById(R.id.btnRight);
        Button btnL = (Button)findViewById(R.id.btnLeft);
        Button btnRetry = (Button) findViewById(R.id.btnRetry);
        TextView tx = (TextView) findViewById(R.id.textView);
        Typeface typeface= Typeface.createFromAsset(getAssets(), "fonts/trench100free.ttf");
        tx.setTypeface(typeface);
        btnL.setTypeface(typeface);
        btnR.setTypeface(typeface);
        btnRetry.setTypeface(typeface);

        mInkView = (InkView) findViewById(R.id.inkball);
        //set height of view
        mInkView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,size.y-dpToPx(48)));
        mInkView.setTextView(tx);
        mInkView.isButtonClicked(btnR, btnL);
        mInkView.isRetryClicked(btnRetry);
    }

    private int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}



