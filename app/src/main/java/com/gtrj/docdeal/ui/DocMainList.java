package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.adapter.DocMainListAdapter;
import com.gtrj.docdeal.bean.DocInfo;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;
import com.gtrj.docdeal.util.DocApplication;
import com.gtrj.docdeal.util.RecyclerItemClickListener;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhang77555 on 2014/12/24.
 */
public class DocMainList extends Activity implements
        SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recList;
    private SoapObject obj;
    private DocMainListAdapter cAdapter;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefresh = false;
    private int page = 1;
    private Context context;
    private ProgressBarCircularIndeterminate loading;

    private Boolean isPullDown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main_list);
        context = this;

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        loading = (ProgressBarCircularIndeterminate) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        final List<DocInfo> list = new ArrayList<>();
        cAdapter = new DocMainListAdapter(list);
        recList.setAdapter(cAdapter);
        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent();
                        intent.setClass(context, DocMainDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("DocId", cAdapter.contactList.get(cAdapter.contactList.size() - 1 - position).getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                })
        );
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
                            if (!isPullDown&&DocApplication.lruCache.get("DocList") != null) {
                                cAdapter.contactList = (List<DocInfo>) DocApplication.lruCache.get("DocList");
                            } else {
                                cAdapter.contactList.addAll(cAdapter.contactList.size(), parserXml(getListData(page)));
                                DocApplication.lruCache.put("DocList",cAdapter.contactList);
                                isPullDown=false;
                            }
                            Message msg = msgHandler.obtainMessage();
                            msg.arg1 = 2;
                            msgHandler.sendMessage(msg);
                        }
                    });
                    t.start();
                    break;
                case 2:
                    cAdapter.notifyDataSetChanged();
                    loading.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };

    private String getListData(int page) {
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
    public void onRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    swipeLayout.setRefreshing(false);
                    page++;
                    isPullDown=true;
                    Message msg = msgHandler.obtainMessage();
                    msg.arg1 = 1;
                    msgHandler.sendMessage(msg);
                    isRefresh = false;
                }
            }, 2000);
        }
    }

    private List<DocInfo> parserXml(String xml) {
        List<DocInfo> oList = new ArrayList<DocInfo>();
        try {
            Document document = DocumentHelper.parseText(xml);

            Element root = document.getRootElement();
            List docList = root.elements("公文");

            for (int i = 0; i < docList.size(); i++) {
                DocInfo od = new DocInfo();
                Element doc = (Element) docList.get(i);
                od.setTitle(doc.elementText("标题"));
                od.setDate(doc.elementText("发送时间"));
                od.setSender(doc.elementText("发送人"));
                od.setType(doc.elementText("文件类型"));
                od.setId(doc.attributeValue("标识符"));
                od.setReturnCode(doc.attributeValue("回写标识"));
                oList.add(od);
            }
        } catch (DocumentException e) {
            System.out.println(e.getMessage());
        }
        return oList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.search_btn:
                startActivity(new Intent(context,SearchActivity.class));
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //生成对应的菜单,并添加到Menu中
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
