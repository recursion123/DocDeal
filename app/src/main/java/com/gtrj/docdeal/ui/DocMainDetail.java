package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhang77555 on 2014/12/26.
 */
public class DocMainDetail extends Activity {
    private SoapObject obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main_detail);
        Toast.makeText(this, getIntent().getExtras().get("DocId").toString(), Toast.LENGTH_SHORT).show();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("公文详细");

    }
    private String getDetailData(int page) {
        Map<String, String> requestDatas = new HashMap<String, String>();
        SharedPreferences preferences = getSharedPreferences("docdeal", 0);
        String username = preferences.getString("login", "");
        requestDatas.put("userSign", username);
        requestDatas.put("offset", "" + page);
        requestDatas.put("rows", "10");
        requestDatas.put("fromWhere", "mobile");
        obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.DocMainList,
                requestDatas);
        if (obj != null && obj.getPropertyCount() > 0) {
            return obj.getProperty(ContextString.DocMainList + "Return").toString();
        } else {
            return null;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
