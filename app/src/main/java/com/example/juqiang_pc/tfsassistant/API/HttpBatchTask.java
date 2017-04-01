package com.example.juqiang_pc.tfsassistant.API;

import android.os.AsyncTask;

import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class HttpBatchTask  extends AsyncTask<String, Integer, List<String>> {
    private TaskCompleted listner;

    public HttpBatchTask(TaskCompleted listner) {
        this.listner = listner;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        List<String> ret = HttpHelper.getHttpResponseBatch(params[0]);

        return ret;
    }

    @Override
    protected void onPostExecute(List<String> httpResponse) {
        if (null != listner) {
            listner.OnTaskCompleted(httpResponse);
        }
    }
}
