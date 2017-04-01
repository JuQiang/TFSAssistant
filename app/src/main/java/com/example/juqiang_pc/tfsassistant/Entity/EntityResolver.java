package com.example.juqiang_pc.tfsassistant.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class EntityResolver {
    public static List<DashBoard> resolveDashBoardList(String input) {
        List<DashBoard> ret = new ArrayList<DashBoard>();

        try {
            JSONObject json = new JSONObject(input);
            JSONArray jsonArray = json.getJSONArray("dashboardEntries");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonDashBoard = (JSONObject) jsonArray.opt(i);

                DashBoard db = new DashBoard();
                db.id = jsonDashBoard.getString("id");
                db.url = jsonDashBoard.getString("url").replace("t-bj-tfs-01", "tfs.teld.cn");
                db.name = jsonDashBoard.getString("name");
                db.position = jsonDashBoard.getInt("position");
                db.refreshInterval = jsonDashBoard.getInt("refreshInterval");

                ret.add(db);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }

    public static List<Widget> resolveWidgetList(String input) {
        List<Widget> ret = new ArrayList<Widget>();

        try {
            JSONObject json = new JSONObject(input);
            JSONArray jsonArray = json.getJSONArray("widgets");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonWidget = (JSONObject) jsonArray.opt(i);

                Widget w = new Widget();
                w.id = jsonWidget.getString("id");
                w.name = jsonWidget.getString("name");
                w.url = jsonWidget.getString("url").replace("t-bj-tfs-01", "tfs.teld.cn");
                w.settings = jsonWidget.getString("settings");
                w.row = jsonWidget.getJSONObject("position").getInt("row");
                w.column = jsonWidget.getJSONObject("position").getInt("column");
                w.typeId = jsonWidget.getString("typeId");
                if(!w.settings.equals("null")) {
                    if (w.typeId.indexOf("QueryScalarWidget") > -1) {
                        w.type = "query";
                        w.detailId = (new JSONObject(w.settings)).getString("queryId");
                    } else if (w.typeId.indexOf("WitChartWidget") > -1) {
                        w.type = "chart";
                        w.detailId = (new JSONObject(w.settings)).getString("chartId");
                    }

                    if (w.settings.indexOf("colorRules") > -1) {
                        JSONArray jo = (new JSONObject(w.settings)).getJSONArray("colorRules");
                        for (int j = 0; j < jo.length(); j++) {
                            JSONObject obj = ((JSONObject) jo.opt(j));
                            w.isColorRuleEnabled = obj.getBoolean("isEnabled");

                            if (obj.getBoolean("isEnabled") == false) continue;

                            String color = obj.getString("backgroundColor");
                            int thresholdCount = obj.getInt("thresholdCount");
                            String operator = obj.getString("operator");

                            w.colorRuleBackgroundColor = color;
                            w.colorRuleOperator = operator;
                            w.colorRuleThresholdCount = thresholdCount;
                        }

                    }
                }
                ret.add(w);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
    }
}
