package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang77555 on 2015/4/22.
 */
public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("result",getData());
            }
        });
        t.start();
    }

    private String getData() {
        Map<String, String> requestDatas = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("docdeal", 0);
        String username = preferences.getString("login", "");
        requestDatas.put("userSign", username);
        requestDatas.put("userSign", username);
        SoapObject obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.SearchCondition,
                requestDatas);
        return obj.getProperty(ContextString.SearchCondition + "Return").toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
