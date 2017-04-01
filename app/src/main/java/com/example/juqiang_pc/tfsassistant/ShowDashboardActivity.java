package com.example.juqiang_pc.tfsassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.juqiang_pc.tfsassistant.API.HttpBatchTask;
import com.example.juqiang_pc.tfsassistant.API.HttpTask;
import com.example.juqiang_pc.tfsassistant.API.TaskCompleted;
import com.example.juqiang_pc.tfsassistant.Adapter.WidgetAdapter;
import com.example.juqiang_pc.tfsassistant.Entity.EntityResolver;
import com.example.juqiang_pc.tfsassistant.Entity.Widget;
import com.example.juqiang_pc.tfsassistant.View.WidgetView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDashboardActivity extends AppCompatActivity {
    GridView gvDashBoard;
    WidgetAdapter widgetAdapter;
    private Intent intentShowWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dashboard);

        Intent intent = this.getIntent();
        String dashboardData = intent.getStringExtra("dashboardData");
        String dashboardName = intent.getStringExtra("dashboardName");
        String dashboardId = intent.getStringExtra("dashboardId");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShowDashboard);
        toolbar.setTitle(dashboardName);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        final ProgressDialog proDialog = android.app.ProgressDialog.show(ShowDashboardActivity.this, "提示", "正在获取dashboard数据。由于REST API不支持批量获取widgets，所以会慢，请稍候……");
        List<Widget> widgetList = EntityResolver.resolveWidgetList(dashboardData);
        final List<Widget> newWidgetList = new ArrayList<Widget>();
        StringBuilder urlList = new StringBuilder();

        for (int i = 0; i < widgetList.size(); i++) {
            Widget widget = widgetList.get(i);
            //if (widget.type != "query" || widget.type != "chart") continue;
            if (widget.type != "query") continue;

            newWidgetList.add(widget);
            urlList.append(widget.detailId).append(";");
        }

        final Context context = this;
        intentShowWidget = new Intent(this, ShowWidgetActivity.class);

        /*HttpTask ht = new HttpTask(new TaskCompleted() {
            @Override
            public void OnTaskCompleted(Object result) {
String ret = result.toString();
                proDialog.dismiss();
            }
        });
        ht.execute(Utils.prefixURL+Utils.projectID+"/"+Utils.teamID+"/_api/dashboard/dashboards/"+dashboardId+"/widgets/?api-version=3.0-preview.2");
        */
        HttpBatchTask hbt = new HttpBatchTask(new TaskCompleted() {
            @Override
            public void OnTaskCompleted(Object result) {
                List<String> ret = (List<String>) result;
                for(int i=0;i<newWidgetList.size();i++){
                    try {
                        newWidgetList.get(i).resultCount = (new JSONObject(ret.get(i))).getInt("Count");
                    }
                    catch(JSONException ex){
                        ex.printStackTrace();
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }

                gvDashBoard= (GridView)findViewById(R.id.gvDashBoard);

                widgetAdapter = new WidgetAdapter(context,newWidgetList);
                gvDashBoard.setAdapter(widgetAdapter);
                proDialog.dismiss();

                gvDashBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (view == null) return;
                        final ProgressDialog proDialog2 = android.app.ProgressDialog.show(ShowDashboardActivity.this, "提示", "正在获取widget数据……");

                        final String queryid = ((WidgetView)view).getQueryId();
                        final String widgetName = ((WidgetView)view).getWidgetName();
                        int a = 0;
                        HttpTask ht = new HttpTask(new TaskCompleted() {
                            @Override
                            public void OnTaskCompleted(Object result) {
                                intentShowWidget.putExtra("widgetName", widgetName);
                                intentShowWidget.putExtra("queryResult", String.valueOf(result));
                                startActivity(intentShowWidget);
                                proDialog2.dismiss();//万万不可少这句，否则会程序会卡死。
                            }
                        });
                        //36760898
                        ht.execute("http://tfs.teld.cn:8080/tfs/Teld/c955f4f8-3b05-4afc-9969-3a54f7b70533/_apis/wit/wiql/"+queryid);
                    }
                });
                int size = newWidgetList.size();
                //newWidgetList.add(widget);
                //intentShowDashboard.putExtra("dashboardData",String.valueOf(result));
                //startActivity(intentShowDashboard);
            }
        });
        hbt.execute(urlList.toString());
    }
}

