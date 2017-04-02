package com.example.juqiang_pc.tfsassistant.API;

import android.os.AsyncTask;

/**
 * Created by JuQiang on 4/2/2017.
 */

public class HttpCacheTask extends AsyncTask<String, Integer, String> {
    private TaskCompleted listner;

    public HttpCacheTask(TaskCompleted listner) {
        this.listner = listner;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0].toLowerCase();
        String cache = params[1].toLowerCase();
        String hashkey = params[2].toLowerCase();
        String ret = HttpHelper.downloadString(urlString,cache.equals("true"),hashkey);

        return ret;
    }

    @Override
    protected void onPostExecute(String httpResponse) {
        if (null != listner) {
            listner.OnTaskCompleted(httpResponse);
        }
    }
}
