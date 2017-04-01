package com.example.juqiang_pc.tfsassistant.View;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/4/1.
 */
public class ContentRow extends AbstractRow {

    private List<Object> objects;   //表格填充内容
    private int resId;
    private int backcolor = Color.WHITE; // 默认背景为白色

    private boolean isBackground = false;

    private int textMaxlength = 10;// 如果有文本信息，则文本信息的长度
    private int textColor = Color.BLACK;
    private int textSize = 18; // 文字大小 如果是文本的信息

    public ContentRow(Context context, List<Object> objects) {
        super(context);
        // TODO Auto-generated constructor stub
        this.objects = objects;
    }

    /*
     * 设置背景的颜色
     */
    public void setBackground(int resId) {

        this.resId = resId;
        isBackground = true;

    }

    public void setBackColor(int color) {
        this.backcolor = color;
        isBackground = false;
    }

    public void setTextMaxEms(int size) {
        this.textMaxlength = size;
    }

    public void setTextSize(int size) {
        this.textSize = size;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public TableRow addTableRow() {
        // TODO Auto-generated method stub

        TableRow tableRow = new TableRow(context);
        tableRow.addView(addSplitLine());
        tableRow.setLayoutParams(params);

        for (int i = 0; i < objects.size();i++) {
            TextView textView = new TextView(context);
            textView.setClickable(true);
            textView.setGravity(Gravity.CENTER | Gravity.CENTER);

            if (isBackground) {
                textView.setBackgroundResource(resId);
            } else {
                textView.setBackgroundColor(backcolor);
            }

            TableRow.LayoutParams params = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);

            textView.setSingleLine(false);
            textView.setMaxEms(textMaxlength);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);  //float型

            textView.setText(String.valueOf(objects.get(i)));
            tableRow.addView(textView);

            // 添加分割线
            tableRow.addView(addSplitLine());

        }

        return tableRow;

    }


}
