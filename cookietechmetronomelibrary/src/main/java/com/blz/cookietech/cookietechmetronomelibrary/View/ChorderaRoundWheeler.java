package com.blz.cookietech.cookietechmetronomelibrary.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import com.blz.cookietech.cookietechmetronomelibrary.R;

public class ChorderaRoundWheeler extends View {
    Drawable wheeler;
    GestureDetector gestureDetector;
    private float angle = (float) 0.0;
    int width;
    int height;
    float percent;
    int wheelerSize = 0;
    int wheelerPadding = 0;
    float bpmTextSize;
    Paint bpmTextColor;

    Drawable  outerRing;
    Drawable wave;
    private int waveSize;
    private int middlePointX;
    private int middlePointY;
    private float bpmValueTextSize;
    private Paint bpmValueTextColor;
    Rect bpmValueTextBound,bpmTextBound,waveBound;
    private Paint outerRingProgressPaint;

    final RectF oval = new RectF();


    public ChorderaRoundWheeler(Context context) {
        super(context);
    }

    public ChorderaRoundWheeler(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context,new WheelerGestureListener());

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(getWidth() < getHeight()){
                    width = getWidth();
                    height = getWidth();
                }else{
                    width = getHeight();
                    height = getHeight();
                }


                middlePointX = width/2;
                middlePointY = height/2;


                wheeler = getResources().getDrawable(R.drawable.metronome_controller);
                wave  = getResources().getDrawable(R.drawable.ic_wave);
                wheelerPadding = width/10;
                Log.d("akash_wheeler", "onGlobalLayout: "+wheelerPadding);
                wheelerSize = width - wheelerPadding;

                wheeler.setBounds(wheelerPadding,wheelerPadding,wheelerSize,wheelerSize);
                outerRing = getResources().getDrawable(R.drawable.wheeler_progress);
                outerRing.setBounds(0,0,width,height);

                bpmValueTextSize = (float) width/5;
                bpmValueTextColor = new Paint();
                bpmValueTextColor.setColor(Color.WHITE);
                bpmValueTextColor.setTextSize(bpmValueTextSize);


                bpmValueTextBound = new Rect();
                bpmTextBound = new Rect();
                waveBound = new Rect();




                waveSize = width/10;
                wave.setBounds(0,0,waveSize,waveSize);

                bpmTextSize = (float) width/12;
                bpmTextColor = new Paint();
                bpmTextColor.setColor(Color.WHITE);
                bpmTextColor.setTextSize(bpmTextSize);


                outerRingProgressPaint = new Paint();
                outerRingProgressPaint.setStrokeWidth(width/20);
                outerRingProgressPaint.setStyle(Paint.Style.STROKE);
                outerRingProgressPaint.setShader(new LinearGradient(0, 0, 0, getHeight(), Color.parseColor("#0072BC"), Color.parseColor("#00D49A"), Shader.TileMode.CLAMP));

            }
        });

    }

    public ChorderaRoundWheeler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChorderaRoundWheeler(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        Log.d("akash_wheeler", "onDraw: ");
        canvas.save();
        angle = getAngleFromPercent(percent);
        canvas.rotate(angle,(float)width/2,(float)height/2);
        wheeler.draw(canvas);
        canvas.restore();
        outerRing.draw(canvas);



        String bpmValue = String.valueOf((int)percent);
        bpmValueTextColor.getTextBounds(bpmValue,0,bpmValue.length(),bpmValueTextBound);
        float bpmValueTextWidth = bpmValueTextBound.width();
        float bpmValueTextHeight = bpmValueTextBound.height();
        int bpmValuePositionX = (int) (middlePointX - bpmValueTextWidth/2 + waveSize/2);
        int bpmValuePositionY = (int) (middlePointY + bpmValueTextHeight/2);
        canvas.drawText(String.valueOf((int)percent),bpmValuePositionX,bpmValuePositionY,bpmValueTextColor);

        waveBound.left = bpmValuePositionX - waveSize - 10;
        waveBound.right = bpmValuePositionX - 10;
        waveBound.top = middlePointY - waveSize/2;
        waveBound.bottom = waveBound.top+waveSize;
        wave.setBounds(waveBound);
        wave.draw(canvas);


        float bpmTextWidth = bpmTextColor.measureText("BPM");
        int bpmPositionX = middlePointX - (int) bpmTextWidth /2;
        int bpmPositionY = (int) (middlePointY - bpmValueTextHeight);
        canvas.drawText("BPM",bpmPositionX,bpmPositionY,bpmTextColor);



        float radius;

        if (width > height) {
            radius = (float) (height / 2.222) + width/40;
        } else {
            radius = (float) (width / 2.222) + width/40;
        }


        float center_x, center_y;


        center_x = width / 2;
        center_y = height / 2;

        float angle = getAngleFromPercent(percent);
        if(angle < 0)
            angle = angle + 360;

        Log.d("akash_wheeler", "onDraw: degree" + getAngleFromPercent(percent) );

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, -90, angle, false, outerRingProgressPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }




     class WheelerGestureListener extends GestureDetector.SimpleOnGestureListener{
        float prevAngle = 0;
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("akash_wheeler", "onDown: ");
            prevAngle = 0;
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("akash_wheeler", "onScroll: " + distanceY);
            float x = e2.getX() / ((float) getWidth());
            float y = e2.getY() / ((float) getHeight());
            float rotDegrees = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction

            if (! Float.isNaN(rotDegrees)) {
                // instead of getting 0-> 180, -180 0 , we go for 0 -> 360
                float posDegrees = rotDegrees;
                if (rotDegrees < 0) posDegrees = 360 + rotDegrees;

                float change =  posDegrees - prevAngle;

                Log.d("akash_wheeler", "onScroll: " + prevAngle + " "+ posDegrees+ " "+ change + " " + (change * 100 /360))  ;
                if(posDegrees == change || change > 20 || change <-20){
                    prevAngle = posDegrees;
                    return true;
                }
                float tempPercent = percent + (change * 100 /360);
                if((tempPercent >100&&percent ==100) || (tempPercent < 0 && percent ==0)){
                    return true;
                }
                else if(tempPercent > 100 && percent < 100 ){
                    percent = 100;
                }else if(tempPercent < 0 && percent > 0){
                    percent = 0;
                }else{
                    percent =tempPercent;
                }
                Log.d("akash_wheeler", "onScroll: " + percent);
                
                invalidate();
                prevAngle = posDegrees;
                return true; //consumed
            } else
                return false; // not consumed
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            float x = e.getX() / ((float) getWidth());
            float y = e.getY() / ((float) getHeight());
            float mAngleDown = cartesianToPolar(1 - x, 1 - y);// 1- to correct our custom axis direction
            percent = getPercentFromAngle(mAngleDown);
            invalidate();

            Log.d("akash_wheeler", "onSingleTapUp: ");
            return true;
        }
    }

    private int getPercentFromAngle(float mAngleDown) {
        if(mAngleDown > 0){
            return (int) ((mAngleDown * 100) /360);
        }else{
            return (int) (((mAngleDown + 360)*100)/360);
        }
    }


    private float getAngleFromPercent(float percent) {
        return (360 * percent/100);
    }

    private float cartesianToPolar(float x, float y) {
        return (float) -Math.toDegrees(Math.atan2(x - 0.5f, y - 0.5f));
    }






}
