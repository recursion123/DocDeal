package com.gtrj.docdeal.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;


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
        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(entry.getKey());
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RadioButton rb = (RadioButton) v;
                    menuItem.setTitle(rb.getText());
                    ((LinearLayout) scrollView.getChildAt(0)).removeAllViews();
                    CheckBox checkall = new CheckBox(v.getContext());
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
                            }else{
                                c.setSelected(true);
                                for (int i = 0; i < ((LinearLayout) scrollView.getChildAt(0)).getChildCount(); i++) {
                                    ((CheckBox) ((LinearLayout) scrollView.getChildAt(0)).getChildAt(i)).setChecked(true);
                                }
                            }

                        }
                    });
                    for (Map.Entry<String, String> entry : map.get(rb.getText()).entrySet()) {
                        CheckBox checkBox = new CheckBox(v.getContext());
                        checkBox.setText(entry.getKey());
                        checkBox.setGravity(Gravity.CENTER);
                        ((LinearLayout) scrollView.getChildAt(0)).addView(checkBox);
                    }
                    findViewById(R.id.default_text).setVisibility(View.INVISIBLE);
                    pop.dismiss();
                }
            });
            pop.addNewView(radioButton);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }


}
