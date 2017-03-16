package com.szamani.inkball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import static java.lang.Math.abs;


/**
 * Created by Sajjad on 1/20/2017.
 */

public class InkView extends SurfaceView implements SurfaceHolder.Callback {


    class InkThread extends Thread {



        private int ballRadius = 80;
        private int xPosBall= 0;
        private int yPosBall= -ballRadius;
        private Paint ballPaint;

        private int canvasWidth=1;
        private int canvasHeigth=1;
        private int screenH;
        private int screenL;

        private long timePrevFrame = 0;
        private long timeNow = 0;
        private final long shotDelay = 800;
        private long pastShot = 0;
        private int numShotsDrawn =0;

        private LinkedList<Point> ballCoords = new LinkedList();
        private ArrayList<Point> shotCoords = new ArrayList<>();

        private boolean running =true;
        private final Object mRunLock = new Object();

        private SurfaceHolder mSurfaceHolder;
        private Handler msgHandler;

        private  Drawable shooter;
        private final int shooterH = 100;
        private final int shooterL= 100;
        private double shooterXPos=400.00;
        private boolean touch = false;
        private double touchX = screenL / 2;

        private Drawable shot;
        private  int initShotY ;
        private final int shotH = 25;
        private final int shotL = 25;


        public InkThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
            mSurfaceHolder = surfaceHolder;
            currContext = context;
            msgHandler = handler;
            ballPaint = new Paint();
            ballPaint.setColor(Color.BLACK);
            shooter = currContext.getResources().getDrawable(R.drawable.shooter);
            shot = currContext.getResources().getDrawable(R.drawable.shot2);

            //get display size of device
            WindowManager wm = (WindowManager) currContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getRealSize(size);
            //180 offset added for buttons
            screenH=size.y - 180;
            screenL=size.x;
            initShotY =  screenH - shooterH;
            //add resourrces
            //initialize paints

        }


        public void draw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
            for (Point p : ballCoords) {
                canvas.drawCircle(p.x, p.y, ballRadius, ballPaint);
            }


            timeNow = System.currentTimeMillis() - 20;
            if (timeNow - timePrevFrame > 300) {
                Random rand = new Random();
                xPosBall = rand.nextInt(screenL - 2 * ballRadius) + ballRadius;
                yPosBall = - (rand.nextInt(500) + ballRadius);

                if(ballCoords.isEmpty() || ballCoords.getLast().y > 100){
                    canvas.drawCircle(xPosBall, yPosBall, ballRadius, ballPaint);
                    timePrevFrame = System.currentTimeMillis();
                    ballCoords.add(new Point(xPosBall, yPosBall));
                    xPosBall = 0;
                }
            }
            shooter.setBounds((int) shooterXPos, screenH - shooterH, (int) shooterXPos + shooterL, screenH);
            shooter.draw(canvas);
            //add 3 shots
            if(pastShot == 0){
                shotCoords.add(new Point((int) shooterXPos + shotL, initShotY));
                numShotsDrawn++;
                pastShot = timeNow;
            }else if(numShotsDrawn % 3 != 0) {
                shotCoords.add(new Point((int) shooterXPos + shotL, initShotY));
                numShotsDrawn++;
                pastShot = timeNow;
            }else if(timeNow - pastShot > shotDelay){
                pastShot = 0;
            }

            //Redraw prev and new shots
            for(Point p : shotCoords){
                shot.setBounds(p.x ,
                        p.y - shotH ,
                        p.x +  2*shotL ,
                        p.y + shotH );
                p.y=p.y-80;
                shot.draw(canvas);
            }


        }

        public boolean onTouch(MotionEvent event){
            int action = event.getAction();

            synchronized (mSurfaceHolder){
                if(action == MotionEvent.ACTION_DOWN){
                    touchX = event.getX();
                    touch = true;
                    //Log.d("hasTouched","touched");
                    return  true;
                }else if(action == MotionEvent.ACTION_UP){
                    touch = false;
                    return false;
                }
            }
            return false;
        }

        public void moveShooter() {
            if (touch == true) {
                if (touchX > screenL / 2) {
                    if (shooterXPos + 40 < screenL) {
                        shooterXPos += 40;
                    }
                } else if (touchX < screenL / 2) {
                    if (shooterXPos - 40 > 0) {
                        shooterXPos -= 40;
                    }
                }
            }
        }


        public void shoot(Canvas canvas){

        }


        public void updatePosition(){
            int offset = 16;
            checkCollision();
            if(!ballCoords.isEmpty()) {
                for (Point p : ballCoords) {//collision with shooter
                    if (p.y + ballRadius >= screenH - shooterH + offset) {
                        if (p.x - ballRadius + offset <= shooterXPos + shooterL && p.x + ballRadius - offset >= shooterXPos) {
                            running = false;
                            //Log.d("ball","hit");
                            return;
                        }
                    }
                    p.y += 20;
                }

                //out of screen
                if(ballCoords.getFirst().y + ballRadius >=screenH){
                    ballCoords.removeFirst();
                }


            }
            moveShooter();
        }



        public void checkCollision(){
            int offset = 16;
            int size = shotCoords.size();
            if(!ballCoords.isEmpty() && shotCoords.size()>=3) {
                for (Iterator<Point> it = ballCoords.iterator(); it.hasNext();) {
                    Point ball = it.next();
                    for (int i = 0; i < size; i++) {
                        if (ball.x - ballRadius - offset <= shotCoords.get(i).x + shotL
                                && ball.x + ballRadius - offset >= shotCoords.get(i).x
                                && abs(shotCoords.get(i).y - ball.y) < 60) {
                            shotCoords.remove(i);
                            i--;
                            size = shotCoords.size();
                            //Log.d("Collision","TRUE");
                            try {
                                it.remove();
                            }catch (IllegalStateException ex){
                                if(it.hasNext())
                                it.next();
                            }
                        }
                    }
                }
            }
        }

        public void eraseBall(){

        }

        public void pause(){
            synchronized (mSurfaceHolder) {
                //set state pause
            }
        }

        public void setSurfaceSize(int width, int height){
            synchronized (mSurfaceHolder){
                canvasHeigth=height;
                canvasWidth=width;
            }
        }

        public void setRunning(boolean val){
            synchronized (mRunLock){
                running=val;
            }
        }



        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                /*
                timeNow=System.currentTimeMillis();
                timeDelta=timeNow-timePrevFrame;
                if ( timeDelta < 16) {
                    try {
                        Thread.sleep(16 - timeDelta);
                    }
                    catch(InterruptedException e) {

                    }
                }
                timePrevFrame=System.currentTimeMillis();*/
                try {
                    canvas=mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder){
                        updatePosition();
                        draw(canvas);
                        try {
                            Thread.sleep(20);
                        }
                        catch(InterruptedException e) {

                        }
                    }
                }finally {
                    if(canvas!=null)
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private Context currContext;
    private InkThread thread;

    public InkView(Context context, AttributeSet atrs) {
        super(context, atrs);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new InkThread(holder,context, new Handler(){
            @Override
            public void handleMessage(Message m){


            }

        });
        setFocusable(true);
    }

    public InkThread getThread(){
        return thread;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return thread.onTouch(event);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        thread.setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // we have to tell thread to shut down & wait for it to finish
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

}
