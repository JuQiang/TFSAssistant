package com.example.juqiang_pc.tfsassistant.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.TypedValue;
import android.view.View;

import com.example.juqiang_pc.tfsassistant.API.Utils;
import com.example.juqiang_pc.tfsassistant.Entity.DashBoard;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class DashboardView  extends View {
    private Paint penName;
    private DashBoard dashBoard;
    private Bitmap headBackgroundBitmap;
    private int width, height,index;

    public DashboardView(Context context, int index, DashBoard dashBoard) {
        super(context);

        this.index=index;
        this.dashBoard = dashBoard;

        penName = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        penName.setColor(Color.BLACK);
        penName.setStrokeJoin(Paint.Join.ROUND);
        penName.setStrokeCap(Paint.Cap.ROUND);
        penName.setStrokeWidth(3);
        penName.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

        width= Utils.displayWidth-64;
        height=96;
        /*headBackgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.auriel);
        width = headBackgroundBitmap.getWidth();
        height = headBackgroundBitmap.getHeight();*/
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width+48,height+32);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipRect(0,0,width,height, Region.Op.REPLACE);

        if(index%2==0) {
            canvas.drawColor(0xfff0f0f0);
        }
        else{
            canvas.drawColor(0xffd0d0d0);
        }
        /*canvas.drawBitmap(headBackgroundBitmap,
                new Rect(0,0,width,height),
                new Rect(0,0,width/4,height/4),
                null);*/
        canvas.drawText(this.dashBoard.name, 8,64, penName);
    }
    public String getDashboardUrl(){
        return this.dashBoard.url;
    }
    public String getDashboardName(){return this.dashBoard.name;}
    public String getDashboardId(){return this.dashBoard.id;}
}
