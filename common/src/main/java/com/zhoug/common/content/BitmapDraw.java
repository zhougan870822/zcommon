package com.zhoug.common.content;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.graphics.ColorUtils;
import android.util.Log;

import java.util.List;

/**
 * Bitmap 上面写文字
 */
public class BitmapDraw {
    private static final String TAG = "BitmapDraw";
    private Bitmap bitmap;
    /**
     * 文字颜色
     */
    private int textColor=Color.BLACK;
    /**
     * 文字大小
     */
    private int textSize=30;
    /**
     * 文字集合
     */
    private List<String> texts;
    /**
     * 是否根据图片颜色自动改变文字颜色
     */
    private boolean autoTextColor=false;

    /**
     * 内边距
     */
    private int paddingTop=50;
    private int paddingLeft=50;
    private int paddingRight=50;
    private int paddingBottom=50;
    /**
     * 行高
     */
    private int lineSpacing=5;
    /**
     * 位置
     */
    private int gravity=TOP;

    public static final int TOP=1;
    public static final int BOTTOM=2;

    private Paint mPaint;
    private Canvas mCanvas;

    /**
     * 初始化画笔画布
     */
    private void init(){
        if(mPaint==null){
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
        }
        if(mCanvas==null){
            mCanvas=new Canvas();
        }

    }

    public void draw(){
        if(texts==null) return;
        init();
        mCanvas.setBitmap(bitmap);
        mPaint.setTextSize(textSize);
        mPaint.setColor(textColor);
        int lines = getLines();
        Log.d(TAG, "lines="+lines);
        //文字总高度
        int textTotalHeight=lines*textSize+(lines-1)*lineSpacing;
        //开始位置
        int statX=paddingLeft;
        int statY=paddingTop;
        if(gravity==TOP){
            statY=paddingTop;
        }else if(gravity==BOTTOM){
            statY=bitmap.getHeight()-paddingBottom-textTotalHeight;
        }

        int x=statX;
        int y=statY;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        for(String text:texts){
            String[] split = text.split("");
            for(String s:split){
                //单个文字宽度
                float w = mPaint.measureText(s);
                //换行
                if(x+w>bitmap.getWidth()-paddingRight){
                    x=statX;
                    y+=textSize+lineSpacing;
                }
                //文字的基准线
                float baseLine=(y+y+textSize-fontMetrics.ascent-fontMetrics.descent)/2;
                //自动改变文字颜色
                if(autoTextColor){
                    //获取像素的颜色
                    int color = bitmap.getPixel(x, (int) baseLine);
                    //亮色
                    if(ColorUtils.calculateLuminance(color)>=0.5){
                        mPaint.setColor(Color.BLACK);
                    }else{
                        mPaint.setColor(Color.WHITE);
                    }
                }
                mCanvas.drawText(s,x , baseLine, mPaint);
                //横坐标右移
                x+=w;

            }
            //换行
            x=statX;
            y+=textSize+lineSpacing;

        }


    }

    /**
     * 计算总共的行数
     * @return
     */
    private int getLines(){
        int lines=0;
        mPaint.setTextSize(textSize);
        int contentW=bitmap.getWidth()-paddingLeft-paddingRight;
        for(String text:texts){
            float textWidth = mPaint.measureText(text);
            lines+= Math.ceil(textWidth / contentW);
        }
        return lines;
    }


    public BitmapDraw setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public BitmapDraw setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public BitmapDraw setTextSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public BitmapDraw setTexts(List<String> texts) {
        this.texts = texts;
        return this;
    }

    public BitmapDraw setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        return this;
    }

    /**
     * {@link #TOP,#BOTTOM}
     * @param gravity
     */
    public BitmapDraw setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public BitmapDraw setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        return this;
    }

    public BitmapDraw setAutoTextColor(boolean autoTextColor) {
        this.autoTextColor = autoTextColor;
        return this;
    }

    public boolean isAutoTextColor() {
        return autoTextColor;
    }





}
