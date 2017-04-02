package com.example.juqiang_pc.tfsassistant.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import com.example.juqiang_pc.tfsassistant.API.Utils;
import com.example.juqiang_pc.tfsassistant.Entity.Widget;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class WidgetView extends View {
    private Paint penName, penThreshold,penResult;
    private Widget widget;
    private Bitmap headBackgroundBitmap;
    private int width, height, index;
    private StaticLayout nameLayout;

    public WidgetView(Context context, int index, Widget widget) {
        super(context);

        this.index = index;
        this.widget = widget;

        penResult = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        penResult.setColor(Color.WHITE);
        penResult.setStrokeJoin(Paint.Join.ROUND);
        penResult.setStrokeCap(Paint.Cap.ROUND);
        penResult.setStrokeWidth(3);
        penResult.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 64, getResources().getDisplayMetrics()));
        penResult.setTextAlign(Paint.Align.CENTER);

        penThreshold = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        penThreshold.setColor(Color.WHITE);
        penThreshold.setStrokeJoin(Paint.Join.ROUND);
        penThreshold.setStrokeCap(Paint.Cap.ROUND);
        penThreshold.setStrokeWidth(3);
        penThreshold.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 64, getResources().getDisplayMetrics()));
        penThreshold.setTextAlign(Paint.Align.CENTER);

        height = width = (Utils.displayWidth-200)/2;

        TextPaint textPaint = new TextPaint();

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(48.0F);
        nameLayout = new StaticLayout(this.widget.name,textPaint,this.width-24, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);

        /*headBackgroundBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.auriel);
        width = headBackgroundBitmap.getWidth();
        height = headBackgroundBitmap.getHeight();*/
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width+48, height+48);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*canvas.drawBitmap(headBackgroundBitmap,
                new Rect(0,0,width,height),
                new Rect(0,0,width/4,height/4),
                null);*/

        boolean isOverThreshold = ((this.widget.isColorRuleEnabled) && ((this.widget.colorRuleOperator.equals(">") && this.widget.resultCount > this.widget.colorRuleThresholdCount)
                || (this.widget.colorRuleOperator.equals(">=") && this.widget.resultCount >= this.widget.colorRuleThresholdCount)
                || (this.widget.colorRuleOperator.equals("<") && this.widget.resultCount < this.widget.colorRuleThresholdCount)
                || (this.widget.colorRuleOperator.equals("<=") && this.widget.resultCount <= this.widget.colorRuleThresholdCount)
        ));

        canvas.clipRect(0,0,width,height, Region.Op.REPLACE);
        Rect nameRect = new Rect(0,0,width-32,height);

        Paint.FontMetricsInt fontMetrics = penThreshold.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (nameRect.bottom + nameRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()

//从 (20,80)的位置开始绘制


        if(!isOverThreshold)
        {
            canvas.drawColor(0xff009ccc);
            canvas.translate(16,16);
            nameLayout.draw(canvas);
            //canvas.drawText(this.widget.name, 16, 64, penName);
            canvas.drawText(String.valueOf(widget.resultCount), nameRect.centerX(), baseline, penThreshold);
        } else {
            canvas.drawColor(0xffe51400);
            canvas.translate(16,16);
            nameLayout.draw(canvas);
            //canvas.drawText(this.widget.name, 16, 64, penName);
            canvas.drawText(String.valueOf(widget.resultCount), nameRect.centerX(), baseline, penResult);
        }
    }

    public String getWidgetUrl() {
        return this.widget.id;
    }

    public String getQueryId(){
        return this.widget.detailId;
    }

    public String getWidgetName(){
        return this.widget.name;
    }
}
