package com.example.juqiang_pc.tfsassistant.API;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.juqiang_pc.tfsassistant.Entity.Authentication;
import com.example.juqiang_pc.tfsassistant.Entity.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class Utils {
    final private static String packageName = "com.example.juqiang_pc.tfsassistant.API";
    public static String orgPortalUrl = "http://tfs.teld.cn:8080/tfs/teld/c955f4f8-3b05-4afc-9969-3a54f7b70533/_apis/Dashboard/Groups/ad0bf755-9688-4d6a-95f9-4e20702a2972?apiversion=2.0";
    public static String projectID = "c955f4f8-3b05-4afc-9969-3a54f7b70533";//orgportal
    public static String teamID = "ad0bf755-9688-4d6a-95f9-4e20702a2972";
    public static String prefixURL = "http://tfs.teld.cn:8080/tfs/Teld/";
    public static List<User> userList = new ArrayList<User>();

    public static int displayWidth;
    public static int displayHeight;
    private static Context context;

    public static void ShowToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void setContext(Context con) {
        context = con;
    }

    public static Bitmap DownloadBitmap(String urlString, String cacheKey) {
        boolean needCache = true;

        HttpURLConnection urlConnection = null;
        Bitmap ret = null;
        String cachedFilename = GetHash(cacheKey) + ".bitmap";

        if (needCache) {
            boolean exist = fileExists(cachedFilename);
            if (exist) {
                byte[] buf = readExternallStoragePublic(cachedFilename);
                ret = BitmapFactory.decodeByteArray(buf, 0, buf.length);

                return ret;
            }
        }

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream input = new BufferedInputStream(urlConnection.getInputStream());
            String path = getDiskCacheDir(context, packageName);
            int fileLength = urlConnection.getContentLength();

            if (isExternalStorageAvailable() &&
                    !isExternalStorageReadOnly()) {
                try {
                    File file = new File(path);
                    file.mkdirs();
                    FileOutputStream fos = new FileOutputStream(path + cachedFilename);

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
//                if ( isCancelled()) {
//                    input.close();
//                    return null;
//                }
//                        total += count;
                        fos.write(data, 0, count);
                    }
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            input.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return ret;
    }

    public static void WriteLog(String tag, String message) {
        Log.i(tag, message + "-----" + String.valueOf(System.currentTimeMillis()));
    }

    private static String GetHash(String str) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

    private static boolean isExternalStorageAvailable() {
        boolean state = false;
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            state = true;
        }
        return state;
    }

    private static boolean isExternalStorageReadOnly() {
        boolean state = false;
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            state = true;
        }
        return state;
    }

    public static void writeToExternalStoragePublic(String filename, byte[] content) {
        String path = getDiskCacheDir(context, packageName);

        if (isExternalStorageAvailable() &&
                !isExternalStorageReadOnly()) {
            try {
                File file = new File(path);
                file.mkdirs();
                FileOutputStream fos = new FileOutputStream(path + filename);
                fos.write(content);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] readExternallStoragePublic(String filename) {
        int len = 1024;
        byte[] buffer = new byte[len];

        WriteLog("====boolean2.1===", filename);
        String path = getDiskCacheDir(context, packageName);
        WriteLog("====boolean2.2===", path);
        if (!isExternalStorageReadOnly()) {
            try {
                FileInputStream fis = new FileInputStream(path + filename);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int nrb = fis.read(buffer, 0, len); //read up to len bytes
                while (nrb != -1) {
                    baos.write(buffer, 0, nrb);
                    nrb = fis.read(buffer, 0, len);
                }
                buffer = baos.toByteArray();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        WriteLog("====boolean2.5===", "");
        return buffer;
    }

    public static void deleteExternalStoragePublicFile(String filename) {
        String path = getDiskCacheDir(context, packageName);
        File file = new File(path, filename);
        if (file != null) {
            file.delete();
        }
    }

    public static void writeInternalStorage(
            String filename, byte[] content) {
        try {
            //MODE_PRIVATE creates/replaces a file and makes
            //  it private to your application. Other modes:
            //    MODE_WORLD_WRITEABLE
            //    MODE_WORLD_READABLE
            //    MODE_APPEND
            FileOutputStream fos = new FileOutputStream(filename);

            //        openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(content);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean fileExists(String filename) {
        String path = getDiskCacheDir(context, packageName);

        boolean ret = false;
        try {
            File file = new File(path, filename);
            ret = file.canRead();
            boolean ret2 = file.canExecute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ret;
        /*
        File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir + filename);
        return file.canRead();
        */
        //http://stackoverflow.com/questions/21579468/android-file-exists-returns-false-for-existing-file-for-anything-different
    }

    public static byte[] readInternalStorage(String filename) {
        int len = 1024;
        byte[] buffer = new byte[len];
        try {
            FileInputStream fis = new FileInputStream(filename);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int nrb = fis.read(buffer, 0, len); // read up to len bytes
            while (nrb != -1) {
                baos.write(buffer, 0, nrb);
                nrb = fis.read(buffer, 0, len);
            }
            buffer = baos.toByteArray();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void deleteInternalStoragePrivate(String filename) {

        File file = new File(filename);
        if (file != null) {
            file.delete();
        }
    }

    public static String getDiskCacheDir(Context context, String packageName) {

        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            try {
                cachePath = context.getExternalCacheDir().getPath();
            } catch (Exception e) {
                cachePath = context.getCacheDir().getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator;
    }

    public static void addAuthentication(String user, String pass) {
        String filename = "hello.world";
        FileInputStream fis = null;
        BufferedReader br = null;

        String path = getDiskCacheDir(context, packageName);
        if (fileExists(filename)) {
            deleteInternalStoragePrivate(filename);
        }
        try {

            FileOutputStream fos = new FileOutputStream(path + filename);
            fos.write((user + "," + pass + "\r\n").getBytes());
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static Authentication getAuthentication() {
        String filename = "hello.world";
        FileInputStream fis = null;
        BufferedReader br = null;
        Authentication auth = new Authentication();

        String path = getDiskCacheDir(context, packageName);
        if (fileExists(filename)) {
            try {
                fis = new FileInputStream(path + filename);
                br = new BufferedReader(new InputStreamReader(fis));

                String line = br.readLine();
                if (line != null) {
                    String[] tmp = line.split(",");
                    auth.user = tmp[0].toLowerCase();
                    auth.password = tmp[1];
                }
                fis.close();
                br.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            auth.user = "";
            auth.password = "";
        }

        return auth;
    }
}
