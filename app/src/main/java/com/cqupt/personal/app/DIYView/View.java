package com.cqupt.personal.app.DIYView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cqupt.personal.app.R;

/**
 * 个人中心界面菜单栏分割线的绘制
 */

public class View extends android.view.View {

    private Paint paint;

    public View(Context context) {
        this(context,null);
    }

    public View(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.blueSky));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取屏幕的宽和高
        float width = getWidth();
        float height = getHeight();
        //每个半圆的半径(五十个半圆)
        float r = width / 50;
        //圆心坐标
        float cx = r;
        float cy = height * 2 / 5;
        while (cx <= width){
            @SuppressLint("DrawAllocation") RectF oval = new RectF(cx - r,cy - r,cx + r,cy + r);
            canvas.drawArc(oval,0,180,false,paint);
            cx = cx + r * 2;
        }
    }
}
