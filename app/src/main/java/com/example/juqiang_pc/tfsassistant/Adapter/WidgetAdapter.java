package com.example.juqiang_pc.tfsassistant.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.juqiang_pc.tfsassistant.Entity.DashBoard;
import com.example.juqiang_pc.tfsassistant.Entity.Widget;
import com.example.juqiang_pc.tfsassistant.View.DashboardView;
import com.example.juqiang_pc.tfsassistant.View.WidgetView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class WidgetAdapter  extends BaseAdapter {
    private Context context;
    private List<Widget> widgetList;
    private Map<Integer, WidgetView> viewCache = new HashMap<Integer, WidgetView>();

    public WidgetAdapter(Context context, List<Widget> widgetList) {
        this.context = context;
        this.widgetList = widgetList;

        for (int i = 0; i < widgetList.size();i++) {
            WidgetView dv = new WidgetView(this.context,i,widgetList.get(i));

            viewCache.put(i, dv);
        }
    }

    @Override
    public int getCount() {
        return widgetList.size();
    }

    @Override
    public Object getItem(int index) {
        return widgetList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return (WidgetView) (viewCache.get(position));
    }
}
