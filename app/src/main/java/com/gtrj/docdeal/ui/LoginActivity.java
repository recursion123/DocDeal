package com.gtrj.docdeal.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang77555 on 2014/12/23.
 */
public class LoginActivity extends Activity {
    private LinearLayout loginActivity;
    private BootstrapEditText username;
    private BootstrapEditText password;
    private BootstrapButton loginBtn;
    private ProgressBarCircularIndeterminate loading;
    private Activity loginContext;
    private SoapObject obj = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginContext = this;

        SharedPreferences preferences=getSharedPreferences("docdeal", 0);
        String s=preferences.getString("login", "");

        /*//如果状态为已登录则跳转到主菜单
        if(!s.equals("")){
            Intent intent = new Intent(loginContext, MainActivity.class);
            startActivity(intent);
            finish();
        }*/

        loginActivity = (LinearLayout) findViewById(R.id.login);
        loginActivity.setBackgroundResource(R.drawable.bg);

        loading = (ProgressBarCircularIndeterminate) findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);
        username = (BootstrapEditText) findViewById(R.id.username);
        password = (BootstrapEditText) findViewById(R.id.password);

        loginBtn = (BootstrapButton) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editable(false);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean loginSuccess = loginSuccess();
                        if (loginSuccess) {
                            Intent intent = new Intent();
                            intent.setClass(loginContext, MainActivity.class);
                            startActivity(intent);
                            loginContext.finish();
                        } else {
                            Message msg = msgHandler.obtainMessage();
                            msg.arg1 = 1;
                            msgHandler.sendMessage(msg);
                        }
                    }
                });
                t.start();
            }
        });
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                    editable(true);
                    break;
                default:
                    break;
            }
        }
    };

    private boolean loginSuccess() {
        Map<String, String> requestDatas = new HashMap<String, String>();
        requestDatas.put("userEmail", username.getText().toString());
        requestDatas.put("userPassword", password.getText().toString());
        obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.Login,
                requestDatas);
        if (obj != null && obj.getPropertyCount() > 0 && obj.getProperty("getUserExistReturn").toString().equals("0")) {
            //登陆后记录当前用户状态！
            SharedPreferences preferences=getSharedPreferences("docdeal", 0);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putString("login", username.getText().toString());
            editor.commit();
            return true;
        } else {
            return false;
        }
    }

    private void editable(boolean flag) {
        username.setEnabled(flag);
        password.setEnabled(flag);
        loginBtn.setEnabled(flag);
        if (flag) {
            loading.setVisibility(View.INVISIBLE);
        } else {
            loading.setVisibility(View.VISIBLE);
        }
    }
}
