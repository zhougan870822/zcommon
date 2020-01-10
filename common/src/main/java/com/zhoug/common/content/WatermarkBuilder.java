package com.zhoug.common.content;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;

/**
 * 水印
 *
 * @Author HK-LJJ
 * @Date 2019/12/12
 * @Description
 */
public class WatermarkBuilder {
    private static final String TAG = "WatermarkBuilder";
    private int textSize = 30;//px
    private int textColor = Color.parseColor("#555555");
    private Bitmap bitmap;
    private String content;
    private int rowSpacing = 0;
    private int columnSpacing = 0;
    private final float degrees = -45;
    private int alpha = 255;//透明度

    private static final float MARGING_START = 20;


    private Paint getPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        paint.setAlpha(alpha);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    public Bitmap test() {
        Bitmap bitmap = Bitmap.createBitmap(720, 1080, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.parseColor("#bbbbbb"));
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(100, 100, 300, 400);
        canvas.drawRect(rectF, paint);

        paint.setColor(Color.RED);
        canvas.rotate(90);
        canvas.translate(-100, -100);
        canvas.drawRect(rectF, paint);


        return bitmap;
    }

    /**
     * @return Watermark
     */
    public Bitmap builder1() {
        if (TextUtils.isEmpty(content)) {
            return bitmap;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int width = (int) Math.sqrt(w * w + h * h);
        int height = width;
        //水印图片
        Bitmap watermark = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Paint paint = getPaint();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textLength = paint.measureText(content);
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        float startY = rowSpacing - fontMetrics.descent;
        Canvas canvas = new Canvas(watermark);
        canvas.drawColor(Color.parseColor("#cccccc"));
        float startX = 0;
        while (startY < height) {
            //平铺
            for (startX = 0; startX < width; startX += textLength + columnSpacing) {
                canvas.drawText(content, startX, startY, paint);
            }
            startY += textHeight + rowSpacing;
        }

        float tanX = 0;
        float tanY = 0;
        Log.d(TAG, "builder1:tanX=" + tanX);
        Log.d(TAG, "builder1:tanY=" + tanY);
        canvas = new Canvas(bitmap);
        canvas.rotate(degrees, tanX, tanY);
        canvas.drawBitmap(watermark, 0, 0, paint);


        return bitmap;
    }

    /**
     * @return
     */
    public Bitmap builder() {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float cos = (float) Math.cos(Math.toRadians(Math.abs(degrees)));
        float maxLength = height / cos;
        float splitHeight = height * cos;
        float startX = 0;
        float startY = rowSpacing;
//        Bitmap cacheBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = getPaint();
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        startY -= fontMetrics.descent;
        //文字高度
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        Log.d(TAG, "builder:textHeight=" + textHeight);
        float textWidth = paint.measureText(content);

        while (startY < maxLength) {
            canvas.save();
            startY += rowSpacing + textHeight;
            double tan = Math.tan(Math.toRadians(Math.abs(degrees)));
            Log.d(TAG, "builder:tan=" + tan);
//            if(startY<=splitHeight){
            startX = (float) (-tan * startY) + MARGING_START;
//            }else{
//                startX=0;
//            }
            canvas.rotate(degrees, 0, 0);
            canvas.translate(startX, startY);

            //列循环，从每行的开始位置开始，向右每隔2倍宽度的距离开始绘制（文字间距1倍宽度）
            for (float positionX = 0; positionX < width; positionX += textWidth + columnSpacing) {
                //绘制文字
                canvas.drawText(content, positionX, 0, paint);
            }

            canvas.restore();
        }


        return bitmap;
    }

    public WatermarkBuilder setTextSize(int textSize) {
        this.textSize = textSize;

        return this;
    }

    public WatermarkBuilder setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    public WatermarkBuilder setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public WatermarkBuilder setRowSpacing(int rowSpacing) {
        this.rowSpacing = rowSpacing;
        return this;
    }

    public WatermarkBuilder setContent(String content) {
        this.content = content;
        return this;
    }


}
