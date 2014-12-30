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

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
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
                           Map mmm=parserXml(getDetailData())[1];
                            for(Object e:mmm.keySet()){
                                Log.e("result", ((String[])e)[0]+"   "+((String[])e)[1]);
                            }

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

    private Map[] parserXml(String xml) {
        Map[] maps = new Map[2];
        maps[0] = new HashMap<String, String>();
        maps[1] = new HashMap<String[], String>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Element doc = root.element("公文");
            Element basicData = doc.element("基本信息");
            Element formData = doc.element("表单");
            for (int i = 0; i < basicData.nodeCount(); i++) {
                Node data = basicData.getXPathResult(i);
                if (data != null && data.getName() != null && !data.getName().equals("null") && !data.getName().equals("公文标识")) {
                    maps[0].put(data.getName(), data.getText());
                }
            }
            for (int i = 0; i < formData.nodeCount(); i++) {
                Node data = formData.getXPathResult(i);
                if (data != null && data.getName() != null && !data.getName().equals("null") && !data.getName().equals("公文标识")) {
                    Attribute at = formData.element(data.getName()).attribute("canwrite");
                    if (at != null && at.getValue().equals("true")) {
                        maps[1].put(new String[]{data.getName(), "true"}, data.getText());
                    } else {
                        maps[1].put(new String[]{data.getName(), "false"}, data.getText());
                    }
                }
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
        return maps;
    }
}
