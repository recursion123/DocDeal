package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.widgets.PopWindow;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.adapter.SearchConditionAdapter;
import com.gtrj.docdeal.net.WebService;
import com.gtrj.docdeal.util.ContextString;

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
 * Created by zhang77555 on 2015/4/22.
 */
public class SearchActivity extends Activity {

    private MenuItem menuItem;
    private Map<String, List<String>>[] map;
    private ListView listView;
    private List<String> conditions = new ArrayList<>();
    private SearchConditionAdapter conditionAdapter;
    private Button search;

    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y;

        search=(Button)findViewById(R.id.search);
        search.setVisibility(View.INVISIBLE);

        listView = (ListView) findViewById(R.id.search_condition_list);
        conditionAdapter = new SearchConditionAdapter(this, conditions, R.layout.search_condition_item);
        listView.setAdapter(conditionAdapter);

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
                            map = parseXml(getData());
                            Message msg = msgHandler.obtainMessage();
                            msg.arg1 = 2;
                            msgHandler.sendMessage(msg);
                        }
                    });
                    t.start();
                    break;
                case 2:
                    menuItem.setTitle("请选择搜索公文的类型");
                    break;
                default:
                    break;
            }
        }
    };

    private String getData() {
        Map<String, String> requestDatas = new HashMap<>();
        SharedPreferences preferences = getSharedPreferences("docdeal", 0);
        String username = preferences.getString("login", "");
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
            case R.id.search_condition:
                menuItem = item;
                showPopup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup() {
        final PopWindow pop = new PopWindow(this, "部门选择");
        pop.show();
        ListView selectListView = new ListView(this);
        selectListView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, map[1].keySet().size() * 150 > 600 ? 600 : map[1].keySet().size() * 150));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dept_select_list_item, map[1].keySet().toArray(new String[map[1].keySet().size()]));
        selectListView.setAdapter(adapter);
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                menuItem.setTitle(textView.getText());
                conditionAdapter.list.clear();
                conditionAdapter.list.addAll(map[0].get("commonCondition"));
                conditionAdapter.list.addAll(map[1].get(textView.getText()));
                listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, conditionAdapter.list.size() * 170 > height*7/10 ? height*7/10 : conditionAdapter.list.size() * 170));
                conditionAdapter.notifyDataSetChanged();
                search.setVisibility(View.VISIBLE);
                pop.dismiss();
            }
        });
        pop.addNewView(selectListView);
    }

    private Map<String, List<String>>[] parseXml(String xml) {
        Map<String, List<String>>[] result = new HashMap[2];
        result[0] = new HashMap<>();
        result[1] = new HashMap<>();
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();
            List<Element> commonCondition = root.element("workflows").elements("condition");
            List<String> commonConditions = new ArrayList<>();
            for (int i = 0; i < commonCondition.size(); i++) {
                if (commonCondition.get(i).getText() != null) {
                    commonConditions.add(commonCondition.get(i).getText());
                }
            }
            result[0].put("commonCondition", commonConditions);
            List<Element> workFlows = root.element("workflows").elements("workflow");
            for (int i = 0; i < workFlows.size(); i++) {
                if (workFlows.get(i).element("name").getText() != null) {
                    List<Element> elements = workFlows.get(i).elements("condition");
                    List<String> types = new ArrayList<>();
                    for (int m = 0; m < elements.size(); m++) {
                        types.add(elements.get(m).getText());
                    }
                    result[1].put(workFlows.get(i).element("name").getText(), types);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        menuItem = menu.findItem(R.id.search_condition);
        return true;
    }
}
