package com.example.juqiang_pc.tfsassistant.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.juqiang_pc.tfsassistant.Entity.DashBoard;
import com.example.juqiang_pc.tfsassistant.View.DashboardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class DashboardAdapter extends BaseAdapter {
    private Context context;
    private List<DashBoard> dashBoardList;
    private Map<Integer, DashboardView> viewCache = new HashMap<Integer, DashboardView>();
    public DashboardAdapter(Context context, List<DashBoard> dashBoardList) {
        this.context = context;
        this.dashBoardList = dashBoardList;

        for (int i = 0; i < dashBoardList.size();i++) {
            DashboardView dv = new DashboardView(this.context,i,dashBoardList.get(i));

            viewCache.put(i, dv);
        }
    }

    @Override
    public int getCount() {
        return dashBoardList.size();
    }

    @Override
    public Object getItem(int index) {
        return dashBoardList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {
        return (DashboardView) (viewCache.get(index));
    }
}
