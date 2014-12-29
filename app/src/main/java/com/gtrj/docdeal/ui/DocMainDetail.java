package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;

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
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main_detail);
        docId = getIntent().getExtras().get("DocId").toString();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("公文详细");

        Message msg = msgHandler.obtainMessage();
        msg.arg1 = 1;
        msgHandler.sendMessage(msg);
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("result",getDetailData());
                        }
                    });
                    t.start();
                    break;
                default:
                    break;
            }
        }
    };

    private String getDetailData() {
        Map<String, String> requestDatas = new HashMap<String, String>();
        requestDatas.put("gwSign", docId);
        obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.DocMainDetail,
                requestDatas);
        if (obj != null && obj.getPropertyCount() > 0) {
            return obj.getProperty(ContextString.DocMainDetail + "Return").toString();
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
