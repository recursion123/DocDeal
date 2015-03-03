package com.gtrj.docdeal.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;

/**
 * Created by zhang77555 on 2015/3/3.
 */
public class DocMainDetailText extends Activity {
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_main_detail_text);
        Intent intent = getIntent();

        text = (TextView) findViewById(R.id.text);
        Object ob = intent.getExtras().get("text");
        if (ob != null) {
            text.setText(ob.toString());
        }
    }
}
