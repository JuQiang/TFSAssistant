package com.example.juqiang_pc.tfsassistant.API;

import android.os.AsyncTask;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class HttpTask extends AsyncTask<String, Integer, String> {
    private TaskCompleted listner;

    public HttpTask(TaskCompleted listner) {
        this.listner = listner;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0].toLowerCase();
        String ret = HttpHelper.getHttpResponse(urlString);

        return ret;
    }

    @Override
    protected void onPostExecute(String httpResponse) {
        if (null != listner) {
            listner.OnTaskCompleted(httpResponse);
        }
    }
}
