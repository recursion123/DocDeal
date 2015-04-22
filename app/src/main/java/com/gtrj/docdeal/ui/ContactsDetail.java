package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang77555 on 2015/4/14.
 */
public class ContactsDetail extends Activity {

    private ListView contactsDetailInfo;
    private ProgressBarCircularIndeterminate loading;
    private TextView userName;
    private BootstrapThumbnail bootstrapThumbnail;
    private Button sendMessage;
    private Button call;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> list;
    private List<Map<String, String>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_detail);


        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        loading = (ProgressBarCircularIndeterminate) findViewById(R.id.loading);
        bootstrapThumbnail = (BootstrapThumbnail) findViewById(R.id.thumbnail);
        sendMessage = (Button) findViewById(R.id.send_message);
        call = (Button) findViewById(R.id.call);
        userName = (TextView) findViewById(R.id.user_name);
        userName.setText(getIntent().getExtras().getString("name"));

        contactsDetailInfo = (ListView) findViewById(R.id.contacts_detail_info);
        String[] strs = new String[]{"infoName", "info"};
        int[] ids = {R.id.info_name, R.id.info};
        list = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this, list, R.layout.contacts_detail_info, strs, ids);
        contactsDetailInfo.setAdapter(simpleAdapter);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String phone_number = getIntent().getExtras().getString("phone");
                phone_number = phone_number.trim();
                if (phone_number != null && !phone_number.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + phone_number));
                    ContactsDetail.this.startActivity(intent);
                }
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = getIntent().getExtras().getString("phone");
                Uri smsToUri = Uri.parse("smsto:" + phone_number);
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                startActivity(intent);
            }
        });

        hide();

        Message msg = msgHandler.obtainMessage();
        msg.arg1 = 1;
        msgHandler.sendMessage(msg);
    }

    private void hide() {
        loading.setVisibility(View.VISIBLE);
        bootstrapThumbnail.setVisibility(View.INVISIBLE);
        sendMessage.setVisibility(View.INVISIBLE);
        call.setVisibility(View.INVISIBLE);
        userName.setVisibility(View.INVISIBLE);
    }

    private void show() {
        loading.setVisibility(View.INVISIBLE);
        bootstrapThumbnail.setVisibility(View.VISIBLE);
        sendMessage.setVisibility(View.VISIBLE);
        call.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
    }

    private final Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 1:
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            data = xmlToList(getData());
                            Message msg = msgHandler.obtainMessage();
                            msg.arg1 = 2;
                            msgHandler.sendMessage(msg);
                        }
                    });
                    t.start();
                    break;
                case 2:
                    Map m = new HashMap();
                    m.put("infoName", "手机号码");
                    m.put("info", getIntent().getExtras().getString("phone"));
                    list.add(m);
                    list.addAll(data);
                    simpleAdapter.notifyDataSetChanged();
                    show();
                    break;
                default:
                    break;
            }
        }
    };

    private String getData() {
        Map<String, String> requestDatas = new HashMap<>();
        requestDatas.put("userId", getIntent().getExtras().getString("id"));
        requestDatas.put("bookType", getIntent().getExtras().getString("type"));
        SoapObject obj = new WebService().GetObject(
                ContextString.WebServiceURL,
                ContextString.NameSpace,
                ContextString.DocContactsDetail,
                requestDatas);
        return obj.getProperty(ContextString.DocContactsDetail + "Return").toString();
    }

    public static List<Map<String, String>> xmlToList(String str) {
        if (str == null && str.equals("")) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        try {
            Document document = DocumentHelper.parseText(str);

            Element root = document.getRootElement();

            for (Iterator iter = root.elementIterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                if (element.getName().equals("userinfo")) {
                    for (Iterator iter2 = element.elementIterator(); iter2
                            .hasNext(); ) {
                        Element element2 = (Element) iter2.next();
                        if (element2.getName().equals("职务")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "职务");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("内线电话")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "内线电话");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("外线电话")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "外线电话");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("集团短号")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "集团短号");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("办公电话")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "办公电话");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("传真")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "传真");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                        if (element2.getName().equals("电子邮件")) {
                            Map<String, String> m = new HashMap<>();
                            m.put("infoName", "电子邮件");
                            m.put("info", element2.getText());
                            list.add(m);
                        }
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
