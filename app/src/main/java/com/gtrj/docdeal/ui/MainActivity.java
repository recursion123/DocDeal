package com.gtrj.docdeal.ui;

import com.gc.materialdesign.views.ButtonFloatSmall;
import com.gc.materialdesign.views.LayoutRipple;
import com.gc.materialdesigndemo.R;
import com.nineoldandroids.view.ViewHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;


public class MainActivity extends Activity{

	int backgroundColor = Color.parseColor("#1E88E5");
    public  static Context context;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LayoutRipple layoutRipple = (LayoutRipple) findViewById(R.id.docdeal);
        context=this;

        setOriginRiple(layoutRipple);

        layoutRipple.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,DocMainList.class);
				intent.putExtra("BACKGROUND", backgroundColor);
				startActivity(intent);
			}
		});
        layoutRipple = (LayoutRipple) findViewById(R.id.contact);


        setOriginRiple(layoutRipple);

        layoutRipple.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View arg0) {
                Intent intent = new Intent(MainActivity.this,Contacts.class);
                startActivity(intent);
        	}
        });
        layoutRipple = (LayoutRipple) findViewById(R.id.dateManage);


        setOriginRiple(layoutRipple);

        layoutRipple.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View arg0) {

        	}
        });
        layoutRipple = (LayoutRipple) findViewById(R.id.systemSetting);


        setOriginRiple(layoutRipple);

        layoutRipple.setOnClickListener(new OnClickListener() {

        	@Override
        	public void onClick(View arg0) {

        	}
        });
    }

	private void setOriginRiple(final LayoutRipple layoutRipple){

    	layoutRipple.post(new Runnable() {

			@Override
			public void run() {
				View v = layoutRipple.getChildAt(0);
		    	layoutRipple.setxRippleOrigin(ViewHelper.getX(v)+v.getWidth()/2);
		    	layoutRipple.setyRippleOrigin(ViewHelper.getY(v)+v.getHeight()/2);

		    	layoutRipple.setRippleColor(Color.parseColor("#1E88E5"));

		    	layoutRipple.setRippleSpeed(30);
			}
		});

    }
}
