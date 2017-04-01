package com.example.juqiang_pc.tfsassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.juqiang_pc.tfsassistant.API.Utils;
import com.example.juqiang_pc.tfsassistant.API.HttpTask;
import com.example.juqiang_pc.tfsassistant.API.TaskCompleted;
import com.example.juqiang_pc.tfsassistant.Adapter.DashboardAdapter;
import com.example.juqiang_pc.tfsassistant.Entity.Authentication;
import com.example.juqiang_pc.tfsassistant.Entity.DashBoard;
import com.example.juqiang_pc.tfsassistant.Entity.EntityResolver;
import com.example.juqiang_pc.tfsassistant.Entity.User;
import com.example.juqiang_pc.tfsassistant.View.DashboardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Intent intentShowDashboard;
    private Intent intentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.setContext(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Utils.displayWidth = displayMetrics.widthPixels;
        Utils.displayHeight = displayMetrics.heightPixels;

        Authentication auth = Utils.getAuthentication();
        if(auth.user.equals("")) {
            intentLogin = new Intent(this, LoginActivity.class);
            startActivity(intentLogin);
        }

        fetchUserList();
        fetchDashboardList();
    }

    private void fetchDashboardList() {
        final ProgressDialog proDialog = android.app.ProgressDialog.show(MainActivity.this, "提示", "正在获取dashboard列表……");
        intentShowDashboard = new Intent(this, ShowDashboardActivity.class);
        final Context context = this;

        HttpTask ht = new HttpTask(new TaskCompleted() {
            @Override
            public void OnTaskCompleted(Object result) {
                String ret = String.valueOf(result);
                List<DashBoard> dblist = EntityResolver.resolveDashBoardList(ret);
                int size = dblist.size();

                GridView gridView = (GridView) findViewById(R.id.gvDashboardList);
                gridView.setAdapter(new DashboardAdapter(context, dblist));
                proDialog.dismiss();

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (view == null) return;

                        final String url = ((DashboardView) view).getDashboardUrl();
                        final String name = ((DashboardView) view).getDashboardName();
                        final String did = ((DashboardView) view).getDashboardId();
                        HttpTask ht = new HttpTask(new TaskCompleted() {
                            @Override
                            public void OnTaskCompleted(Object result) {
                                intentShowDashboard.putExtra("dashboardName", name);
                                intentShowDashboard.putExtra("dashboardId", did);
                                intentShowDashboard.putExtra("dashboardData", String.valueOf(result));
                                startActivity(intentShowDashboard);
                            }
                        });
                        ht.execute(url);

                    }
                });
            }
        });
        ht.execute(Utils.orgPortalUrl);
    }

    private void fetchUserList() {
        final ProgressDialog proDialog = android.app.ProgressDialog.show(MainActivity.this, "提示", "正在获取用户列表……");
        HttpTask ht = new HttpTask(new TaskCompleted() {
            @Override
            public void OnTaskCompleted(Object result) {
                try {
                    JSONArray ja = (new JSONObject(result.toString())).getJSONArray("value");
                    Utils.userList = new ArrayList<User>();
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.optJSONObject(i);
                        User user = new User();
                        user.id= jo.getString("id");
                        user.displayName = jo.getString("displayName");
                        user.uniqueName = jo.getString("uniqueName");
                        user.url = jo.getString("url");
                        user.imageUrl = jo.getString("imageUrl");

                        Utils.userList.add(user);
                    }
                    proDialog.dismiss();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        ht.execute("http://tfs.teld.cn:8080/tfs/teld/_apis/projects/c955f4f8-3b05-4afc-9969-3a54f7b70533/teams/ad0bf755-9688-4d6a-95f9-4e20702a2972/members?api-version=2.0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
