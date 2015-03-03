package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.adapter.DocDetailAdapter;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.Base64Util;
import com.gtrj.docdeal.util.ContextString;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.ksoap2.serialization.SoapObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang77555 on 2014/12/26.
 */
public class DocMainDetail extends Activity {
    private SoapObject obj;
    private String docId;
    private RecyclerView recList;
    private DocDetailAdapter cAdapter;
    private ProgressBarCircularIndeterminate loading;
    public static String docPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main_detail);
        docId = getIntent().getExtras().get("DocId").toString();

        docPath = null;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("公文详细");

        loading = (ProgressBarCircularIndeterminate) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        recList = (RecyclerView) findViewById(R.id.cardDetail);
        recList.setVisibility(RecyclerView.INVISIBLE);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        final Map[] detailData = new Map[2];
        cAdapter = new DocDetailAdapter(detailData);
        recList.setAdapter(cAdapter);

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
                            cAdapter.detailData = parserXml(getDetailData());
                            Message msg = msgHandler.obtainMessage();
                            msg.arg1 = 2;
                            msgHandler.sendMessage(msg);
                        }
                    });
                    t.start();
                    break;
                case 2:
                    cAdapter.notifyDataSetChanged();
                    recList.setVisibility(RecyclerView.VISIBLE);
                    loading.setVisibility(View.INVISIBLE);
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
        maps[1] = new HashMap<String, String[]>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            Element doc = root.element("公文");
            Element basicData = doc.element("基本信息");
            Element formData = doc.element("表单");
            Element text = doc.element("正文");
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
                        maps[1].put(data.getName(), new String[]{data.getText(), "true"});
                    } else {
                        maps[1].put(data.getName(), new String[]{data.getText(), "false"});
                    }
                }
            }
            if (text != null && text.getText() != null&&!"".equals(text.getText())) {
                docPath = Base64Util.decoderBase64FileWithFileName(text.getText(), "maintext.doc");
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
        return maps;
    }

    //生成对应的菜单,并添加到Menu中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.docdetail, menu);
        return true;
    }
}
