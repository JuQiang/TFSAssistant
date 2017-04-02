package com.example.juqiang_pc.tfsassistant.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/4/1.
 */

public class MyTableView extends ScrollView {


    public TableLayout tableLayout; //表格布局
    private SplitLineRow splitLineRow;  //水平分割线

    //各种参数
    private int headColor = Color.WHITE;
    private int headHeight = TableRow.LayoutParams.WRAP_CONTENT;
    private int headtextColor = Color.BLACK;
    private int hedaMaxEms = 10;

    private int divisonColor = Color.RED;
    private int divisonWidth = 1;


    private int resId = 0;
    private int backgroudColor = Color.WHITE;
    private int cellMaxEms = 10;;
    private int cellTextColor = Color.BLACK;
    private int cellTextSize = 15;
    private int cellHeight = TableRow.LayoutParams.WRAP_CONTENT;

    public MyTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initWidge();
    }

	/*
	 * 公共接口
	 */

    /*
     * 设置表格的标题头
     */
    public void setTableHeaders(List<String> header){

        //添加分割线
        splitLineRow = new SplitLineRow(getContext());
        tableLayout.addView(splitLineRow.addTableRow());


        HeadRow  row = new HeadRow(getContext(), header);

        row.setDivisonColor(divisonColor);
        row.setDivisonWidth(divisonWidth);
        row.setBackgroundColor(headColor);
        row.setTabRowHeight(headHeight);
        row.setTextMaxEms(hedaMaxEms);
        row.setTextClor(headtextColor);

        tableLayout.addView(row.addTableRow());

        tableLayout.addView(splitLineRow.addTableRow()); //添加分割线

    }
    /*
     * 设置表格头的背景颜色
     */
    public void setTableheadColor(int color){
        this.headColor =color;
    }
    /*
     * 设置表头的的高度
     */
    public void setTableHeadHeigt(int height){
        this.headHeight = height;
    }
    /*
     * 设置表头的的字体颜色
     */
    public void setTableHeadTextcolor(int color){
        this.headtextColor = color;
    }

    /*
     * 设置表头的的字体大小
     */
    public void setTableHeadMaxEms(int size){
        this.hedaMaxEms =size;
    }

    public void setTableDivisonColor(int color){
        this.divisonColor = color;
    }
    public void setTableDivisonWidth(int width){
        this.divisonWidth = width;
    }
    /*******************内容     **************/
	/*
	 * 添加新的一行，目前的版本仅支持表格中由图像的下信息
	 * @@param objects
	 * 添加的内容
	 * @@param columns
	 * 表格的哪一行为图片数据
	 */

    public void  addNewRow(List<Object> objects){

        ContentRow addContentsRow = new ContentRow(getContext(), objects);

        if(resId != 0)
        {
            addContentsRow.setBackground(resId);
        }else
        {
            addContentsRow.setBackColor(backgroudColor);
        }
        addContentsRow.setTextColor(cellTextColor);
        addContentsRow.setTextMaxEms(cellMaxEms);
        addContentsRow.setTextSize(cellTextSize);
        addContentsRow.setTabRowHeight(cellHeight);


        tableLayout.addView(addContentsRow.addTableRow());
        tableLayout.addView(splitLineRow.addTableRow());


    }
    /*
     * 设置表格的背景
     * @resId 背景的Id
     */
    public void setTableCellBackground(int resId){
        this.resId = resId;
    }


    public void setTableCellBaackgroundColor(int color){
        this.backgroudColor = color;
    }
	/*
	 * 如果表格显示的是文字，设置文字每一行显示的字长
	 */

    public void setTableCellMaxEms(int length){
        this.cellMaxEms = length;
    }
    public void setTableCellTextSize(int size){
        this.cellTextSize = size;
    }
    public void setTableCellTextColor(int color){
        this.cellTextColor = color;
    }
    public void setTableCellHeight(int height){
        this.cellHeight = height;
    }
    /********************私有成员***************************/


	/*
	 * 界面的初始化
	 */
    private  void initWidge(){

        this.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        //水平的滚动条
        HorizontalScrollView horizontalScrollView = new HorizontalScrollView(getContext());
        horizontalScrollView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        horizontalScrollView.setFillViewport(true);
        tableLayout = new TableLayout(getContext());  //表格布局

        tableLayout.setLayoutParams(new HorizontalScrollView.LayoutParams(
                HorizontalScrollView.LayoutParams.MATCH_PARENT,HorizontalScrollView.LayoutParams.MATCH_PARENT));
        tableLayout.setStretchAllColumns(true);  //所有的列都可以被拉伸

        horizontalScrollView.addView(tableLayout);

        this.addView(horizontalScrollView);


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }

}
