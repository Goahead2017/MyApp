package com.cqupt.personal.app.DIYView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.cqupt.personal.app.R;

/**
 * 个人中心界面顶部的绘制
 */

public class PersonalView extends View {

    private Paint paint;

    public PersonalView(Context context) {
        this(context,null);
    }

    public PersonalView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PersonalView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.blueSky));
        paint.setStyle(Paint.Style.FILL);
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
        //每个半圆的半径(十个半圆)
        float r = width / 20;
        //圆心坐标
        float cx = r;
        float cy = height * 2 / 5;
        while (cx <= width){
            @SuppressLint("DrawAllocation") RectF oval = new RectF(cx - r,cy - r,cx + r,cy + r);
            canvas.drawArc(oval,0,180,false,paint);
            cx = cx + r * 2;
        }
        canvas.drawRect(0,0,width,cy,paint);
    }
}
