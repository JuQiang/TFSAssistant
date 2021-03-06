package com.example.juqiang_pc.tfsassistant;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.juqiang_pc.tfsassistant.API.HttpTask;
import com.example.juqiang_pc.tfsassistant.API.TaskCompleted;
import com.example.juqiang_pc.tfsassistant.API.Utils;
import com.example.juqiang_pc.tfsassistant.Entity.Authentication;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView userView;
    private EditText passView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        userView = (AutoCompleteTextView) findViewById(R.id.username);
        passView = (EditText) findViewById(R.id.password);
    }

    public void onLoginClicked(View view) {

        final Intent intentMain = new Intent(this, MainActivity.class);

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        userView.getText().toString(),
                        passView.getText().toString().toCharArray());
            }
        });
        HttpTask ht = new HttpTask(new TaskCompleted() {
            @Override
            public void OnTaskCompleted(Object result) {
                String s = result.toString();
                if (s.length() < 1) {
                    Utils.ShowToast("登录的用户名或者密码错误！");
                } else {
                    Utils.addAuthentication(userView.getText().toString(), passView.getText().toString());
                    startActivity(intentMain);
                }
            }
        });

        ht.execute("http://tfs.teld.cn:8080/tfs/teld/_apis/projects?api-version=2.0");
    }
}

