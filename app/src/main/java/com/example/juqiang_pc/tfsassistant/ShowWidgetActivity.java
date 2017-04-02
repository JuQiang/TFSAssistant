package com.example.juqiang_pc.tfsassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.juqiang_pc.tfsassistant.API.HttpTask;
import com.example.juqiang_pc.tfsassistant.API.TaskCompleted;
import com.example.juqiang_pc.tfsassistant.View.MyTableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ShowWidgetActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_widget);

        Intent intent = this.getIntent();
        String widgetName = intent.getStringExtra("widgetName");
        String queryResult = intent.getStringExtra("queryResult");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShowWidget);
        toolbar.setTitle(widgetName);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        List<String> urllist = new ArrayList<String>();
        final List<String> columns = new ArrayList<String>();
        final List<String> columnNames = new ArrayList<String>();

        final Context context = this;

        try {
            JSONObject jo = new JSONObject(queryResult);
            JSONArray jsonColumns = jo.getJSONArray("columns");
            final StringBuilder sbColumnTypes = new StringBuilder();

            for (int i = 0; i < jsonColumns.length(); i++) {
                JSONObject jsonCol = (new JSONObject(jsonColumns.opt(i).toString()));
                columns.add(jsonCol.getString("referenceName"));
                columnNames.add(jsonCol.getString("name"));
                sbColumnTypes.append(jsonCol.getString("referenceName")).append(",");
            }
            if (sbColumnTypes.length() > 0) {
                sbColumnTypes.deleteCharAt(sbColumnTypes.length() - 1);
            }

            JSONArray jsonWorkitems = jo.getJSONArray("workItems");
            StringBuilder sbWorkitems = new StringBuilder();

            for (int i = 0; i < jsonWorkitems.length(); i++) {
                sbWorkitems.append((new JSONObject(jsonWorkitems.opt(i).toString())).getString("id")).append(",");
            }
            if (sbWorkitems.length() > 0) {
                sbWorkitems.deleteCharAt(sbWorkitems.length() - 1);
            }

            String url = "http://tfs.teld.cn:8080/tfs/teld/_apis/wit/workitems?ids=" +
                    sbWorkitems.toString() +
                    "&fields=" +
                    sbColumnTypes.toString();

            final ProgressDialog proDialog = android.app.ProgressDialog.show(ShowWidgetActivity.this, "提示", "正在获取workitems数据……");
            final MyTableView mtv = (MyTableView) findViewById(R.id.mtvWorkItems);
            mtv.tableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ////workitem
                    String url = "http://tfs.teld.cn:8080/tfs/Teld/_apis/wit/workitems/57235?api-version=1.0";

                }
            });

            HttpTask ht = new HttpTask(new TaskCompleted() {
                @Override
                public void OnTaskCompleted(Object result) {
                    try {
                        JSONArray jsonWorkitems = (new JSONObject(result.toString())).getJSONArray("value");
                        MyTableView mtv = (MyTableView) findViewById(R.id.mtvWorkItems);
                        mtv.setTableHeaders(columnNames);


                        for (int i = 0; i < jsonWorkitems.length(); i++) {
                            List<Object> values = new ArrayList<Object>();
                            JSONObject jsonWorkitem = jsonWorkitems.getJSONObject(i);
                            JSONObject fields = jsonWorkitem.getJSONObject("fields");

                            StringBuilder sb = new StringBuilder();

                            for (int j = 0; j < columns.size(); j++) {
                                String fieldName = columns.get(j);
                                if (fields.has(fieldName)) {
                                    String fullname = fields.getString(fieldName);
                                    //"System.AssignedTo" -> "王亮 <TELD\wangliang>"
                                    //"Teld.Scrum.Barrier.Confirmer" -> "朱诗严 <TELD\zhusy>"
                                    if (fieldName.equals("System.AssignedTo") ||
                                            fieldName.equals("Teld.Scrum.Barrier.Confirmer") ||
                                            fieldName.equals("System.CreatedBy")||
                                            fieldName.equals("Teld.Scrum.AccidentCharger")

                                            ) {
                                        values.add(getPartName(fullname, "<"));
                                    } else if (fieldName.equals("System.CreatedDate") ||
                                            fieldName.equals("Microsoft.VSTS.Scheduling.DueDate") ||
                                            fieldName.equals("Teld.Scrum.PlanSubmitDate")||
                                            fieldName.equals("Teld.Release.Date")||
                                            fieldName.equals("Teld.Publish.Begintime2")||
                                            fieldName.equals("Teld.Scrum.PlanSubmitDate")||
                                            fieldName.equals("Teld.Scrum.StartTime")||
                                            fieldName.equals("Teld.Scrum.SolvedTime")||
                                            fieldName.equals("Teld.Scrum.AffectTime")
                                            ) {
                                        values.add(getPartName(fullname, "T"));
                                    } else {
                                        values.add(fields.getString(fieldName));
                                    }
                                } else {
                                    values.add(" ");
                                }
                            }
                            mtv.addNewRow(values);
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    proDialog.dismiss();
                }
            });
            ht.execute(url);
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getPartName(String fullname, String token) {
        int pos = fullname.indexOf(token);
        if (pos < 0) return fullname;

        return fullname.substring(0, pos);
    }
}
