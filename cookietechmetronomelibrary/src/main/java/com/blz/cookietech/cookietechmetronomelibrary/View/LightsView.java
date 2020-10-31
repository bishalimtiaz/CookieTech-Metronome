package com.blz.cookietech.cookietechmetronomelibrary.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

public class LightsView extends View {

    private static final String TAG = "LightsView";

    private int height,width,middlePointX,getMiddlePointY;
    private Paint lightOffPaint,lightOnPaint;
    private int lightNumber;
    private int radius;
    private int distanceBtnTwoCenter;
    public LightsView(Context context) {
        super(context);
    }

    public LightsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                width = getWidth();
                height = getHeight();
                middlePointX = width/2;
                getMiddlePointY = height/2;
                int radiusX,radiusY;
                radiusY = height/5; // circle height will be 40% off parent. 40%/2 = radiusY

                Log.d(TAG, "onGlobalLayout: width : " + width);
                Log.d(TAG, "onGlobalLayout: height : " + height);
                Log.d(TAG, "onGlobalLayout: lightNumber : " + lightNumber);

                distanceBtnTwoCenter = width/(lightNumber + 1);
                radiusX = (distanceBtnTwoCenter/2) - (distanceBtnTwoCenter/10);

                radius = Math.min(radiusX,radiusY);

                Log.d(TAG, "onGlobalLayout: radiusX : " + radiusX);
                Log.d(TAG, "onGlobalLayout: radiusY : " + radiusY);
                Log.d(TAG, "onGlobalLayout: radius : " + radius);


                lightOffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                lightOffPaint.setColor(Color.parseColor("#132334")); //OFF color;
                lightOnPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                lightOnPaint.setColor(Color.parseColor("#3495FF")); //ON color;

                //lightPaint.stroke
                return true;
            }
        });

    }

    public LightsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LightsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw: called");
        drawLight(canvas);

    }

    private void drawLight(Canvas canvas) {
        if (lightOffPaint != null){
           for(int i = 1;i <= lightNumber;i++){

               canvas.drawCircle(distanceBtnTwoCenter*i, getMiddlePointY, radius, lightOffPaint);
           }
        }
    }

   /* private void setLightOn(Canvas canvas) {
        lightOffPaint.reset();
        lightOffPaint.setColor(Color.parseColor("#3495FF"));
        invalidate();
    }

    private void setLightOff(Canvas canvas) {
        lightOffPaint.reset();
        lightOffPaint.setColor(Color.parseColor("#132334"));
        invalidate();
    }*/

    public void setLightNumber(int lightNumber){

        this.lightNumber = lightNumber;
        invalidate();

    }

}
