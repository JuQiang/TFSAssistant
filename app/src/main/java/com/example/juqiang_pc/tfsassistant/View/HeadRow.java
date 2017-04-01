package com.example.juqiang_pc.tfsassistant.View;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/4/1.
 */

public class HeadRow extends AbstractRow {


    private List<String> header;
    private int textColor = Color.BLACK;
    private int textLenghtMax = 10;          //定于每一行最多显示几个字
    private int cellBackgroundColor = Color.WHITE;

    public HeadRow(Context context, List<String> header) {
        super(context);

        this.context = context;
        this.header = header;
    }
    /*
     * 设置字体的颜色
     */
    public void setTextClor(int color){
        this.textColor = color;
    }
    /*
     * 设置每一行的的字符数
     */
    public void setTextMaxEms(int lenght){
        this.textLenghtMax = lenght;
    }
    /*
     * 设置表格的背景颜色 默认为白色
     */
    public  void setBackgroundColor(int color){
        this.cellBackgroundColor = color;
    }
    @Override
    public TableRow addTableRow(){

        TableRow tableRow = new TableRow(context);
        tableRow.addView(addSplitLine());           //添加分隔符号

        tableRow.setLayoutParams(params);
        for(int i = 0;i<header.size();i++){

            TextView textView = new TextView(context);
            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.MATCH_PARENT);
            textView.setLayoutParams(params);

            textView.setGravity(Gravity.CENTER|Gravity.CENTER);  //文本居中显示
            textView.setTextColor(textColor); //设置文本颜色
            textView.setSingleLine(false);
            textView.setMaxEms(textLenghtMax);
            textView.setBackgroundColor(cellBackgroundColor); //设施背景颜色
            textView.setText(header.get(i));
            tableRow.addView(textView);
            tableRow.addView(addSplitLine());

        }
        return tableRow;
    }




}
