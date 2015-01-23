package com.gtrj.docdeal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.gc.materialdesigndemo.R;


import java.util.Map;

/**
 * Created by zhang77555 on 2015/1/20.
 */
public class DocDetailAdapter extends RecyclerView.Adapter<DocDetailAdapter.DetailViewHolder> {
    public Map[] detailData;

    public DocDetailAdapter(Map[] detailData) {
        this.detailData = detailData;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void onBindViewHolder(DetailViewHolder detailViewHolder, int i) {


        if (detailData[0] != null) {
            TableRow title = new TableRow(detailViewHolder.cardBaseInfo.getContext());
            title.setGravity(Gravity.CENTER);

            TextView titleText = new TextView(detailViewHolder.cardBaseInfo.getContext());
            titleText.setText("基本信息");
            titleText.setTextSize(18);
            titleText.setHeight(120);
            titleText.setGravity(Gravity.CENTER);
            title.addView(titleText);
            detailViewHolder.cardBaseInfo.addView(title);

            for (Object e : detailData[0].keySet()) {
                TableRow tr = new TableRow(detailViewHolder.cardBaseInfo.getContext());
                tr.setBackgroundResource(R.drawable.cell_shape);
                tr.setGravity(Gravity.CENTER_VERTICAL);

                TextView tv = new TextView(detailViewHolder.cardBaseInfo.getContext());
                tv.setText(e.toString());
                tv.setHeight(120);
                tv.setWidth(300);
                tv.setGravity(Gravity.CENTER);
                tr.addView(tv);

                TextView tv2 = new TextView(detailViewHolder.cardBaseInfo.getContext());
                tv2.setText(detailData[0].get(e).toString());
                tv2.setGravity(Gravity.CENTER);
                tv2.setWidth(650);
                tr.addView(tv2);
                detailViewHolder.cardBaseInfo.addView(tr);
            }
            detailViewHolder.cardBaseInfo.getChildAt(detailViewHolder.cardBaseInfo.getChildCount() - 1).setBackgroundResource(R.drawable.cell_shape_bottom);
        }


        if (detailData[1] != null) {
            TableRow title = new TableRow(detailViewHolder.cardFormInfo.getContext());
            title.setGravity(Gravity.CENTER);

            TextView titleText = new TextView(detailViewHolder.cardFormInfo.getContext());
            titleText.setText("表单信息");
            titleText.setTextSize(18);
            titleText.setHeight(120);
            titleText.setGravity(Gravity.CENTER);
            title.addView(titleText);
            detailViewHolder.cardFormInfo.addView(title);

            for (Object e : detailData[1].keySet()) {
                TableRow tr = new TableRow(detailViewHolder.cardFormInfo.getContext());
                tr.setBackgroundResource(R.drawable.cell_shape);
                tr.setGravity(Gravity.CENTER_VERTICAL);

                TextView tv = new TextView(detailViewHolder.cardFormInfo.getContext());
                tv.setText(e.toString());
                tv.setHeight(120);
                tv.setWidth(300);
                tv.setGravity(Gravity.CENTER);
                tr.addView(tv);
                if (((String[]) detailData[1].get(e))[1].equals("true")) {
                    BootstrapEditText bet = new BootstrapEditText(detailViewHolder.cardFormInfo.getContext());
                    bet.setText(((String[]) detailData[1].get(e))[0]);
                    bet.setGravity(Gravity.CENTER);
                    bet.setWidth(650);
                    tr.addView(bet);
                    detailViewHolder.cardFormInfo.addView(tr);
                } else {
                    if (((String[]) detailData[1].get(e))[0] != null && !((String[]) detailData[1].get(e))[0].equals("")) {
                        TextView tv2 = new TextView(detailViewHolder.cardFormInfo.getContext());
                        tv2.setText(((String[]) detailData[1].get(e))[0]);
                        tv2.setGravity(Gravity.CENTER);
                        tv2.setWidth(650);
                        tr.addView(tv2);
                        detailViewHolder.cardFormInfo.addView(tr);
                    }
                }
            }
            detailViewHolder.cardFormInfo.getChildAt(detailViewHolder.cardFormInfo.getChildCount() - 1).setBackgroundResource(R.drawable.cell_shape_bottom);
        }

    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.carddetail, viewGroup, false);
        return new DetailViewHolder(itemView);
    }

    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        protected TableLayout cardBaseInfo;
        protected TableLayout cardFormInfo;

        public DetailViewHolder(View v) {
            super(v);
            cardBaseInfo = (TableLayout) v.findViewById(R.id.cardBaseInfo);
            cardFormInfo = (TableLayout) v.findViewById(R.id.cardFormInfo);
        }
    }
}
