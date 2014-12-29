package com.gtrj.docdeal.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.ImageView;

/**
 * Created by zhang77555 on 2014/12/18.
 */
public class myImageView extends ImageView {
    private String namespace = "http://shadow.com";
    private int color;

    public myImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        color = Color.GRAY;
    }


    /* (non-Javadoc)
    * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
    */


    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub


        super.onDraw(canvas);
        //画边框
        Rect rec = canvas.getClipBounds();
        rec.bottom--;
        rec.right--;
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rec, paint);
    }
}

