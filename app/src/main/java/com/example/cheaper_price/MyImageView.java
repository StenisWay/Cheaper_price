package com.example.cheaper_price;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class MyImageView extends AppCompatImageView{

    private int id;
    private Paint mPaint;
    private int resId;

    public MyImageView(@NonNull Context context) {
        super(context);
    }

    public MyImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        mPaint = new Paint();
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        //加载图片
        BitmapFactory.decodeResource(res,resId,options);
        //计算缩放比
        options.inSampleSize = caculateInSampleSize(options,reqHeight,reqWidth);
        //重新加载图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    public void setImageResource(int resId) {
        this.resId = resId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //必要步骤，避免由于初始化之前导致的异常错误
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap b = decodeSampledBitmapFromResource(getResources(), resId, 90, 90);

        if (null == b) {
            return;
        }

        Bitmap resizeBitmap = b.copy(Bitmap.Config.ARGB_8888, true);

        canvas.drawBitmap(resizeBitmap, 0, 0, mPaint);

    }

}
