package com.gc.materialdesign.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gc.materialdesign.R;

/**
 * Created by zhang77555 on 2015/3/17.
 */
public class PopWindow extends android.app.Dialog {

    Context context;
    View view;
    View backView;
    String title;
    ScrollView scrollView;
    TextView titleTextView;

    public PopWindow(Context context, String title) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;// init Context
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_window);

        view = (RelativeLayout) findViewById(R.id.pop_win);
        scrollView = (ScrollView) findViewById(R.id.button_scrollView);

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.title);

        backView = (RelativeLayout) findViewById(R.id.dialog_rootView);
        backView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft()
                        || event.getX() > view.getRight()
                        || event.getY() > view.getBottom()
                        || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });
    }

    public void addNewView(View view) {
        ((LinearLayout) scrollView.getChildAt(0)).addView(view);
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_amination);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        PopWindow.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_amin);

        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }
}
