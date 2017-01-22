package com.szamani.inkball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.szamani.inkball.InkView.InkThread;


public class GamePlay extends Activity {
    private InkThread mInkThread;
    private InkView mInkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Navigation and notification bar hidden
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_game_play);



    }
}


    //    private Activity activity;
//    public ImageView ballImage;
//    private FrameLayout mainLayout;
//
//    private int time;
//    public int delay=0;
//
//
//    Ball(Activity activity, int time) {
//
//        this.activity = activity;
//        this.time=time;
//    }
//
//    public void configureBallImage(int x, int y, FrameLayout layout){
//        mainLayout = layout;
//        ballImage = new ImageView(activity);
//        ballImage.setImageResource(R.drawable.ball);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(x, y, 0, 0);
//        ballImage.setLayoutParams(params);
//        mainLayout.addView(ballImage);
//    }
//
//    public void moveBall(float xPos, float yPos, Animator.AnimatorListener listener) {
//        ViewPropertyAnimator animator = ballImage.animate();
//        animator.translationX(xPos);
//        animator.translationY(yPos);
//        animator.setDuration(time);
//        animator.setListener(listener);
//        animator.setStartDelay(delay);
//        animator.start();
//
//
//    }


//    public void eraseBall(){





