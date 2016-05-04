package com.dtcj.liukai.autoscrollanimation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * Author: liuk
 * Created at: 16/5/3
 */
public class AutoScrollView extends View {

    private int imageOneLeftLocation = 0;
    private int imageTwoLeftLocation = 0;
    private int imageOneWidth;
    private int imageTwoWidth;
    private int screenWidth = 1080;     //最大屏幕宽度

    private int top = 0;

    private int step = 1;   //越小越慢,保证是两张图宽度的公约数
    private int allStep = 0;    //移动值
    private boolean isRightFirst = true;
    private int direction = -1;     //0 left  1 right  -1 不滚动

    private Paint paint;
    private Bitmap bitmap, bitmaps;


    public AutoScrollView(Context context) {
        super(context);
        init();
    }

    public AutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);   //抗锯齿
        paint.setStyle(Paint.Style.STROKE);
        direction = -1;

        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (direction == 1) {   //右滑
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, imageOneLeftLocation, top, paint);
                canvas.drawBitmap(bitmaps, imageTwoLeftLocation, top, paint);

                imageOneLeftLocation += step;
                imageTwoLeftLocation += step;
                allStep += step;

                if (allStep == imageOneWidth && isRightFirst) { //第二张图片在前，第一张图片在后
                    imageTwoLeftLocation = screenWidth - imageTwoWidth;
                    imageOneLeftLocation = imageTwoLeftLocation + (-imageOneWidth);
                    allStep = 0;
                    isRightFirst = false;
                } else if (allStep == imageTwoWidth && !isRightFirst) {  //第一张图片在前，第二张图片在后
                    imageOneLeftLocation = screenWidth - imageOneWidth;
                    imageTwoLeftLocation = imageOneLeftLocation + (-imageTwoWidth);
                    allStep = 0;
                    isRightFirst = true;
                }
                invalidate();
            }
        } else if (direction == 0) {    //左滑
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, imageOneLeftLocation, top, paint);
                canvas.drawBitmap(bitmaps, imageTwoLeftLocation, top, paint);

                imageOneLeftLocation -= step;
                imageTwoLeftLocation -= step;
                allStep += step;

                if (allStep == imageOneWidth && isRightFirst) {
                    imageTwoLeftLocation = screenWidth - imageTwoWidth;
                    imageOneLeftLocation = imageTwoLeftLocation + imageOneWidth;
                    allStep = 0;
                    isRightFirst = false;
                } else if (allStep == imageTwoWidth && !isRightFirst) {
                    imageOneLeftLocation = screenWidth - imageOneWidth;
                    imageTwoLeftLocation = imageOneLeftLocation + imageTwoWidth;
                    allStep = 0;
                    isRightFirst = true;
                }
                invalidate();
            }
        } else {    //不滑动
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, imageOneLeftLocation, top, paint);
            }
        }
    }


    public void startScroll(int direction, int step, int resIdFirst, int resIdSecond) {
        this.direction = direction;
        this.step = step;
        if (screenWidth % step != 0) {
            throw new RuntimeException("step是两张图公约数");
        }
        initBitmap(resIdFirst, resIdSecond);
        initLeftPoint(direction);
        invalidate();
    }

    private void initLeftPoint(int direction) {
        if (direction == 1) {
            imageOneLeftLocation = screenWidth - imageOneWidth;
            imageTwoLeftLocation = screenWidth - imageOneWidth - imageTwoWidth;
        } else if (direction == 0) {
            imageOneLeftLocation = screenWidth - imageOneWidth;
            imageTwoLeftLocation = screenWidth - imageOneWidth + imageTwoWidth;
        } else {
            imageOneLeftLocation = 0;
        }
    }

    private void initBitmap(int resIdFirst,int resIdSecond) {
        try {
            bitmap = BitmapFactory.decodeResource(getContext().getResources(), resIdFirst);
            bitmaps = BitmapFactory.decodeResource(getContext().getResources(), resIdSecond);
            if(bitmap != null){
                imageOneWidth = bitmap.getWidth();
            }
            if(bitmaps != null){
                imageTwoWidth = bitmaps.getWidth();
            }
        } catch (OutOfMemoryError e) { // 有可能内存溢出,如果内存溢出，清理内存(图片框架中)，再次初始化
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        if (bitmaps != null && !bitmaps.isRecycled()) {
            bitmaps.recycle();
        }
        System.gc();
    }
}
