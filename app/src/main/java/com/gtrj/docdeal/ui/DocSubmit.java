package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.gc.materialdesign.widgets.PopWindow;
import com.gc.materialdesigndemo.R;
import com.gtrj.docdeal.util.SerializableMap;

import java.util.Map;

/**
 * Created by zhang77555 on 2015/3/19.
 */
public class DocSubmit extends Activity {
    private Map<String, Map<String, String>> map;
    private MenuItem menuItem;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_submit);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        SerializableMap serializableMap = (SerializableMap) bundle.get("flowData");
        map = serializableMap.getMap();
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("公文送审");

        scrollView = (ScrollView) findViewById(R.id.dept_list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
            case R.id.dept_select:
                menuItem = item;
                showPopup();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showPopup() {
        final PopWindow pop = new PopWindow(this, "部门选择");
        pop.show();
        ListView listView = new ListView(this);
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, map.keySet().size() * 150 > 600 ? 600 : map.keySet().size() * 150));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.dept_select_list_item, map.keySet().toArray(new String[map.keySet().size()]));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                menuItem.setTitle(textView.getText());
                ((LinearLayout) scrollView.getChildAt(0)).removeAllViews();
                CheckBox checkall = new CheckBox(view.getContext());
                checkall.setText("全选");
                checkall.setGravity(Gravity.CENTER);
                ((LinearLayout) scrollView.getChildAt(0)).addView(checkall);
                checkall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox c = (CheckBox) v;
                        if (c.isSelected()) {
                            c.setSelected(false);
                            for (int i = 0; i < ((LinearLayout) scrollView.getChildAt(0)).getChildCount(); i++) {
                                ((CheckBox) ((LinearLayout) scrollView.getChildAt(0)).getChildAt(i)).setChecked(false);
                            }
                        } else {
                            c.setSelected(true);
                            for (int i = 0; i < ((LinearLayout) scrollView.getChildAt(0)).getChildCount(); i++) {
                                ((CheckBox) ((LinearLayout) scrollView.getChildAt(0)).getChildAt(i)).setChecked(true);
                            }
                        }

                    }
                });
                for (Map.Entry<String, String> entry : map.get(textView.getText()).entrySet()) {
                    CheckBox checkBox = new CheckBox(view.getContext());
                    checkBox.setText(entry.getKey());
                    checkBox.setGravity(Gravity.CENTER);
                    ((LinearLayout) scrollView.getChildAt(0)).addView(checkBox);
                }
                findViewById(R.id.default_text).setVisibility(View.INVISIBLE);
                pop.dismiss();
            }
        });
        pop.addNewView(listView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }


}
